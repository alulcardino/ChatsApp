package com.vidial.chatsapp.presentation.ui.screens.registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    private val _state = MutableStateFlow<RegistrationState>(RegistrationState.Initial)
    val state: StateFlow<RegistrationState> get() = _state

    private val _effect = MutableSharedFlow<RegistrationEffect>()
    val effect: SharedFlow<RegistrationEffect> get() = _effect

    private val nameRegex = "^[A-Za-z ]*$".toRegex()

    private val usernameRegex = "^[A-Za-z0-9-_]*$".toRegex()

    var name by mutableStateOf("")
    var username by mutableStateOf("")

    fun handleIntent(intent: RegistrationIntent) {
        when (intent) {
            is RegistrationIntent.RegisterUser -> {
                if (isUsernameValid(intent.username) && isNameValid(intent.name)) {
                    registerUser(intent.phoneNumber, intent.name, intent.username)
                } else {
                    viewModelScope.launch {
                        _effect.emit(RegistrationEffect.ShowError("Invalid username format"))
                    }
                }
            }
        }
    }

    private fun isUsernameValid(username: String): Boolean {
        return username.matches(usernameRegex)
    }

    private fun isNameValid(name: String): Boolean {
        return name.matches(nameRegex)
    }

    private fun registerUser(phoneNumber: String, name: String, username: String) {
        viewModelScope.launch {
            _state.value = RegistrationState.Loading
            try {
                val result = registerUserUseCase(phoneNumber, name, username)
                if (result.isSuccess) {
                    _effect.emit(RegistrationEffect.NavigateToChatList)
                } else {
                    _effect.emit(
                        RegistrationEffect.ShowError(
                            result.exceptionOrNull()?.message ?: "Неизвестная ошибка"
                        )
                    )
                }
            } catch (e: Exception) {
                _effect.emit(RegistrationEffect.ShowError(e.message ?: "Неизвестная ошибка"))
            } finally {
                _state.value = RegistrationState.Initial
            }
        }
    }

    fun onNameChange(newName: String) {
        name = if (isNameValid(newName)) newName else name
    }

    fun onUsernameChange(newUsername: String) {
        username = if (isUsernameValid(newUsername)) newUsername else username
    }
}
