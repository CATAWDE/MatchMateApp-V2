package com.example.matchmateapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey val userId: String,
    val prevKey: Int?,
    val nextKey: Int?
)
