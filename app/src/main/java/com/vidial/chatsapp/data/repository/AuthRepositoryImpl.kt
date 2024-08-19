package com.vidial.chatsapp.data.repository

import com.vidial.chatsapp.data.remote.api.PlannerokApi
import com.vidial.chatsapp.data.remote.requests.AuthResult
import com.vidial.chatsapp.data.remote.requests.CodeRequest
import com.vidial.chatsapp.data.remote.requests.PhoneRequest
import com.vidial.chatsapp.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: PlannerokApi
) : AuthRepository {

    override suspend fun sendAuthCode(phone: String): Result<Unit> {
        return try {
            // Создаем запрос с номером телефона
            val response = api.sendAuthCode(PhoneRequest(phone))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error sending auth code"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkAuthCode(phone: String, code: String): Result<AuthResult> {
        return try {
            // Создаем запрос с номером телефона и кодом
            val response = api.checkAuthCode(CodeRequest(phone, code))
            if (response.isSuccessful) {
                response.body()?.let {
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
