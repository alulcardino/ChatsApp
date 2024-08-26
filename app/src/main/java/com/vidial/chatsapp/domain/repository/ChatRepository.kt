package com.vidial.chatsapp.domain.repository

import com.vidial.chatsapp.data.remote.dto.ChatDto
import com.vidial.chatsapp.data.remote.dto.MessageDto

interface ChatRepository {
    suspend fun getChats() : List<ChatDto>

    suspend fun getChatById(chatId: Int) : ChatDto

    suspend fun getMessagesForChat(chatId: Int): List<MessageDto>
}
