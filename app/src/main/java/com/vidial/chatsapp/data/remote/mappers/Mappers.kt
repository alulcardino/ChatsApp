package com.vidial.chatsapp.data.remote.mappers

import com.vidial.chatsapp.data.remote.dto.AvatarDto
import com.vidial.chatsapp.data.remote.dto.ChatDto
import com.vidial.chatsapp.data.remote.dto.CountryDto
import com.vidial.chatsapp.data.remote.dto.GetUserProfileDto
import com.vidial.chatsapp.data.remote.dto.MessageDto
import com.vidial.chatsapp.data.remote.dto.UpdateProfileDto
import com.vidial.chatsapp.domain.model.AvatarModel
import com.vidial.chatsapp.domain.model.ChatInfoModel
import com.vidial.chatsapp.domain.model.CountryModel
import com.vidial.chatsapp.domain.model.MessageModel
import com.vidial.chatsapp.domain.model.UpdateProfileModel
import com.vidial.chatsapp.domain.model.UserProfileModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun ChatDto.toChatInfoModel() : ChatInfoModel {
    return ChatInfoModel(
        id, imageUrl, name, description
    )
}

fun CountryDto.toCountryModel() : CountryModel {
    return CountryModel(
        name, code, mobileCode, flagResId
    )
}

fun GetUserProfileDto.toUserProfileModel(): UserProfileModel {
    val avatarUrl = profileData?.avatars?.avatar?.let {
        if (it.startsWith("http")) it else "https://plannerok.ru/$it"
    } ?: ""

    val birthDate = profileData?.birthday.orEmpty()
    val username = profileData?.username.orEmpty()
    val city = profileData?.city.orEmpty()
    val phone = profileData?.phone.orEmpty()
    val status = profileData?.status.orEmpty()

    val formattedBirthDate = formatDateStringFromDto(birthDate)

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
fun formatDateStringFromDto(dateString: String): String {
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
        ""
    }
}

fun MessageDto.toMessageModel() : MessageModel {
    return MessageModel(
        id, chatId, sender, content, timestamp
    )
}

fun UpdateProfileModel.toUpdateProfileDto(): UpdateProfileDto {
    return UpdateProfileDto(
        name = this.name,
        username = this.username,
        birthday = formatDateStringToDto(this.birthday ?: ""), // Преобразуем дату
        city = this.city,
        vk = "",
        instagram = "",
        status = this.status,
        avatarDto = this.avatar?.toAvatarDto()
    )
}

fun AvatarModel.toAvatarDto(): AvatarDto {
    return AvatarDto(
        filename = this.filename,
        base64 = this.base64
    )
}

fun formatDateStringToDto(dateString: String): String {
    return try {
        val originalFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val targetFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date: Date? = originalFormat.parse(dateString)
        targetFormat.format(date ?: Date())
    } catch (e: Exception) {
        ""
    }
}
