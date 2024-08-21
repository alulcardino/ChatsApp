package com.vidial.chatsapp.presentation.ui.screens.chat

import com.vidial.chatsapp.domain.model.ChatInfoModel
import com.vidial.chatsapp.domain.model.MessageModel

data class ChatState(
    val chat: ChatInfoModel? = null,
    val messages: List<MessageModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
