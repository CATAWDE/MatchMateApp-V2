package com.example.matchmateapp.data.mapper

import com.example.matchmateapp.data.local.entity.MatchUserEntity
import com.example.matchmateapp.data.remote.dto.RandomUserDto
import com.example.matchmateapp.domain.model.MatchStatus
import com.example.matchmateapp.domain.model.MatchUser

fun RandomUserDto.toEntity(existing: MatchUserEntity?): MatchUserEntity? {
    val id = login?.uuid ?: return null
    val name = listOfNotNull(name?.first, name?.last)
        .joinToString(" ")
        .ifBlank { "Unknown" }

    return MatchUserEntity(
        id = id,
        name = name,
        age = dob?.age,
        city = location?.city,
        country = location?.country,
        imageUrl = picture?.large ?: picture?.medium,
        // Critical requirement: existing decision wins.
        status = existing?.status ?: MatchStatus.PENDING
    )
}

fun MatchUserEntity.toDomain(): MatchUser =
    MatchUser(
        id = id,
        name = name,
        age = age,
        city = city,
        country = country,
        imageUrl = imageUrl,
        status = status
    )
