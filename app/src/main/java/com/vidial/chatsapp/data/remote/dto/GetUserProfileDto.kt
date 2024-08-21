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

    val avatarUrl = profileData?.avatars?.avatar?.let {
        if (it.startsWith("http")) it else "https://plannerok.ru/$it"
    } ?: ""

    val birthDate = profileData?.birthday.orEmpty()
    val username = profileData?.username.orEmpty()
    val city = profileData?.city.orEmpty()
    val phone = profileData?.phone.orEmpty()

    return UserProfileModel(
        birthDate = birthDate,
        username = username,
        city = city,
        avatarUrl = avatarUrl,
        phone = phone
    )
}
