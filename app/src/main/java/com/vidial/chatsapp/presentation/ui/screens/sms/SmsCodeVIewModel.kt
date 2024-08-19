package com.vidial.chatsapp.presentation.ui.screens.sms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vidial.chatsapp.domain.usecase.CheckAuthCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SmsCodeViewModel @Inject constructor(
    private val checkAuthCodeUseCase: CheckAuthCodeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SmsCodeState>(SmsCodeState.Initial)
    val state: StateFlow<SmsCodeState> get() = _state

    fun verifyCode(phoneNumber: String, code: String) {
        viewModelScope.launch {
            _state.value = SmsCodeState.Loading
            try {
                val result = checkAuthCodeUseCase(phoneNumber, code)
                if (result.isSuccess) {
                    val authResult = result.getOrNull()
                    if (authResult != null && authResult.isUserExists) {
                        _state.value = SmsCodeState.Authenticated
                    } else {
                        _state.value = SmsCodeState.Register
                    }
                } else {
                    _state.value = SmsCodeState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _state.value = SmsCodeState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class SmsCodeState {
    object Initial : SmsCodeState()
    object Loading : SmsCodeState()
    object Authenticated : SmsCodeState()
    object Register : SmsCodeState()
    data class Error(val message: String) : SmsCodeState()
}
