package com.vidial.chatsapp.presentation.ui.screens.chatList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vidial.chatsapp.data.remote.dto.ChatDto
import com.vidial.chatsapp.domain.model.ChatInfoModel
import com.vidial.chatsapp.domain.usecase.chat.GetChatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val getChatsUseCase: GetChatsUseCase
) : ViewModel() {

    private val _chats = MutableStateFlow<List<ChatInfoModel>>(emptyList())
    val chats: StateFlow<List<ChatInfoModel>> = _chats

    init {
        loadChats()
    }

    private fun loadChats() {
        viewModelScope.launch {
            val result = getChatsUseCase()
            _chats.value = result
        }
    }
}
