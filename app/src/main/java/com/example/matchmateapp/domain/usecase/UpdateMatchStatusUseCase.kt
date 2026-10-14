package com.example.matchmateapp.domain.usecase

import com.example.matchmateapp.domain.model.MatchStatus
import com.example.matchmateapp.domain.repository.MatchRepository
import javax.inject.Inject

class UpdateMatchStatusUseCase @Inject constructor(
    private val repository: MatchRepository
) {
    suspend operator fun invoke(userId: String, status: MatchStatus) {
        require(userId.isNotBlank())
        repository.updateStatus(userId, status)
    }
}
