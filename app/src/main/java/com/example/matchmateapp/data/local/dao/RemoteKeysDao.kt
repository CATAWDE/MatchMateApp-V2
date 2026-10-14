package com.example.matchmateapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.matchmateapp.data.local.RemoteKeyEntity

@Dao
interface RemoteKeysDao {
    @Query("SELECT * FROM remote_keys WHERE userId = :userId")
    suspend fun remoteKeysByUserId(userId: String): RemoteKeyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<RemoteKeyEntity>)

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}
