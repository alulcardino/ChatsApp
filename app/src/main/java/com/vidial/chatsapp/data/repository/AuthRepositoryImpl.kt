package com.vidial.chatsapp.data.repository

import com.vidial.chatsapp.data.remote.api.PlannerokApi
import com.vidial.chatsapp.data.remote.dto.GetUserProfileDto
import com.vidial.chatsapp.data.remote.dto.UpdateProfileDto
import com.vidial.chatsapp.data.remote.response.AuthResponse
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
        val response = try {
            request()
        } catch (e: Exception) {
            return Result.failure(e)
        }

        return when {
            response.isSuccessful -> {
                Result.success(response.body()!!)
            }
            response.code() == 401 -> {
                val newToken = tokenProvider.refreshAccessToken()
                if (newToken != null) {
                    val newResponse = try {
                        request()
                    } catch (e: Exception) {
                        return Result.failure(Exception("Request after token refresh failed"))
                    }
                    if (newResponse.isSuccessful) {
                        Result.success(newResponse.body()!!)
                    } else {
                        Result.failure(Exception("Request after token refresh failed with status: ${newResponse.code()}"))
                    }
                } else {
                    Result.failure(Exception("Unable to refresh token"))
                }
            }
            response.code() == 404 -> {
                Result.failure(Exception("Request failed with status 404: Not Found"))
            }
            else -> {
                Result.failure(Exception("Request failed with status: ${response.code()} - ${response.message()}"))
            }
        }
    }

    override suspend fun sendAuthCode(phoneRequest: PhoneRequest): Result<Unit> {
        return executeWithTokenRefresh {
            api.sendAuthCode(phoneRequest)
        }.map { Unit }
    }

    override suspend fun checkAuthCode(codeRequest: CodeRequest): Result<AuthResponse> {
        return executeWithTokenRefresh {
            api.checkAuthCode(codeRequest)
        }.onSuccess {
            tokenProvider.saveTokens(it.accessToken, it.refreshToken)
            tokenProvider.setUserLoggedIn(true)
        }
    }

    override suspend fun registerUser(registerRequest: RegisterRequest): Result<AuthResponse> {
        return executeWithTokenRefresh {
            api.registerUser(registerRequest)
        }.onSuccess {
            tokenProvider.saveTokens(it.accessToken, it.refreshToken)
        }
    }

    override suspend fun getUserProfile(): Result<GetUserProfileDto> {
        return executeWithTokenRefresh {
            api.getUserProfile()
        }.mapCatching { response ->
            response.let {
                it
            }
        }.onFailure {
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
        }
    }
}
