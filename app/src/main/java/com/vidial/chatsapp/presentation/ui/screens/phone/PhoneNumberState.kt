package com.vidial.chatsapp.presentation.ui.screens.phone

sealed class PhoneNumberState {
    object Initial : PhoneNumberState()
    object Loading : PhoneNumberState()
    object Success : PhoneNumberState()
    data class Error(val message: String) : PhoneNumberState()
}
