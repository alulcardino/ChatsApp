package com.vidial.chatsapp.domain.model

import com.vidial.chatsapp.data.remote.dto.AvatarDto
import com.vidial.chatsapp.data.remote.dto.UpdateProfileDto

data class UpdateProfileModel(
    val name: String?,
    val username: String?,
    val birthday: String?,
    val city: String?,
    val avatar: AvatarModel?
)

data class AvatarModel(
    val filename: String?,
    val base64: String?
)

fun UpdateProfileModel.toUpdateProfileDto(): UpdateProfileDto {
    return UpdateProfileDto(
        name = this.name,
        username = this.username,
        birthday = this.birthday,
        city = this.city,
        vk = "",
        instagram = "",
        status = "",
        avatarDto = this.avatar?.toAvatarDto()
    )
}

fun AvatarModel.toAvatarDto(): AvatarDto {
    return AvatarDto(
        filename = this.filename,
        base64 = this.base64
    )
}
