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
import java.time.LocalDate
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: PlannerokApi,
    private val tokenProvider: TokenProvider,
) : AuthRepository {

    private suspend fun <T> executeWithTokenRefresh(request: suspend () -> Result<T>): Result<T> {
        var response = request()
        if (response.isFailure && (response.exceptionOrNull() as? Exception)?.message?.contains("401") == true) {
            Log.d("AuthDebug", "401 Unauthorized response, attempting to refresh token.")
            val newToken = tokenProvider.refreshAccessToken()
            return if (newToken != null) {
                request()
            } else {
                Result.failure(Exception("Unable to refresh token"))
            }
        }
        return response
    }

    override suspend fun sendAuthCode(phone: String): Result<Unit> {
        return executeWithTokenRefresh {
            Log.d("AuthDebug", "Sending auth code for phone: $phone")
            try {
                val response = api.sendAuthCode(PhoneRequest(phone))
                if (response.isSuccessful) {
                    Log.d("AuthDebug", "Auth code sent successfully.")
                    Result.success(Unit)
                } else {
                    Log.d("AuthDebug", "Error sending auth code: ${response.message()}")
                    Result.failure(Exception("Error sending auth code"))
                }
            } catch (e: Exception) {
                Log.d("AuthDebug", "Exception in sendAuthCode: ${e.message}")
                Result.failure(e)
            }
        }
    }

    override suspend fun checkAuthCode(phone: String, code: String): Result<AuthResult> {
        return executeWithTokenRefresh {
            try {
                val response = api.checkAuthCode(CodeRequest(phone, code))
                if (response.isSuccessful) {
                    response.body()?.let {
                        tokenProvider.saveTokens(it.accessToken, it.refreshToken)
                        tokenProvider.setUserLoggedIn(true)
                        Result.success(it)
                    } ?: Result.failure(Exception("Empty response"))
                } else {
                    Result.failure(Exception("Error checking auth code"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun registerUser(phone: String, name: String, username: String): Result<AuthResult> {
        return executeWithTokenRefresh {
            withContext(Dispatchers.IO) {
                try {
                    val response = api.registerUser(RegisterRequest(phone, name, username))
                    if (response.isSuccessful) {
                        response.body()?.let {
                            tokenProvider.saveTokens(it.accessToken, it.refreshToken)
                            Result.success(it)
                        } ?: Result.failure(Exception("Empty response"))
                    } else {
                        Result.failure(Exception("Error registering user"))
                    }
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getUserProfile(): Result<UserProfile> {
        return executeWithTokenRefresh {
            Log.d("AuthDebug", "Fetching user profile")
            try {
                val response = api.getUserProfile()

                val profileResponse = response.body()
                Log.d("AuthDebug", "Full Response Body: $profileResponse")

                if (response.isSuccessful && profileResponse != null) {
                    val profileData = profileResponse.profileData

                    if (profileData != null) {
                        val avatarUrl = profileData.avatars?.avatar ?: profileData.avatar ?: ""

                        val userProfile = UserProfile(
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
                        Log.d("AuthDebug", "User profile fetched successfully.")
                        Result.success(userProfile)
                    } else {
                        Log.d("AuthDebug", "Profile data is null.")
                        Result.failure(Exception("Profile data is null"))
                    }
                } else {
                    Log.d("AuthDebug", "Error fetching user profile: ${response.message()}")
                    Result.failure(Exception("Error fetching user profile"))
                }
            } catch (e: Exception) {
                Log.d("AuthDebug", "Exception in getUserProfile: ${e.message}")
                Result.failure(e)
            }
        }
    }

    override suspend fun updateUserProfile(profileRequest: UpdateProfileRequest): Result<Unit> {
        return executeWithTokenRefresh {
            Log.d("AuthDebug", "Updating user profile with name: ${profileRequest.name}, username: ${profileRequest.username}")
            try {
                val response = api.updateProfile(profileRequest)
                if (response.isSuccessful) {
                    Log.d("AuthDebug", "Profile updated successfully.")
                    Result.success(Unit)
                } else {
                    Log.d("AuthDebug", "Error updating profile: ${response.message()}")
                    Result.failure(Exception("Error updating profile"))
                }
            } catch (e: Exception) {
                Log.d("AuthDebug", "Exception in updateProfile: ${e.message}")
                Result.failure(e)
            }
        }
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
        // Например, вы можете использовать LocalDate для парсинга даты и определения знака
        return "Zodiac Sign" // Placeholder
    }
}
