package com.example.testquest.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T): ResultWrapper<T>()
    data class GenericError(val code: Int?, val error: ErrorResponse? = null): ResultWrapper<Nothing>()
    object NetworkError: ResultWrapper<Nothing>()
}

@Serializable
data class ErrorResponse(
    @SerialName("error")
    val error: String
)

fun <T> ResultWrapper<T>.onGenericError(call: (code: Int?, errorResponse: ErrorResponse?) -> Unit): ResultWrapper<T> {
    if (this is ResultWrapper.GenericError) {
        call.invoke(this.code, this.error)
    }
    return this
}

fun <T> ResultWrapper<T>.onResultSuccess(call: (T) -> Unit): ResultWrapper<T> {
    if (this is ResultWrapper.Success) {
        call.invoke(this.value)
    }
    return this
}
