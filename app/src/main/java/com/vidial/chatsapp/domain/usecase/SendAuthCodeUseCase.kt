package com.vidial.chatsapp.domain.usecase

import com.vidial.chatsapp.data.remote.requests.PhoneRequest
import com.vidial.chatsapp.domain.repository.AuthRepository
import javax.inject.Inject

class   SendAuthCodeUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(phone: String): Result<Unit> {
        return repository.sendAuthCode(PhoneRequest(phone))
    }
}
