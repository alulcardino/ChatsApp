package com.vidial.chatsapp.domain.usecase

import com.vidial.chatsapp.data.remote.requests.PhoneRequest
import com.vidial.chatsapp.data.repository.AuthException
import com.vidial.chatsapp.data.repository.mapFailure
import com.vidial.chatsapp.domain.repository.AuthRepository
import javax.inject.Inject

class SendAuthCodeUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(phone: String): Result<Unit> {
        return repository.sendAuthCode(PhoneRequest(phone)).mapFailure { exception ->
            when (exception) {
                is AuthException.Unauthorized -> AuthException.Unauthorized("Invalid verification code, please try again")
                is AuthException.NotFound -> AuthException.NotFound()
                is AuthException.NetworkError -> AuthException.NetworkError()
                is AuthException.TokenRefreshFailed -> AuthException.TokenRefreshFailed()
                else -> AuthException.UnknownError()
            }
        }
    }
}
