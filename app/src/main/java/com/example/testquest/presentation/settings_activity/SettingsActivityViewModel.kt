package com.example.testquest.presentation.settings_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.testquest.data.network.RetrofitProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val DOGS_API = "https://dog.ceo/api/"
private const val NATIONALITIES_API = "https://api.nationalize.io/"

class SettingsActivityViewModel @Inject constructor(
    private val retrofitProvider: RetrofitProvider
): ViewModel() {

    fun setBaseApiToDog() {
        viewModelScope.launch {
            retrofitProvider.updateBaseUrl(DOGS_API)
        }
    }

    fun setBaseApiToNationalize() {
        viewModelScope.launch {
            retrofitProvider.updateBaseUrl(NATIONALITIES_API)
        }
    }

}

class SettingsActivityViewModelFactory @Inject constructor(
    private val retrofitProvider: RetrofitProvider
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsActivityViewModel(retrofitProvider) as T
    }
}