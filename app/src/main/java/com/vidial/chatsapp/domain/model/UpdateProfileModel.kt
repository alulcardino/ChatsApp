package com.vidial.chatsapp.domain.model


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


