package com.vidial.chatsapp.domain.usecase

import com.vidial.chatsapp.data.remote.mappers.toUpdateProfileDto
import com.vidial.chatsapp.domain.exception.AuthException
import com.vidial.chatsapp.domain.exception.mapFailure
import com.vidial.chatsapp.domain.model.UpdateProfileModel
import com.vidial.chatsapp.domain.repository.AuthRepository
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(profileRequest: UpdateProfileModel): Result<Unit> {
        return authRepository.updateUserProfile(profileRequest.toUpdateProfileDto())
            .mapFailure { exception ->
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
