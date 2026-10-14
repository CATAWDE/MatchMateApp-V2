package com.example.matchmateapp.domain.model

enum class MatchStatus {
    PENDING, ACCEPTED, DECLINED
}

data class MatchUser(
    val id: String,
    val name: String,
    val age: Int?,
    val city: String?,
    val country: String?,
    val imageUrl: String?,
    val status: MatchStatus = MatchStatus.PENDING
)
