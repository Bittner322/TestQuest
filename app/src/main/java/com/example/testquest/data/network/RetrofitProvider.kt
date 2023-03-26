package com.example.testquest.data.network

import com.example.testquest.data.network.services.CustomAPIService
import com.example.testquest.data.network.services.DogsService
import com.example.testquest.data.network.services.NationalityService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Inject
import javax.inject.Singleton

private const val DOGS_API = "https://dog.ceo/api/"

@Singleton
class RetrofitProvider @Inject constructor() {

    private val contentType = "application/json".toMediaType()

    private val jsonConverter = Json {
        prettyPrint = false
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
    }

    private val currentBaseUrl: String = DOGS_API

    private var _retrofit: Retrofit = createRetrofit(currentBaseUrl)
    val retrofit: Retrofit
        get() = _retrofit

    fun updateBaseUrl(url: String) {
        _retrofit = createRetrofit(url)
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(jsonConverter.asConverterFactory(contentType))
            .build()
    }
}

fun RetrofitProvider.createDogsService(): DogsService {
    return retrofit.create()
}

fun RetrofitProvider.createNationalityService(): NationalityService {
    return retrofit.create()
}

fun RetrofitProvider.createCustomApiService(): CustomAPIService {
    return retrofit.create()
}