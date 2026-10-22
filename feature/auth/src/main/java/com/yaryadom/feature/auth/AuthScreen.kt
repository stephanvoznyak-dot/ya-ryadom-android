package com.yaryadom.feature.auth

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            onAuthSuccess()
        }
    }

    // Обработка кнопки «Назад» при открытом WebView
    BackHandler(enabled = uiState.showTelegramWebView) {
        viewModel.hideTelegramWebView()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.showTelegramWebView) {
            // Режим реального входа через Telegram Login Widget
            Column(modifier = Modifier.fillMaxSize()) {
                TextButton(
                    onClick = { viewModel.hideTelegramWebView() },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("← Назад")
                }

                TelegramLoginWebView(
                    onAuthSuccess = { data -> viewModel.onTelegramAuthSuccess(data) },
                    onError = { message -> viewModel.onTelegramAuthError(message) },
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            // Обычный экран с кнопкой
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Я рядом",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 36.sp
                    ),
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Сервис аренды жилья\nс интеграцией Telegram",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(48.dp))

                Button(
                    onClick = { viewModel.startTelegramLogin() },
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0088CC)
                    )
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Войти через Telegram",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                uiState.errorMessage?.let { error ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Нажимая кнопку, вы соглашаетесь\nс условиями использования сервиса",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
