package com.example.testquest.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NationalityResponse(
    @SerialName("country")
    val country: List<Country>,
    @SerialName("name")
    val name: String,
) {
    @Serializable
    data class Country(
        @SerialName("country_id")
        val countryId: String,
        @SerialName("probability")
        val probability: Double
    )
}