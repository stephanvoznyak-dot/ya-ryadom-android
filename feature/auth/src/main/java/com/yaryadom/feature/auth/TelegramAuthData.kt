package com.yaryadom.feature.auth

/**
 * Данные, которые возвращает Telegram Login Widget / Bot Auth.
 * Согласно официальной документации Telegram:
 * https://core.telegram.org/widgets/login
 */
data class TelegramAuthData(
    val id: Long,
    val firstName: String,
    val lastName: String? = null,
    val username: String? = null,
    val photoUrl: String? = null,
    val authDate: Long,
    val hash: String
)
