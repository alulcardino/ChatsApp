package com.vidial.chatsapp.data.remote.dto


data class MessageDto(
    val id: Int,
    val chatId: Int,
    val sender: String,
    val content: String,
    val timestamp: String
)




