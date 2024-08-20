package com.vidial.chatsapp.domain.usecase.chat

import com.vidial.chatsapp.data.remote.dto.toMessageModel
import com.vidial.chatsapp.domain.model.MessageModel
import com.vidial.chatsapp.domain.repository.ChatRepository
import javax.inject.Inject

class GetMessagesFromChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chatId: Int) : List<MessageModel> {
        return chatRepository.getMessagesForChat(chatId).map { it.toMessageModel() }
    }
}
