package com.yaryadom.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
     * Запуск процесса входа через Telegram.
     * На данном этапе реализована заглушка.
     * В будущем здесь будет:
     * 1. Открытие deep-link / WebView с Telegram Login Widget
     * 2. Получение auth-данных (id, first_name, username, hash)
     * 3. Отправка на backend для проверки подписи
     * 4. Сохранение JWT-токена
     */
    fun loginWithTelegram() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                // TODO: заменить на реальную интеграцию с Telegram
                delay(1200) // имитация сетевого запроса

                // Успешный вход (заглушка)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        userName = "Пользователь"
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Ошибка авторизации"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
