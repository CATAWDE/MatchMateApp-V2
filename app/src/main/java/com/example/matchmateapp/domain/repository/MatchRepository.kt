package com.example.matchmateapp.domain.repository

import androidx.paging.PagingData
import com.example.matchmateapp.domain.model.MatchStatus
import com.example.matchmateapp.domain.model.MatchUser
import kotlinx.coroutines.flow.Flow

interface MatchRepository {
    fun observeMatches(): Flow<PagingData<MatchUser>>
    suspend fun updateStatus(userId: String, status: MatchStatus)
}
