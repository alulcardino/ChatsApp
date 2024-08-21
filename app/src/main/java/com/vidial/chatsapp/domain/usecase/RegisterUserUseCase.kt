package com.vidial.chatsapp.domain.usecase

import com.vidial.chatsapp.data.remote.requests.RegisterRequest
import com.vidial.chatsapp.data.remote.response.AuthResponse
import com.vidial.chatsapp.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(phoneNumber: String, name: String, username: String): Result<AuthResponse> {
        return authRepository.registerUser(RegisterRequest(phoneNumber, name, username))
    }
}
