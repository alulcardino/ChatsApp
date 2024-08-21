package com.vidial.chatsapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.vidial.chatsapp.domain.model.AvatarModel
import com.vidial.chatsapp.domain.model.UpdateProfileModel


data class UpdateProfileDto(
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
    val avatarDto: AvatarDto?
)

data class AvatarDto(
    @SerializedName("filename")
    val filename: String?,

    @SerializedName("base_64")
    val base64: String?
)
