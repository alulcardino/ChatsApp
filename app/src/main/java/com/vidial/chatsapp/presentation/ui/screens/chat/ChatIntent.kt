package com.vidial.chatsapp.presentation.ui.screens.chat

sealed class ChatIntent {
    data class LoadChat(val chatId: Int) : ChatIntent()
    data class LoadMessages(val chatId: Int) : ChatIntent()
    data class SendMessage(val chatId: Int, val sender: String, val content: String) : ChatIntent()
}
