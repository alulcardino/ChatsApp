package com.vidial.chatsapp.data.repository

sealed class AuthException(message: String) : Exception(message) {
    class TokenRefreshFailed : AuthException("Unable to refresh token")
    class NotFound : AuthException("Resource not found")
    class Unauthorized(val s: String = "") : AuthException("Unauthorized access")
    class NetworkError : AuthException("Network error")
    class UnknownError : AuthException("Unknown error")
}

inline fun <T> Result<T>.mapFailure(transform: (Throwable) -> Throwable): Result<T> {
    return fold(
        onSuccess = { this },
        onFailure = { Result.failure(transform(it)) }
    )
}
