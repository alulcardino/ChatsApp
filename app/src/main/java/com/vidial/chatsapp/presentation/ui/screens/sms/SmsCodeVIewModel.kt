package com.vidial.chatsapp.presentation.ui.screens.sms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vidial.chatsapp.domain.repository.AuthRepository
import com.vidial.chatsapp.domain.usecase.CheckAuthCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SmsCodeViewModel @Inject constructor(
    private val checkAuthCodeUseCase: CheckAuthCodeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SmsCodeState>(SmsCodeState.Initial)
    val state: StateFlow<SmsCodeState> get() = _state

    private val _effect = MutableSharedFlow<SmsCodeEffect>()
    val effect: SharedFlow<SmsCodeEffect> get() = _effect

    private val _otpValues = MutableStateFlow(List(6) { "" })
    val otpValues: StateFlow<List<String>> get() = _otpValues

    fun onOtpValueChange(index: Int, newValue: String) {
        if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
            _otpValues.value = _otpValues.value.toMutableList().apply {
                this[index] = newValue
            }
        }
    }

    fun onFocusChange(currentIndex: Int, isNext: Boolean) {
        if (isNext) {
            _otpValues.value.getOrNull(currentIndex + 1)?.let { _otpValues.value[currentIndex + 1] }
        } else {
            _otpValues.value.getOrNull(currentIndex - 1)?.let { _otpValues.value[currentIndex - 1] }
        }
    }

    fun verifyCode(phoneNumber: String) {
        val code = _otpValues.value.joinToString("")
        viewModelScope.launch {
            _state.value = SmsCodeState.Loading
            try {
                val result = checkAuthCodeUseCase(phoneNumber, code)
                if (result.isSuccess) {
                    val authResult = result.getOrNull()
                    if (authResult?.isUserExists == true) {
                        _state.value = SmsCodeState.Authenticated
                        _effect.emit(SmsCodeEffect.NavigateToChatList)
                    } else {
                        _state.value = SmsCodeState.Register
                        _effect.emit(SmsCodeEffect.NavigateToRegistration(phoneNumber))
                    }
                } else {
                    _state.value = SmsCodeState.Initial
                    _effect.emit(SmsCodeEffect.ShowErrorToast(result.exceptionOrNull()?.message ?: "Invalid verification code."))
                }
            } catch (e: Exception) {
                _state.value = SmsCodeState.Initial
                _effect.emit(SmsCodeEffect.ShowErrorToast(e.message ?: "Unknown error"))
            }
        }
    }
}
