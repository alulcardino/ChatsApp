package com.vidial.chatsapp.presentation.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vidial.chatsapp.domain.usecase.CheckAuthCodeUseCase
import com.vidial.chatsapp.domain.usecase.SendAuthCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sendAuthCodeUseCase: SendAuthCodeUseCase,
    private val checkAuthCodeUseCase: CheckAuthCodeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    fun sendPhoneNumber(phone: String) {
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            val result = sendAuthCodeUseCase(phone)
            result.onSuccess {
                _uiState.value = AuthUiState.CodeSent
            }.onFailure {
                _uiState.value = AuthUiState.Error(it.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun verifyCode(phone: String, code: String) {
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            val result = checkAuthCodeUseCase(phone, code)
            result.onSuccess { authResponse ->
                if (authResponse.isUserExists) {
                    _uiState.value = AuthUiState.Authorized
                    saveTokens(authResponse.refreshToken, authResponse.accessToken)
                } else {
                    _uiState.value = AuthUiState.NeedsRegistration
                }
            }.onFailure {
                _uiState.value = AuthUiState.Error(it.localizedMessage ?: "Invalid SMS code")
            }
        }
    }

    private fun saveTokens(refreshToken: String, accessToken: String) {
        // Save tokens to secure storage
    }
}

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    object CodeSent : AuthUiState()
    object Authorized : AuthUiState()
    object NeedsRegistration : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}
