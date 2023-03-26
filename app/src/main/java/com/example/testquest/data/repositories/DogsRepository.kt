package com.example.testquest.data.repositories

import com.example.testquest.data.network.ResultWrapper
import com.example.testquest.data.network.RetrofitProvider
import com.example.testquest.data.network.createDogsService
import com.example.testquest.data.network.safeApiCall
import com.example.testquest.data.network.services.DogsService
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DogsRepository @Inject constructor(
    private val retrofitProvider: RetrofitProvider
) {
    private val dogsService: DogsService
        get() = retrofitProvider.createDogsService()

    suspend fun getDog(): ResultWrapper<String> {
        return safeApiCall(Dispatchers.IO) {
            dogsService.getDog().pictureUrl
        }
    }
}