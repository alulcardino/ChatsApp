package com.vidial.chatsapp.presentation.ui.screens.profile

import com.vidial.chatsapp.domain.model.UserProfileModel

sealed class UserProfileState {
    object Loading : UserProfileState()
    data class Success(val userProfile: UserProfileModel) : UserProfileState()
    data class Error(val message: String) : UserProfileState()
}
