package com.vidial.chatsapp.domain.provider

import android.content.SharedPreferences
import com.vidial.chatsapp.data.remote.api.PlannerokApi
import com.vidial.chatsapp.data.remote.requests.RefreshTokenRequest
import kotlinx.coroutines.runBlocking

class TokenProvider(
    private val api: PlannerokApi,
    private val preferences: SharedPreferences
) {

    fun getAccessToken(): String {
        return preferences.getString("access_token", "") ?: ""
    }

    fun refreshAccessToken(): String? {
        val refreshToken = preferences.getString("refresh_token", "") ?: return null
        val response = runBlocking {
            api.refreshToken(RefreshTokenRequest(refreshToken))
        }
        return if (response.isSuccessful) {
            val newToken = response.body()?.accessToken
            preferences.edit().putString("access_token", newToken).apply()
            newToken
        } else {
            null
        }
    }
}
