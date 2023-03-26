package com.example.testquest.data.repositories

import com.example.testquest.data.network.ResultWrapper
import com.example.testquest.data.network.RetrofitProvider
import com.example.testquest.data.network.createNationalityService
import com.example.testquest.data.network.models.NationalityResponse
import com.example.testquest.data.network.safeApiCall
import com.example.testquest.data.network.services.NationalityService
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class NationalityRepository @Inject constructor(
    private val retrofitProvider: RetrofitProvider
) {
    private val nationalityService: NationalityService
        get() = retrofitProvider.createNationalityService()

    suspend fun getNationalityCountry(nationality: List<String>): ResultWrapper<List<NationalityResponse>> {
        return safeApiCall(Dispatchers.IO) {

            val url = nationality.joinToString(
                separator = "&",
                prefix = "?",
                postfix = ""
            ) { "name[]=$it" }

            nationalityService.getNationality(url)
        }
    }
}