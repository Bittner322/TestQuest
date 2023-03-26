package com.example.testquest.presentation.main_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.testquest.data.network.RetrofitProvider
import com.example.testquest.data.network.onGenericError
import com.example.testquest.data.network.onResultSuccess
import com.example.testquest.data.repositories.BaseApiRepository
import com.example.testquest.data.repositories.CustomAPIRepository
import com.example.testquest.data.repositories.DogsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val DOGS_API = "https://dog.ceo/api/"

class MainActivityViewModel @Inject constructor(
    private val dogsRepository: DogsRepository,
    baseApiRepository: BaseApiRepository,
    private val customAPIRepository: CustomAPIRepository,
    private val retrofitProvider: RetrofitProvider
): ViewModel() {

    private val _dogImage = MutableStateFlow("")
    val dogImage = _dogImage.asStateFlow()

    private val _customApiText = MutableStateFlow("")
    val customApiText = _customApiText.asStateFlow()

    private val _dogApiErrorFlow = MutableStateFlow("")
    val dogApiErrorFlow = _dogApiErrorFlow.asStateFlow()

    private val _customApiErrorFlow = MutableStateFlow("")
    val customApiErrorFlow = _customApiErrorFlow.asStateFlow()

    val sharedPreferencesFlow = baseApiRepository.getApiSharedPrefsFlow()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            DOGS_API
        )

    fun loadDog() {
        viewModelScope.launch {
            dogsRepository.getDog()
                .onResultSuccess { url ->
                    _dogImage.value = url
                }
                .onGenericError { code, _ ->
                    _dogApiErrorFlow.value = "Http error: $code"
                }
        }
    }

    fun loadCustomApiContent(url: String) {
        viewModelScope.launch {
            customAPIRepository.getCustomApiContent(url)
                .onResultSuccess { stringResponse ->
                    _customApiText.value = stringResponse
                }
                .onGenericError { code, _ ->
                    _customApiErrorFlow.value = "Http error: $code"
                }
        }
    }

    fun setBaseApiToCustom(customUrl: String) {
        viewModelScope.launch {
            retrofitProvider.updateBaseUrl(customUrl)
        }
    }

}

class MainActivityViewModelFactory @Inject constructor(
    private val dogsRepository: DogsRepository,
    private val baseApiRepository: BaseApiRepository,
    private val customAPIRepository: CustomAPIRepository,
    private val retrofitProvider: RetrofitProvider
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(
            dogsRepository,
            baseApiRepository,
            customAPIRepository,
            retrofitProvider
        ) as T
    }
}