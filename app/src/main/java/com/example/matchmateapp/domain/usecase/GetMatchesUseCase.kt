package com.example.matchmateapp.domain.usecase

import androidx.paging.PagingData
import com.example.matchmateapp.domain.model.MatchUser
import com.example.matchmateapp.domain.repository.MatchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMatchesUseCase @Inject constructor(
    private val repository: MatchRepository
) {
    operator fun invoke(): Flow<PagingData<MatchUser>> =
        repository.observeMatches()
}
