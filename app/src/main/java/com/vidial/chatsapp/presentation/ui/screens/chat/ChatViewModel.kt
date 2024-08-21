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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getMessagesFromChatUseCase: GetMessagesFromChatUseCase,
    private val getChatByIdUseCase: GetChatByIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state

    private val _effect = MutableSharedFlow<ChatEffect>()
    val effect: SharedFlow<ChatEffect> = _effect

    fun handleIntent(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.LoadChat -> loadChat(intent.chatId)
            is ChatIntent.LoadMessages -> loadMessagesForChat(intent.chatId)
            is ChatIntent.SendMessage -> sendMessage(intent.chatId, intent.sender, intent.content)
        }
    }

    private fun loadChat(chatId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val chatInfo = getChatByIdUseCase(chatId)
                _state.value = _state.value.copy(chat = chatInfo, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, errorMessage = "Failed to load chat")
                _effect.emit(ChatEffect.ShowError("Failed to load chat"))
            }
        }
    }

    private fun loadMessagesForChat(chatId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val messages = getMessagesFromChatUseCase(chatId)
                _state.value = _state.value.copy(messages = messages, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, errorMessage = "Failed to load messages")
                _effect.emit(ChatEffect.ShowError("Failed to load messages"))
            }
        }
    }

    private fun sendMessage(chatId: Int, sender: String, content: String) {
        val newMessage = MessageModel(
            id = _state.value.messages.size + 1,
            chatId = chatId,
            sender = sender,
            content = content,
            timestamp = getCurrentTimestamp()
        )
        _state.value = _state.value.copy(messages = _state.value.messages + newMessage)
    }

    private fun getCurrentTimestamp(): String {
        val current = System.currentTimeMillis()
        val formatter = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
        return formatter.format(current)
    }
}
