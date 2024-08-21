package com.vidial.chatsapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.vidial.chatsapp.domain.model.UserProfileModel

data class GetUserProfileDto(
    @SerializedName("profile_data")
    val profileData: ProfileData?
)

data class ProfileData(
    @SerializedName("name")
    val name: String?,

    @SerializedName("username")
    val username: String?,

    @SerializedName("birthday")
    val birthday: String?,

    @SerializedName("city")
    val city: String?,

    @SerializedName("vk")
    val vk: String?,

    @SerializedName("instagram")
    val instagram: String?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("avatar")
    val avatar: String?,

    @SerializedName("id")
    val id: Int?,

    @SerializedName("last")
    val last: String?,

    @SerializedName("online")
    val online: Boolean?,

    @SerializedName("created")
    val created: String?,

    @SerializedName("phone")
    val phone: String?,

    @SerializedName("completed_task")
    val completedTask: Int?,

    @SerializedName("avatars")
    val avatars: Avatars?
)

data class Avatars(
    @SerializedName("avatar")
    val avatar: String?,

    @SerializedName("bigAvatar")
    val bigAvatar: String?,

    @SerializedName("miniAvatar")
    val miniAvatar: String?
)

fun GetUserProfileDto.toUserProfileModel(): UserProfileModel {

    val avatarUrl = if (this.profileData?.avatars?.avatar?.startsWith("http") == true) {
        this.profileData.avatars.avatar
    } else {
        val constructedUrl = this.profileData?.avatars?.avatar?.let { "https://plannerok.ru/$it" }
        constructedUrl ?: ""
    }

    val birthDate = this.profileData?.birthday ?: ""
    val username = this.profileData?.username ?: ""
    val city = this.profileData?.city ?: ""
    val phone = this.profileData?.phone ?: ""

    return UserProfileModel(
        birthDate = birthDate,
        username = username,
        city = city,
        avatarUrl = avatarUrl,
        phone = phone
    )
}
