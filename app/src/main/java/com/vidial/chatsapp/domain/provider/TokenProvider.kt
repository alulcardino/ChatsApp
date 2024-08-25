package com.vidial.chatsapp.domain.provider

import android.content.SharedPreferences
import android.util.Log
import com.vidial.chatsapp.data.remote.api.PlannerokApi
import com.vidial.chatsapp.data.remote.requests.RefreshTokenRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokenProvider @Inject constructor(
    private val api: PlannerokApi,
    private val preferences: SharedPreferences
) {

    suspend fun refreshAccessToken(): String? {
        val refreshToken = preferences.getString(TokenConstants.REFRESH_TOKEN_KEY, "")

        if (refreshToken.isNullOrEmpty()) {
            return null
        }

        return try {
            val response = api.refreshToken(RefreshTokenRequest(refreshToken))
            if (response.isSuccessful) {
                val newToken = response.body()?.accessToken
                val newRefreshToken = response.body()?.refreshToken

                if (newToken != null && newRefreshToken != null) {
                    saveTokens(newToken, newRefreshToken)
                    newToken
                } else {
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }


    fun saveTokens(accessToken: String?, refreshToken: String?) {
        preferences.edit()
            .putString(TokenConstants.ACCESS_TOKEN_KEY, accessToken)
            .putString(TokenConstants.REFRESH_TOKEN_KEY, refreshToken)
            .putLong(TokenConstants.TOKEN_TIMESTAMP_KEY, System.currentTimeMillis())
            .apply()
    }

    fun setUserLoggedIn(isLoggedIn: Boolean) {
        preferences.edit()
            .putBoolean(TokenConstants.IS_LOGGED_IN_KEY, isLoggedIn)
            .apply()
    }

    fun isUserLoggedIn(): Boolean {
        return preferences.getBoolean(TokenConstants.IS_LOGGED_IN_KEY, false)
    }

    fun clearTokens() {
        preferences.edit()
            .remove(TokenConstants.ACCESS_TOKEN_KEY)
            .remove(TokenConstants.REFRESH_TOKEN_KEY)
            .remove(TokenConstants.TOKEN_TIMESTAMP_KEY)
            .apply()
    }

    object TokenConstants {
        const val ACCESS_TOKEN_KEY = "access_token"
        const val REFRESH_TOKEN_KEY = "refresh_token"
        const val TOKEN_TIMESTAMP_KEY = "token_timestamp"
        const val IS_LOGGED_IN_KEY = "is_logged_in"
    }
}
