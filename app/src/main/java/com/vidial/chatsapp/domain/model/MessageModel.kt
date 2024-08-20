package com.vidial.chatsapp.domain.model

data class MessageModel (
    val id: Int,
    val chatId: Int,
    val sender: String,
    val content: String,
    val timestamp: String
)
