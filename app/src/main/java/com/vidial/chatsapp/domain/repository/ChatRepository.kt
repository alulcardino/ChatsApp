package com.vidial.chatsapp.domain.repository

import com.vidial.chatsapp.data.remote.dto.ChatDto

interface ChatRepository {
    suspend fun getChats() : List<ChatDto>

    suspend fun getChatById(id: Int) : ChatDto
}

