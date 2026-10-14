package com.example.matchmateapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.matchmateapp.data.local.MatchMateDatabase
import com.example.matchmateapp.data.mapper.toDomain
import com.example.matchmateapp.data.paging.MatchRemoteMediator
import com.example.matchmateapp.data.remote.api.MatchApiService
import com.example.matchmateapp.domain.model.MatchStatus
import com.example.matchmateapp.domain.model.MatchUser
import com.example.matchmateapp.domain.repository.MatchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MatchRepositoryImpl @Inject constructor(
    private val api: MatchApiService,
    private val database: MatchMateDatabase
) : MatchRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun observeMatches(): Flow<PagingData<MatchUser>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 3,
                enablePlaceholders = false
            ),
            remoteMediator = MatchRemoteMediator(api, database),
            pagingSourceFactory = { database.matchUserDao().pagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }

    override suspend fun updateStatus(
        userId: String,
        status: MatchStatus
    ) {
        database.matchUserDao().updateStatus(userId, status)
    }
}
