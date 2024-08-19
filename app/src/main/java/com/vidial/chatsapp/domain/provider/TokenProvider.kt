package com.vidial.chatsapp.domain.provider

import android.content.SharedPreferences
import com.vidial.chatsapp.data.remote.api.PlannerokApi
import com.vidial.chatsapp.data.remote.requests.RefreshTokenRequest
import kotlinx.coroutines.runBlocking

class TokenProvider(
    private val api: PlannerokApi,
    private val preferences: SharedPreferences
) {

    companion object {
        private const val TOKEN_EXPIRATION_TIME = 10 * 60 * 1000 // 10 минут в миллисекундах
    }

    private fun isTokenExpired(): Boolean {
        val tokenTimestamp = preferences.getLong("token_timestamp", 0L)
        val currentTime = System.currentTimeMillis()
        return currentTime - tokenTimestamp > TOKEN_EXPIRATION_TIME
    }

    fun getAccessToken(): String {
        if (isTokenExpired()) {
            // Попробовать обновить токен если он истек
            refreshAccessToken()
        }
        return preferences.getString("access_token", "") ?: ""
    }

    fun refreshAccessToken(): String? {
        val refreshToken = preferences.getString("refresh_token", "") ?: return null
        val response = runBlocking {
            api.refreshToken(RefreshTokenRequest(refreshToken))
        }
        return if (response.isSuccessful) {
            val newToken = response.body()?.accessToken
            preferences.edit()
                .putString("access_token", newToken)
                .putLong("token_timestamp", System.currentTimeMillis())
                .apply()
            newToken
        } else {
            null
        }
    }

    fun saveTokens(accessToken: String?, refreshToken: String?) {
        preferences.edit()
            .putString("access_token", accessToken)
            .putString("refresh_token", refreshToken)
            .apply()
    }
}
