package com.vidial.chatsapp.domain.model

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Locale

data class UserProfileModel(
    val birthDate: String,
    val phone: String,
    val zodiacSign: String = calculateZodiacSign(birthDate),
    val username: String,
    val city: String,
    val avatarUrl: String?
)

fun calculateZodiacSign(birthDate: String): String {
    if (birthDate.isBlank()) {
        return "Unknown"
    }

    return try {
        // Проверяем формат даты
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val date = dateFormat.parse(birthDate) ?: return "Unknown"

        // Разбираем день и месяц
        val calendar = java.util.Calendar.getInstance().apply { time = date }
        val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)
        val month = calendar.get(java.util.Calendar.MONTH) + 1  // Месяцы в Calendar начинаются с 0

        // Определяем знак зодиака по дню и месяцу
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
        Log.e("calculateZodiacSign", "Error parsing birth date: ${e.message}")
        "Unknown"
    }
}


