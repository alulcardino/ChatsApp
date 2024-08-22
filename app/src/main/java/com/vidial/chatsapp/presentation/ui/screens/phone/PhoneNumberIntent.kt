package com.vidial.chatsapp.presentation.ui.screens.phone

sealed class PhoneNumberIntent {
    data class SelectCountry(val countryCode: String) : PhoneNumberIntent()
    data class SendPhoneNumber(val phoneNumber: String) : PhoneNumberIntent()
    data class UpdatePhoneNumber(val phoneNumber: String) : PhoneNumberIntent()

}
