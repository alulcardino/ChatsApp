package com.vidial.chatsapp.presentation.ui.screens.profile

import android.net.Uri
import com.vidial.chatsapp.domain.model.UpdateProfileModel

sealed class UserProfileIntent {
    object FetchUserProfile : UserProfileIntent()
    object StartEditing : UserProfileIntent()
    object CancelEditing : UserProfileIntent()
    data class UpdateProfile(val profileModel: UpdateProfileModel) : UserProfileIntent()
    data class SetAvatarUri(val uri: Uri) : UserProfileIntent()
    object Logout : UserProfileIntent()
}
