package com.example.testquest.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DogsResponse(
    @SerialName("message")
    val pictureUrl: String
)