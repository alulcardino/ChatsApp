package com.vidial.chatsapp.presentation.ui.screens.chatList

sealed class ChatListEffect {
    data class ShowError(val message: String) : ChatListEffect()
    data class NavigateToChat(val chatId: Int) : ChatListEffect()
    data object NavigateToProfile : ChatListEffect() // Добавлен новый эффект для перехода на профиль

}
