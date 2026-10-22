package com.yaryadom.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class AuthViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // Временный клиент. Позже будет предоставляться через Hilt из core:network
    private val authApi: AuthApi by lazy {
        Retrofit.Builder()
            // TODO: замените на адрес вашего backend
            // Для эмулятора Android: http://10.0.2.2:3000/
            // Для реального устройства: http://<IP-вашего-компьютера>:3000/
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

    /**
     * Получены данные от Telegram Login Widget.
     * Отправляем их на backend для официальной проверки hash.
     */
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

                val response = authApi.verifyTelegramAuth(request)

                if (response.success && response.token != null) {
                    // TODO: сохранить token в DataStore
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isAuthenticated = true,
                            userName = response.user?.firstName ?: data.firstName,
                            telegramAuthData = data
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = response.error ?: "Ошибка проверки на сервере"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Не удалось связаться с сервером: ${e.message}"
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
