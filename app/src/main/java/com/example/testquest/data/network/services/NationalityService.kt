package com.example.testquest.data.network.services

import com.example.testquest.data.network.models.NationalityResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface NationalityService {

    @GET
    suspend fun getNationality(
        @Url url: String = "?name[]=michael&name[]=matthew&name[]=jane",
    ): List<NationalityResponse>

}