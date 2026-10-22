package com.yaryadom.feature.auth

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val showTelegramWebView: Boolean = false,
    val errorMessage: String? = null,
    val userName: String? = null,
    val telegramAuthData: TelegramAuthData? = null
)
