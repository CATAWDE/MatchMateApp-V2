package com.example.matchmateapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.paging.PagingSource
import com.example.matchmateapp.data.local.entity.MatchUserEntity
import com.example.matchmateapp.domain.model.MatchStatus

@Dao
interface MatchUserDao {
    @Query("SELECT * FROM match_users ORDER BY rowid ASC")
    fun pagingSource(): PagingSource<Int, MatchUserEntity>

    @Query("SELECT * FROM match_users WHERE id IN (:ids)")
    suspend fun getByIds(ids: List<String>): List<MatchUserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(users: List<MatchUserEntity>)

    @Query("UPDATE match_users SET status = :status WHERE id = :userId")
    suspend fun updateStatus(userId: String, status: MatchStatus)
}
