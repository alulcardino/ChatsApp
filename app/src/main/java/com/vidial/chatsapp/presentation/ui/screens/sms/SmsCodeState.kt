package com.vidial.chatsapp.presentation.ui.screens.sms

sealed class SmsCodeState {
    object Initial : SmsCodeState()
    object Loading : SmsCodeState()
    object Authenticated : SmsCodeState()
    object Register : SmsCodeState()
    data class Error(val message: String) : SmsCodeState()
}
