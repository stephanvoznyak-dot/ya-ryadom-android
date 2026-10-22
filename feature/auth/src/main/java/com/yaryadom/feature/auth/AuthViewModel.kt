package com.yaryadom.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    /**
     * Показать WebView с официальным Telegram Login Widget.
     */
    fun startTelegramLogin() {
        _uiState.update {
            it.copy(
                showTelegramWebView = true,
                errorMessage = null,
                isLoading = false
            )
        }
    }

    fun hideTelegramWebView() {
        _uiState.update { it.copy(showTelegramWebView = false) }
    }

    /**
     * Вызывается после успешного получения данных от Telegram Login Widget.
     *
     * ВАЖНО ПО БЕЗОПАСНОСТИ:
     * Полученные данные (особенно hash) обязательно нужно отправить на backend
     * и проверить подпись с использованием bot_token.
     * Никогда не доверяйте данным только на клиенте.
     *
     * Алгоритм проверки на сервере описан здесь:
     * https://core.telegram.org/widgets/login#checking-authorization
     */
    fun onTelegramAuthSuccess(data: TelegramAuthData) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, showTelegramWebView = false) }

            try {
                // TODO: Отправить data на backend для проверки hash и получения JWT
                // val token = authRepository.verifyTelegramAuth(data)

                // Временное решение — считаем авторизацию успешной локально
                // (только для разработки, пока нет backend)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        userName = data.firstName + (data.lastName?.let { " $it" } ?: ""),
                        telegramAuthData = data
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Ошибка авторизации через Telegram"
                    )
                }
            }
        }
    }

    fun onTelegramAuthError(message: String) {
        _uiState.update {
            it.copy(
                showTelegramWebView = false,
                isLoading = false,
                errorMessage = message
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
