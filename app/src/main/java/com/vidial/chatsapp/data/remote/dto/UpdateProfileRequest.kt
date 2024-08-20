package com.vidial.chatsapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest(
    @SerializedName("name")
    val name: String?,

    @SerializedName("username")
    val username: String?,

    @SerializedName("birthday")
    val birthday: String?,  // Убедитесь, что дата корректно парсится как строка или LocalDate

    @SerializedName("city")
    val city: String?,

    @SerializedName("vk")
    val vk: String?,

    @SerializedName("instagram")
    val instagram: String?,

    @SerializedName("status")
    val status: String?,

// Вложенный объект с информацией об аватаре
)

data class Avatar(
    @SerializedName("filename")
    val filename: String?,  // Имя файла аватара

    @SerializedName("base_64")
    val base64: String?  // Кодировка изображения в base64
)
