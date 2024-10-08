package com.vidial.chatsapp.presentation.ui.components.navigation

sealed class ScreenRoute(val route: String) {
    object PhoneNumberScreen : ScreenRoute("phone_number")
    object SmsCodeScreen : ScreenRoute("sms_code/{phone}") {
        fun createRoute(phone: String) = "sms_code/$phone"
    }
    object RegistrationScreen : ScreenRoute("registration/{phone}") {
        fun createRoute(phone: String) = "registration/$phone"
    }
    object ChatListScreen : ScreenRoute("chat_list")
    object ChatScreen : ScreenRoute("chat/{chatId}") {
        fun createRoute(chatId: Int) = "chat/$chatId"
    }
    object ProfileScreen : ScreenRoute("profile")
    object EditProfileScreen : ScreenRoute("edit_profile")

    object LoginGraph : ScreenRoute("login_graph")
    object ChatsGraph : ScreenRoute("chats_graph")
    object ProfileGraph : ScreenRoute("profile_graph")
}
