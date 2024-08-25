package com.vidial.chatsapp.presentation.ui.screens.registration

sealed class RegistrationState {
    object Initial : RegistrationState()
    object Loading : RegistrationState()
    data class Error(val message: String) : RegistrationState()
    data class Success(val message: String) : RegistrationState()
}
