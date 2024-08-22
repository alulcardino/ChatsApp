package com.vidial.chatsapp.presentation.ui.screens.registration

sealed class RegistrationEffect {
    object NavigateToChatList : RegistrationEffect()
    data class ShowError(val message: String) : RegistrationEffect()
}
