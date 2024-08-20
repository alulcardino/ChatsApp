package com.vidial.chatsapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @SerializedName("profile_data")
    val profileData: ProfileData?
)

data class ProfileData(
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

    @SerializedName("avatar")
    val avatar: String?, // Это строка, которая должна представлять URL

    @SerializedName("id")
    val id: Int?,  // Это целое число

    @SerializedName("last")
    val last: String?,  // Это строка, представляющая дату и время

    @SerializedName("online")
    val online: Boolean?,  // Это булево значение

    @SerializedName("created")
    val created: String?,  // Это строка, представляющая дату и время

    @SerializedName("phone")
    val phone: String?,  // Это строка

    @SerializedName("completed_task")
    val completedTask: Int?,  // Это целое число

    @SerializedName("avatars")
    val avatars: Avatars?  // Вложенный объект с аватарами
)

data class Avatars(
    @SerializedName("avatar")
    val avatar: String?,  // Это строка, представляющая URL

    @SerializedName("bigAvatar")
    val bigAvatar: String?,  // Это строка, представляющая URL

    @SerializedName("miniAvatar")
    val miniAvatar: String?  // Это строка, представляющая URL
)

data class UserProfile(
    val avatarUrl: String,     // URL аватарки пользователя
    val phoneNumber: String,   // Номер телефона
    val nickname: String,      // Никнейм
    val city: String,          // Город проживания
    val birthDate: String,     // Дата рождения (можно использовать формат ISO или другой)
    val zodiacSign: String,    // Знак зодиака
    val about: String,         // Информация о пользователе
    val vk: String?,           // Профиль VK (может быть null, если не указан)
    val instagram: String?,    // Профиль Instagram (может быть null, если не указан)
)
