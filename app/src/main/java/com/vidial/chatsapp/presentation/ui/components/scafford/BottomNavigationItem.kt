package com.vidial.chatsapp.presentation.ui.components.scafford

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.vidial.chatsapp.R
import com.vidial.chatsapp.presentation.ui.components.navigation.ScreenRoute

enum class BottomNavigationItem(
    @StringRes val titleRes: Int,
    val route: String,
    @DrawableRes val navigationIconRes: Int
) {
    CHATS(
        R.string.chats,
        ScreenRoute.ChatsGraph.route,
        R.drawable.ic_chat
    ),
    PROFILE(
        R.string.catalog,
        ScreenRoute.ProfileGraph.route,
        R.drawable.ic_user
    );

}
