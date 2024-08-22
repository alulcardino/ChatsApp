package com.vidial.chatsapp.presentation.ui.screens.sms

sealed class SmsCodeIntent {
    data class VerifyCode(val phoneNumber: String, val code: String) : SmsCodeIntent()
}
