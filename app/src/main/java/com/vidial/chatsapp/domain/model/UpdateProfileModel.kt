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
        birthday = formatDateString(this.birthday ?: ""), // Преобразуем дату
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

fun formatDateString(dateString: String): String {
    return try {
        // Определяем исходный формат даты
        val originalFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        // Определяем целевой формат даты
        val targetFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Преобразуем строку даты в объект Date
        val date: Date? = originalFormat.parse(dateString)
        // Форматируем объект Date в строку в требуемом формате
        targetFormat.format(date ?: Date()) // Используем текущую дату в случае ошибки
    } catch (e: Exception) {
        // Возвращаем пустую строку в случае ошибки
        ""
    }
}
