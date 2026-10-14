package com.example.matchmateapp.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.matchmateapp.data.local.MatchMateDatabase
import com.example.matchmateapp.data.local.RemoteKeyEntity
import com.example.matchmateapp.data.local.entity.MatchUserEntity
import com.example.matchmateapp.data.mapper.toEntity
import com.example.matchmateapp.data.remote.api.MatchApiService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MatchRemoteMediator(
    private val api: MatchApiService,
    private val database: MatchMateDatabase
) : RemoteMediator<Int, MatchUserEntity>() {

    private val userDao = database.matchUserDao()
    private val keysDao = database.remoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MatchUserEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1

            LoadType.PREPEND -> {
                val remoteKey = getRemoteKeyForFirstItem(state)
                remoteKey?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
            }

            LoadType.APPEND -> {
                val remoteKey = getRemoteKeyForLastItem(state)
                remoteKey?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
            }
        }

        return try {
            val response = api.getMatches(
                page = page,
                results = state.config.pageSize
            )

            val ids = response.results.mapNotNull { it.login?.uuid }

            // Fetch only users that already exist locally.
            val existingUsers = if (ids.isEmpty()) {
                emptyList()
            } else {
                userDao.getByIds(ids)
            }

            // Fast lookup: id -> existing Room entity.
            val existingById = existingUsers.associateBy { it.id }

            // Merge API data with existing local decisions.
            val finalUsers = response.results.mapNotNull { dto ->
                dto.toEntity(existingById[dto.login?.uuid])
            }

            val endOfPaginationReached = finalUsers.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    keysDao.clearRemoteKeys()
                    // IMPORTANT: do not clear users.
                    // Existing Accept/Decline decisions must survive refresh.
                }

                keysDao.insertAll(
                    finalUsers.map {
                        RemoteKeyEntity(
                            userId = it.id,
                            prevKey = if (page == 1) null else page - 1,
                            nextKey = if (endOfPaginationReached) null else page + 1
                        )
                    }
                )

                userDao.upsertAll(finalUsers)
            }

            MediatorResult.Success(endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, MatchUserEntity>
    ): RemoteKeyEntity? =
        state.lastItemOrNull()?.let { keysDao.remoteKeysByUserId(it.id) }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, MatchUserEntity>
    ): RemoteKeyEntity? =
        state.firstItemOrNull()?.let { keysDao.remoteKeysByUserId(it.id) }
}
