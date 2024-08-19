package com.vidial.chatsapp.presentation.ui.screens.phone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vidial.chatsapp.domain.usecase.SendAuthCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneNumberViewModel @Inject constructor(
    private val sendAuthCodeUseCase: SendAuthCodeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<PhoneNumberState>(PhoneNumberState.Initial)
    val state: StateFlow<PhoneNumberState> get() = _state

    fun sendPhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            _state.value = PhoneNumberState.Loading
            try {
                sendAuthCodeUseCase(phoneNumber)
                _state.value = PhoneNumberState.Success
            } catch (e: Exception) {
                _state.value = PhoneNumberState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class PhoneNumberState {
    object Initial : PhoneNumberState()
    object Loading : PhoneNumberState()
    object Success : PhoneNumberState()
    data class Error(val message: String) : PhoneNumberState()
}
