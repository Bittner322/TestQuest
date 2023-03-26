package com.example.testquest.presentation.dialogs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.testquest.data.network.models.NationalityResponse
import com.example.testquest.data.network.onGenericError
import com.example.testquest.data.network.onResultSuccess
import com.example.testquest.data.repositories.NationalityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class NationalityDialogViewModel @Inject constructor(
    private val nationalityRepository: NationalityRepository
): ViewModel() {

    private val _nationality = MutableStateFlow<List<NationalityResponse>>(emptyList())
    val nationality = _nationality.asStateFlow()

    private val _errorFlow = MutableStateFlow("")
    val errorFlow = _errorFlow.asStateFlow()

    fun loadNationalities(nationality: List<String>) {
        viewModelScope.launch {
            nationalityRepository.getNationalityCountry(nationality)
                .onGenericError { code, _ ->
                    _errorFlow.value = "Http error: $code"
                }
                .onResultSuccess {
                    _nationality.value = it
                }

        }
    }
}

class NationalityDialogViewModelFactory @Inject constructor(
    private val nationalityRepository: NationalityRepository
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NationalityDialogViewModel(nationalityRepository) as T
    }
}