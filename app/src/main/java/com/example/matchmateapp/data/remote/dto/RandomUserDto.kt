package com.example.matchmateapp.data.remote.dto

data class RandomUserResponseDto(
    val results: List<RandomUserDto> = emptyList()
)

data class RandomUserDto(
    val login: LoginDto?,
    val name: NameDto?,
    val dob: DobDto?,
    val location: LocationDto?,
    val picture: PictureDto?
)

data class LoginDto(val uuid: String?)
data class NameDto(val title: String?, val first: String?, val last: String?)
data class DobDto(val age: Int?)
data class LocationDto(val city: String?, val country: String?)
data class PictureDto(val large: String?, val medium: String?)
