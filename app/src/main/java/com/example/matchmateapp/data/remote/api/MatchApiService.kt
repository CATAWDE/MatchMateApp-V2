package com.example.matchmateapp.data.remote.api

import com.example.matchmateapp.data.remote.dto.RandomUserResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MatchApiService {
    @GET("api/")
    suspend fun getMatches(
        @Query("page") page: Int,
        @Query("results") results: Int,
        @Query("seed") seed: String = "matchmate"
    ): RandomUserResponseDto
}
