package com.vidial.chatsapp.presentation.ui.screens.registration

sealed class RegistrationIntent {
    data class RegisterUser(val phoneNumber: String, val name: String, val username: String) :
        RegistrationIntent()
}
