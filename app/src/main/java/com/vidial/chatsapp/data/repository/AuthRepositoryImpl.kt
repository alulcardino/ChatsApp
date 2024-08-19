package com.vidial.chatsapp.data.repository

import com.vidial.chatsapp.data.remote.api.PlannerokApi
import com.vidial.chatsapp.data.remote.requests.CodeRequest
import com.vidial.chatsapp.data.remote.requests.PhoneRequest
import com.vidial.chatsapp.data.remote.response.AuthResponse
import com.vidial.chatsapp.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: PlannerokApi
) : AuthRepository {
    override suspend fun sendAuthCode(phone: String): Result<Unit> {
        return try {
            val response = api.sendAuthCode(PhoneRequest(phone))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to send auth code"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkAuthCode(phone: String, code: String): Result<AuthResponse> {
        return try {
            val response = api.checkAuthCode(CodeRequest(phone, code))
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to check auth code"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
