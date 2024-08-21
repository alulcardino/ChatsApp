package com.vidial.chatsapp.presentation.ui.screens.chat

sealed class ChatEffect {
    data class ShowError(val message: String) : ChatEffect()
    object NavigateBack : ChatEffect()
}
