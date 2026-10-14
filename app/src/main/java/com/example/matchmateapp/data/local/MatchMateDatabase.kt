package com.example.matchmateapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.matchmateapp.data.local.dao.MatchUserDao
import com.example.matchmateapp.data.local.dao.RemoteKeysDao
import com.example.matchmateapp.data.local.entity.MatchUserEntity

@Database(
    entities = [MatchUserEntity::class, RemoteKeyEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(MatchTypeConverters::class)
abstract class MatchMateDatabase : RoomDatabase() {
    abstract fun matchUserDao(): MatchUserDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}
