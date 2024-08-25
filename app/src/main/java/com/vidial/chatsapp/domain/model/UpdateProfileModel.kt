package com.vidial.chatsapp.domain.model

import com.vidial.chatsapp.data.remote.dto.AvatarDto
import com.vidial.chatsapp.data.remote.dto.UpdateProfileDto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class UpdateProfileModel(
    val name: String?,
    val username: String?,
    val birthday: String?,
    val city: String?,
    val avatar: AvatarModel?,
    val status: String?,
)

data class AvatarModel(
    val filename: String?,
    val base64: String?
)


