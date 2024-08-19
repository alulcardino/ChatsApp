package com.vidial.chatsapp.domain.usecase.chat

import com.vidial.chatsapp.data.remote.dto.toChatInfoModel
import com.vidial.chatsapp.data.remote.requests.AuthResult
import com.vidial.chatsapp.domain.model.ChatInfoModel
import com.vidial.chatsapp.domain.repository.AuthRepository
import com.vidial.chatsapp.domain.repository.ChatRepository
import javax.inject.Inject

class GetChatsUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke() : List<ChatInfoModel> {
        return chatRepository.getChats().map { it.toChatInfoModel() }
    }
}

