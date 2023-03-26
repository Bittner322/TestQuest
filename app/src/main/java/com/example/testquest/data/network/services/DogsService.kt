package com.example.testquest.data.network.services

import com.example.testquest.data.network.models.DogsResponse
import retrofit2.http.GET

interface DogsService {

    @GET("breeds/image/random")
    suspend fun getDog(): DogsResponse
}