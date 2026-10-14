package com.example.matchmateapp.data.local

import androidx.room.TypeConverter
import com.example.matchmateapp.domain.model.MatchStatus

class MatchTypeConverters {
    @TypeConverter
    fun fromStatus(status: MatchStatus): String = status.name

    @TypeConverter
    fun toStatus(value: String): MatchStatus = MatchStatus.valueOf(value)
}
