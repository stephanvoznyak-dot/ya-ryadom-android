package com.yaryadom.feature.auth.data

import retrofit2.http.Body
import retrofit2.http.POST

data class TelegramAuthRequest(
    val id: Long,
    val first_name: String,
    val last_name: String? = null,
    val username: String? = null,
    val photo_url: String? = null,
    val auth_date: Long,
    val hash: String
)

data class AuthResponse(
    val success: Boolean,
    val token: String? = null,
    val user: UserDto? = null,
    val error: String? = null
)

data class UserDto(
    val telegramId: Long,
    val firstName: String,
    val lastName: String? = null,
    val username: String? = null,
    val photoUrl: String? = null
)

interface AuthApi {
    @POST("auth/telegram")
    suspend fun verifyTelegramAuth(@Body body: TelegramAuthRequest): AuthResponse
}
