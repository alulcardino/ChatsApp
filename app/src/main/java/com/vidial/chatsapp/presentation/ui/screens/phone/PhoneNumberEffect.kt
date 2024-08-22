package com.vidial.chatsapp.presentation.ui.screens.phone

sealed class PhoneNumberEffect {
    data class ShowError(val message: String) : PhoneNumberEffect()
    object NavigateToNextScreen : PhoneNumberEffect()
}
