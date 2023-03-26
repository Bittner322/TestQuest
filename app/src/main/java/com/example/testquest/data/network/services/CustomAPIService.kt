package com.example.testquest.data.network.services

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface CustomAPIService {

    @GET
    suspend fun getCustomApiResponse(
        @Url url: String = "google.com/",
    ): ResponseBody

}