package com.vidial.chatsapp.presentation.ui.screens.chatList

import com.vidial.chatsapp.domain.model.ChatInfoModel

data class ChatListState(
    val chats: List<ChatInfoModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
