package com.vidial.chatsapp.domain.usecase

import com.vidial.chatsapp.data.remote.requests.RegisterRequest
import com.vidial.chatsapp.data.remote.response.AuthResponse
import com.vidial.chatsapp.data.repository.AuthException
import com.vidial.chatsapp.data.repository.mapFailure
import com.vidial.chatsapp.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(phoneNumber: String, name: String, username: String): Result<AuthResponse> {
        return authRepository.registerUser(RegisterRequest(phoneNumber, name, username))
            .mapFailure { exception ->
                when (exception) {
                    is AuthException.Unauthorized -> Exception("Authentication failed")
                    is AuthException.NotFound -> Exception("Registration endpoint not found")
                    is AuthException.NetworkError -> Exception("Network error occurred")
                    is AuthException.TokenRefreshFailed -> Exception("Token refresh failed")
                    else -> Exception("Unknown error occurred")
                }
            }
    }
}
