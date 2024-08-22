package com.vidial.chatsapp.presentation.ui.screens.sms

sealed class SmsCodeEffect {
    object NavigateToChatList : SmsCodeEffect()
    data class NavigateToRegistration(val phoneNumber: String) : SmsCodeEffect()
    data class ShowErrorToast(val message: String) : SmsCodeEffect()
}
