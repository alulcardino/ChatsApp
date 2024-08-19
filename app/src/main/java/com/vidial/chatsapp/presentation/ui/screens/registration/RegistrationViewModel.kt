package com.vidial.chatsapp.presentation.ui.screens.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vidial.chatsapp.domain.usecase.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<RegistrationState>(RegistrationState.Initial)
    val state: StateFlow<RegistrationState> get() = _state

    fun registerUser(phoneNumber: String, name: String, username: String) {
        viewModelScope.launch {
            _state.value = RegistrationState.Loading
            try {
                val result = registerUserUseCase(phoneNumber, name, username)
                if (result.isSuccess) {
                    _state.value = RegistrationState.Success
                } else {
                    _state.value = RegistrationState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _state.value = RegistrationState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class RegistrationState {
    object Initial : RegistrationState()
    object Loading : RegistrationState()
    object Success : RegistrationState()
    data class Error(val message: String) : RegistrationState()
}
