package com.yaryadom.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaryadom.core.common.auth.SessionManager
import com.yaryadom.core.common.auth.UserSession
import com.yaryadom.feature.auth.data.AuthApi
import com.yaryadom.feature.auth.data.TelegramAuthRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // Временный клиент. Позже будет из core:network через Hilt
    private val authApi: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

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

    fun onTelegramAuthSuccess(data: TelegramAuthData) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, showTelegramWebView = false) }

            try {
                val request = TelegramAuthRequest(
                    id = data.id,
                    first_name = data.firstName,
                    last_name = data.lastName,
                    username = data.username,
                    photo_url = data.photoUrl,
                    auth_date = data.authDate,
                    hash = data.hash
                )

                // Пытаемся проверить на backend.
                // Если сервер недоступен — сохраняем сессию локально (режим разработки).
                var token: String? = null
                var serverUserName: String? = null

                try {
                    val response = authApi.verifyTelegramAuth(request)
                    if (response.success) {
                        token = response.token
                        serverUserName = response.user?.firstName
                    } else {
                        // Сервер ответил ошибкой проверки — не пропускаем
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = response.error ?: "Ошибка проверки на сервере"
                            )
                        }
                        return@launch
                    }
                } catch (e: Exception) {
                    // Backend недоступен — работаем в offline/dev режиме
                    // В продакшене здесь лучше показывать ошибку
                }

                val session = UserSession(
                    telegramId = data.id,
                    firstName = data.firstName,
                    lastName = data.lastName,
                    username = data.username,
                    photoUrl = data.photoUrl,
                    token = token
                )

                sessionManager.saveSession(session)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        userName = serverUserName ?: data.firstName,
                        telegramAuthData = data
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
