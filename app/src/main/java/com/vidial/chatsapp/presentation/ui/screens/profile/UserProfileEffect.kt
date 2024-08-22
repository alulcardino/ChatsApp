package com.vidial.chatsapp.presentation.ui.screens.profile

sealed class UserProfileEffect {
    object NavigateToPhoneNumberScreen : UserProfileEffect()
    data class ShowErrorMessage(val message: String) : UserProfileEffect()
    object ProfileUpdated : UserProfileEffect()
}
