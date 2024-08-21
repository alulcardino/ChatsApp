package com.vidial.chatsapp.presentation.ui.screens.chatList

sealed class ChatListIntent {
    object LoadChats : ChatListIntent()
    data class OpenChat(val chatId: Int) : ChatListIntent()
}
