package com.vidial.chatsapp.data.repository

import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.vidial.chatsapp.data.remote.api.PlannerokApi
import com.vidial.chatsapp.data.remote.dto.UpdateProfileRequest
import com.vidial.chatsapp.data.remote.dto.UserProfile
import com.vidial.chatsapp.data.remote.dto.UserProfileResponse
import com.vidial.chatsapp.data.remote.requests.AuthResult
import com.vidial.chatsapp.data.remote.requests.CodeRequest
import com.vidial.chatsapp.data.remote.requests.PhoneRequest
import com.vidial.chatsapp.data.remote.requests.RegisterRequest
import com.vidial.chatsapp.domain.provider.TokenProvider
import com.vidial.chatsapp.domain.provider.TokenProvider.TokenConstants.ACCESS_TOKEN_KEY
import com.vidial.chatsapp.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.time.LocalDate
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: PlannerokApi,
    private val tokenProvider: TokenProvider,
) : AuthRepository {

    private suspend fun <T> executeWithTokenRefresh(request: suspend () -> Response<T>): Result<T> {
        Log.d("AuthDebug", "Executing request with possible token refresh.")

        val response = try {
            request()
        } catch (e: Exception) {
            Log.d("AuthDebug", "Request failed with exception: ${e.message}")
            return Result.failure(e)
        }

        if (response.code() == 401) {
            Log.d("AuthDebug", "401 Unauthorized detected, attempting to refresh token.")

            // Попытка обновления токена
            val newToken = tokenProvider.refreshAccessToken()

            return if (newToken != null) {
                Log.d("AuthDebug", "Token refreshed successfully: $newToken. Retrying the request.")
                val newResponse = try {
                    request() // Повторяем запрос с новым токеном
                } catch (e: Exception) {
                    Log.d("AuthDebug", "Request after token refresh failed with exception: ${e.message}")
                    return Result.failure(Exception("Request after token refresh failed"))
                }

                if (newResponse.isSuccessful) {
                    Result.success(newResponse.body()!!)
                } else {
                    Log.d("AuthDebug", "Request after token refresh failed: ${newResponse.code()} - ${newResponse.message()}")
                    Result.failure(Exception("Request after token refresh failed with status: ${newResponse.code()}"))
                }
            } else {
                Log.d("AuthDebug", "Failed to refresh token, cannot retry request.")
                Result.failure(Exception("Unable to refresh token"))
            }
        }

        return if (response.isSuccessful) {
            Log.d("AuthDebug", "Request succeeded.")
            Result.success(response.body()!!)
        } else {
            Log.d("AuthDebug", "Request failed: ${response.code()} - ${response.message()}")
            Result.failure(Exception("Request failed with status: ${response.code()}"))
        }
    }

    override suspend fun sendAuthCode(phone: String): Result<Unit> {
        return executeWithTokenRefresh {
            Log.d("AuthDebug", "Sending auth code for phone: $phone")
            api.sendAuthCode(PhoneRequest(phone)) // Возвращает Response<Unit>
        }.map { Unit }
    }

    override suspend fun checkAuthCode(phone: String, code: String): Result<AuthResult> {
        return executeWithTokenRefresh {
            Log.d("AuthDebug", "Checking auth code for phone: $phone with code: $code")
            api.checkAuthCode(CodeRequest(phone, code)) // Возвращает Response<AuthResult>
        }.onSuccess {
            tokenProvider.saveTokens(it.accessToken, it.refreshToken)
            tokenProvider.setUserLoggedIn(true)
        }
    }

    override suspend fun registerUser(phone: String, name: String, username: String): Result<AuthResult> {
        return executeWithTokenRefresh {
            Log.d("AuthDebug", "Registering user with phone: $phone, name: $name, username: $username")
            api.registerUser(RegisterRequest(phone, name, username)) // Возвращает Response<AuthResult>
        }.onSuccess {
            tokenProvider.saveTokens(it.accessToken, it.refreshToken)
        }
    }

    override suspend fun getUserProfile(): Result<UserProfile> {
        return executeWithTokenRefresh {
            Log.d("AuthDebug", "Fetching user profile")
            api.getUserProfile() // Возвращает Response<UserProfileResponse>
        }.mapCatching { profileResponse ->
            val profileData = profileResponse.profileData ?: throw Exception("Profile data is null")

            val avatarUrl = if (profileData.avatars?.avatar?.startsWith("http") == true) {
                profileData.avatars.avatar
            } else {
                "https://plannerok.ru/${profileData.avatars?.avatar}"
            }

            UserProfile(
                avatarUrl = avatarUrl,
                phoneNumber = profileData.phone ?: "",
                nickname = profileData.username ?: "",
                city = profileData.city ?: "",
                birthDate = profileData.birthday ?: "",
                zodiacSign = calculateZodiacSign(profileData.birthday ?: ""),
                about = profileData.status ?: "",
                vk = profileData.vk ?: "",
                instagram = profileData.instagram ?: "",
            )
        }
    }

    override suspend fun updateUserProfile(profileRequest: UpdateProfileRequest): Result<Unit> {
        return executeWithTokenRefresh {
            Log.d("AuthDebug", "Attempting to update user profile with data: $profileRequest")
            api.updateProfile(profileRequest) // Возвращает Response<Unit>
        }.map { Unit }
    }

    override suspend fun logout() {
        withContext(Dispatchers.IO) {
            tokenProvider.clearTokens()
            tokenProvider.setUserLoggedIn(false)
            Log.d("AuthDebug", "User logged out successfully.")
        }
    }

    private fun calculateZodiacSign(birthday: String): String {
        // Логика для вычисления знака зодиака по дате рождения
        return "Zodiac Sign" // Placeholder
    }
}
