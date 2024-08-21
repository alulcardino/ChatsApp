package com.vidial.chatsapp.data.repository

import android.util.Log
import com.vidial.chatsapp.data.remote.api.PlannerokApi
import com.vidial.chatsapp.data.remote.dto.Avatars
import com.vidial.chatsapp.data.remote.dto.GetUserProfileDto
import com.vidial.chatsapp.data.remote.dto.UpdateProfileDto
import com.vidial.chatsapp.data.remote.requests.AuthResult
import com.vidial.chatsapp.data.remote.requests.CodeRequest
import com.vidial.chatsapp.data.remote.requests.PhoneRequest
import com.vidial.chatsapp.data.remote.requests.RegisterRequest
import com.vidial.chatsapp.domain.provider.TokenProvider
import com.vidial.chatsapp.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
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
                    Log.d(
                        "AuthDebug",
                        "Request after token refresh failed with exception: ${e.message}"
                    )
                    return Result.failure(Exception("Request after token refresh failed"))
                }

                if (newResponse.isSuccessful) {
                    Result.success(newResponse.body()!!)
                } else {
                    Log.d(
                        "AuthDebug",
                        "Request after token refresh failed: ${newResponse.code()} - ${newResponse.message()}"
                    )
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

    override suspend fun registerUser(
        phone: String,
        name: String,
        username: String
    ): Result<AuthResult> {
        return executeWithTokenRefresh {
            Log.d(
                "AuthDebug",
                "Registering user with phone: $phone, name: $name, username: $username"
            )
            api.registerUser(
                RegisterRequest(
                    phone,
                    name,
                    username
                )
            ) // Возвращает Response<AuthResult>
        }.onSuccess {
            tokenProvider.saveTokens(it.accessToken, it.refreshToken)
        }
    }

    override suspend fun getUserProfile(): Result<GetUserProfileDto> {
        return executeWithTokenRefresh {
            Log.d("AuthDebug", "Fetching user profile from API")
            api.getUserProfile()
        }.mapCatching { response ->
            response.let {
                Log.d("AuthDebug", "Successfully fetched user profile: $response")
                it
            } ?: throw Exception("Profile data is null")
        }.onFailure { exception ->
            Log.e("AuthDebug", "Failed to fetch user profile: ${exception.message}")
        }
    }

    override suspend fun updateUserProfile(profileRequest: UpdateProfileDto): Result<Unit> {
        return executeWithTokenRefresh {
            api.updateProfile(profileRequest)
        }.map { }
    }

    override suspend fun logout() {
        withContext(Dispatchers.IO) {
            tokenProvider.clearTokens()
            tokenProvider.setUserLoggedIn(false)
            Log.d("AuthDebug", "User logged out successfully.")
        }
    }


}
