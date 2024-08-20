package com.vidial.chatsapp.presentation.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vidial.chatsapp.data.remote.dto.ChatDto
import com.vidial.chatsapp.data.remote.dto.MessageDto
import com.vidial.chatsapp.domain.model.ChatInfoModel
import com.vidial.chatsapp.domain.model.MessageModel
import com.vidial.chatsapp.domain.repository.ChatRepository
import com.vidial.chatsapp.domain.usecase.chat.GetChatByIdUseCase
import com.vidial.chatsapp.domain.usecase.chat.GetMessagesFromChatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getMessagesFromChatUseCase: GetMessagesFromChatUseCase,
    private val getChatByIdUseCase: GetChatByIdUseCase
) : ViewModel() {

    private val _messages = MutableStateFlow<List<MessageModel>>(emptyList())
    val messages: StateFlow<List<MessageModel>> = _messages

    private val _chat = MutableStateFlow<ChatInfoModel?>(null)
    val chat: StateFlow<ChatInfoModel?> = _chat

    fun loadChat(chatId: Int) {
        viewModelScope.launch {
            _chat.value = getChatByIdUseCase(chatId)
        }
    }

    fun loadMessagesForChat(chatId: Int) {
        viewModelScope.launch {
            _messages.value = getMessagesFromChatUseCase(chatId)
        }
    }

    fun sendMessage(chatId: Int, sender: String, content: String) {
        val newMessage = MessageModel(
            id = _messages.value.size + 1,
            chatId = chatId,
            sender = sender,
            content = content,
            timestamp = getCurrentTimestamp()
        )
        _messages.value += newMessage
    }

    private fun getCurrentTimestamp(): String {
        val current = System.currentTimeMillis()
        val formatter = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
        return formatter.format(current)
    }
}
