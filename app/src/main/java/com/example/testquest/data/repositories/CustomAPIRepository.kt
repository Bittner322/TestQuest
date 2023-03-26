package com.example.testquest.data.repositories

import com.example.testquest.data.network.ResultWrapper
import com.example.testquest.data.network.RetrofitProvider
import com.example.testquest.data.network.createCustomApiService
import com.example.testquest.data.network.safeApiCall
import com.example.testquest.data.network.services.CustomAPIService
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CustomAPIRepository @Inject constructor(
    private val retrofitProvider: RetrofitProvider
) {
    private val customAPIService: CustomAPIService
        get() = retrofitProvider.createCustomApiService()

    suspend fun getCustomApiContent(url: String): ResultWrapper<String> {
        return safeApiCall(Dispatchers.IO) {
            customAPIService.getCustomApiResponse(url = url).string()
        }
    }
}