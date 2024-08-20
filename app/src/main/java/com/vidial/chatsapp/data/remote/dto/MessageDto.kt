package com.vidial.chatsapp.data.remote.dto

import com.vidial.chatsapp.domain.model.ChatInfoModel
import com.vidial.chatsapp.domain.model.MessageModel

data class MessageDto(
    val id: Int,
    val chatId: Int,
    val sender: String,
    val content: String,
    val timestamp: String
)

fun MessageDto.toMessageModel() : MessageModel {
    return MessageModel(
        id, chatId, sender, content, timestamp
    )
}


