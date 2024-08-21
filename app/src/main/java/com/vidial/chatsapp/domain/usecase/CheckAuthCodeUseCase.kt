package com.vidial.chatsapp.domain.usecase

import com.vidial.chatsapp.data.remote.requests.CodeRequest
import com.vidial.chatsapp.data.remote.response.AuthResponse
import com.vidial.chatsapp.domain.repository.AuthRepository
import javax.inject.Inject

class CheckAuthCodeUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(phone: String, code: String): Result<AuthResponse> {
        return authRepository.checkAuthCode(CodeRequest(phone = phone, code = code))
    }
}
