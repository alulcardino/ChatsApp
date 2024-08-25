package com.vidial.chatsapp.data.remote.dto

import com.vidial.chatsapp.domain.model.ChatInfoModel

data class ChatDto(
    val id: Int,
    val imageUrl: String,
    val name: String,
    val description: String
)


