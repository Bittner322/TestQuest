package com.example.testquest.data.repositories

import android.content.SharedPreferences
import com.example.testquest.data.network.RetrofitProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

private const val SHARED_PREFS_KEY = "sharedPrefsKey"

private const val DOGS_API = "https://dog.ceo/api/"

class BaseApiRepository @Inject constructor(
    private val sharedPrefs: SharedPreferences,
    private val retrofitProvider: RetrofitProvider
) {
    fun getApiSharedPrefsFlow(): Flow<String> {
        return sharedPrefs.observeKey(SHARED_PREFS_KEY, DOGS_API)
    }

    fun setCurrentBaseApi() {
        val currentApi = sharedPrefs.getString(SHARED_PREFS_KEY, DOGS_API)!!

        if (currentApi != "") {
            retrofitProvider.updateBaseUrl(currentApi)
        }
    }
}

inline fun <reified T> SharedPreferences.observeKey(key: String, default: T): Flow<T> {
    val flow = MutableStateFlow(getItem(key, default))

    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, k ->
        if (key == k) {
            flow.value = getItem(key, default)!!
        }
    }
    registerOnSharedPreferenceChangeListener(listener)

    return flow
        .onCompletion { unregisterOnSharedPreferenceChangeListener(listener) }
}

inline fun <reified T> SharedPreferences.getItem(key: String, default: T): T {
    @Suppress("UNCHECKED_CAST")
    return when (default){
        is String -> getString(key, default) as T
        is Int -> getInt(key, default) as T
        is Long -> getLong(key, default) as T
        is Boolean -> getBoolean(key, default) as T
        is Float -> getFloat(key, default) as T
        is Set<*> -> getStringSet(key, default as Set<String>) as T
        is MutableSet<*> -> getStringSet(key, default as MutableSet<String>) as T
        else -> throw IllegalArgumentException("generic type not handle ${T::class.java.name}")
    }
}