package com.vidial.chatsapp.domain.usecase.chat

import com.vidial.chatsapp.data.remote.dto.toChatInfoModel
import com.vidial.chatsapp.domain.model.ChatInfoModel
import com.vidial.chatsapp.domain.repository.ChatRepository
import javax.inject.Inject

class GetChatByIdUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chatId: Int) : ChatInfoModel {
        return chatRepository.getChatById(chatId).toChatInfoModel()
    }
}

