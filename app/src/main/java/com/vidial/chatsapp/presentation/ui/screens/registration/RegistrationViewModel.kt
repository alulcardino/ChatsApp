package com.vidial.chatsapp.presentation.ui.screens.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vidial.chatsapp.domain.usecase.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegistrationViewState())
    val state: StateFlow<RegistrationViewState> get() = _state

    private val _effect = MutableSharedFlow<RegistrationEffect>()
    val effect: SharedFlow<RegistrationEffect> get() = _effect

    private val usernameRegex = "^[A-Za-z0-9-_]*$".toRegex()

    fun handleIntent(intent: RegistrationIntent) {
        when (intent) {
            is RegistrationIntent.RegisterUser -> {
                if (isUsernameValid(intent.username)) {
                    registerUser(intent.phoneNumber, intent.name, intent.username)
                } else {
                    viewModelScope.launch {
                        _effect.emit(RegistrationEffect.ShowError("Invalid username format"))
                    }
                }
            }
        }
    }

    // Валидация username
    private fun isUsernameValid(username: String): Boolean {
        return username.matches(usernameRegex)
    }

    // Регистрация пользователя
    private fun registerUser(phoneNumber: String, name: String, username: String) {
        viewModelScope.launch {
            _state.value = RegistrationViewState(isLoading = true)
            try {
                val result = registerUserUseCase(phoneNumber, name, username)
                if (result.isSuccess) {
                    _effect.emit(RegistrationEffect.NavigateToChatList)
                } else {
                    _effect.emit(RegistrationEffect.ShowError(result.exceptionOrNull()?.message ?: "Неизвестная ошибка"))
                }
            } catch (e: Exception) {
                _effect.emit(RegistrationEffect.ShowError(e.message ?: "Неизвестная ошибка"))
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }
}
