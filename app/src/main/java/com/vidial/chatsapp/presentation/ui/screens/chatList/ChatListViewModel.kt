package com.vidial.chatsapp.presentation.ui.screens.chatList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vidial.chatsapp.data.remote.dto.ChatDto
import com.vidial.chatsapp.domain.model.ChatInfoModel
import com.vidial.chatsapp.domain.usecase.chat.GetChatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val getChatsUseCase: GetChatsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ChatListState())
    val state: StateFlow<ChatListState> = _state

    private val _effect = MutableSharedFlow<ChatListEffect>()
    val effect: SharedFlow<ChatListEffect> = _effect

    init {
        handleIntent(ChatListIntent.LoadChats)
    }

    fun handleIntent(intent: ChatListIntent) {
        when (intent) {
            is ChatListIntent.LoadChats -> loadChats()
            is ChatListIntent.OpenChat -> openChat(intent.chatId)
        }
    }

    private fun loadChats() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val chats = getChatsUseCase()
                _state.value = _state.value.copy(chats = chats, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, errorMessage = "Ошибка загрузки чатов")
                _effect.emit(ChatListEffect.ShowError("Ошибка загрузки чатов"))
            }
        }
    }

    private fun openChat(chatId: Int) {
        viewModelScope.launch {
            _effect.emit(ChatListEffect.NavigateToChat(chatId))
        }
    }
}
