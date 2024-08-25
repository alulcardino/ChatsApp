package com.vidial.chatsapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.vidial.chatsapp.domain.model.UserProfileModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    val status = profileData?.status.orEmpty()

    val formattedBirthDate = formatDateString(birthDate)

    val zodiacSign = calculateZodiacSign(formattedBirthDate)

    return UserProfileModel(
        birthDate = formattedBirthDate,
        username = username,
        city = city,
        avatarUrl = avatarUrl,
        phone = phone,
        zodiacSign = zodiacSign,
        status = status
    )
}
fun formatDateString(dateString: String): String {
    return try {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val targetFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        val date: Date? = originalFormat.parse(dateString)
        targetFormat.format(date ?: Date())
    } catch (e: Exception) {
        ""
    }
}

fun calculateZodiacSign(birthDate: String): String {
    if (birthDate.isBlank()) {
        return "Unknown"
    }

    return try {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val date = dateFormat.parse(birthDate) ?: return "Unknown"

        val calendar = java.util.Calendar.getInstance().apply { time = date }
        val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)
        val month = calendar.get(java.util.Calendar.MONTH) + 1

        when (month) {
            1 -> if (day < 20) "Capricorn" else "Aquarius"
            2 -> if (day < 19) "Aquarius" else "Pisces"
            3 -> if (day < 21) "Pisces" else "Aries"
            4 -> if (day < 20) "Aries" else "Taurus"
            5 -> if (day < 21) "Taurus" else "Gemini"
            6 -> if (day < 21) "Gemini" else "Cancer"
            7 -> if (day < 23) "Cancer" else "Leo"
            8 -> if (day < 23) "Leo" else "Virgo"
            9 -> if (day < 23) "Virgo" else "Libra"
            10 -> if (day < 23) "Libra" else "Scorpio"
            11 -> if (day < 22) "Scorpio" else "Sagittarius"
            12 -> if (day < 22) "Sagittarius" else "Capricorn"
            else -> "Unknown"
        }
    } catch (e: Exception) {
        "Unknown"
    }
}
