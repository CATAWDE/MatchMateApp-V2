package com.example.matchmateapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.matchmateapp.domain.model.MatchStatus

@Entity(tableName = "match_users")
data class MatchUserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val age: Int?,
    val city: String?,
    val country: String?,
    val imageUrl: String?,
    val status: MatchStatus = MatchStatus.PENDING
)
