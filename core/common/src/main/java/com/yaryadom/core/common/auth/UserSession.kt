package com.yaryadom.core.common.auth

/**
 * Локальная сессия пользователя после успешной авторизации через Telegram.
 */
data class UserSession(
    val telegramId: Long,
    val firstName: String,
    val lastName: String? = null,
    val username: String? = null,
    val photoUrl: String? = null,
    val token: String? = null          // JWT от backend (может быть null на этапе разработки)
)
