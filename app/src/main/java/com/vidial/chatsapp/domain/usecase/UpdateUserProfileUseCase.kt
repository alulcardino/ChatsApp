package com.vidial.chatsapp.domain.usecase

import com.vidial.chatsapp.data.repository.AuthException
import com.vidial.chatsapp.data.repository.mapFailure
import com.vidial.chatsapp.domain.model.UpdateProfileModel
import com.vidial.chatsapp.domain.model.toUpdateProfileDto
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
