package com.yaryadom.feature.auth

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

/**
 * ВАЖНО:
 * 1. Создайте бота через @BotFather
 * 2. Выполните команду /setdomain и укажите домен (можно использовать временный,
 *    например через ngrok или github pages для разработки)
 * 3. Замените BOT_USERNAME на имя вашего бота (без @)
 */
object TelegramConfig {
    // TODO: замените на имя вашего бота
    const val BOT_USERNAME = "YaRyadomBot"
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun TelegramLoginWebView(
    onAuthSuccess: (TelegramAuthData) -> Unit,
    onError: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true

                addJavascriptInterface(object {
                    @JavascriptInterface
                    fun onTelegramAuth(json: String) {
                        try {
                            val data = parseTelegramAuthJson(json)
                            onAuthSuccess(data)
                        } catch (e: Exception) {
                            onError("Ошибка разбора данных Telegram: ${e.message}")
                        }
                    }
                }, "AndroidBridge")

                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        // Внедряем обработчик результата Login Widget
                        view?.evaluateJavascript(
                            """
                            window.onTelegramAuth = function(user) {
                                AndroidBridge.onTelegramAuth(JSON.stringify(user));
                            };
                            """.trimIndent(),
                            null
                        )
                    }

                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        return false
                    }
                }

                // HTML-страница с официальным Telegram Login Widget
                val html = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <style>
                            body {
                                margin: 0;
                                padding: 0;
                                display: flex;
                                justify-content: center;
                                align-items: center;
                                min-height: 100vh;
                                background-color: #f8fafc;
                                font-family: -apple-system, BlinkMacSystemFont, sans-serif;
                            }
                            .container {
                                text-align: center;
                                padding: 24px;
                            }
                            h2 {
                                color: #0f172a;
                                margin-bottom: 8px;
                            }
                            p {
                                color: #64748b;
                                margin-bottom: 24px;
                            }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <h2>Я рядом</h2>
                            <p>Войдите через Telegram</p>
                            <script async src="https://telegram.org/js/telegram-widget.js?22"
                                data-telegram-login="${TelegramConfig.BOT_USERNAME}"
                                data-size="large"
                                data-radius="12"
                                data-onauth="onTelegramAuth(user)"
                                data-request-access="write">
                            </script>
                        </div>
                    </body>
                    </html>
                """.trimIndent()

                loadDataWithBaseURL(
                    "https://oauth.telegram.org",
                    html,
                    "text/html",
                    "UTF-8",
                    null
                )
            }
        }
    )
}

private fun parseTelegramAuthJson(json: String): TelegramAuthData {
    // Простой парсинг без внешних библиотек (можно заменить на Gson/Moshi)
    fun extract(key: String): String? {
        val regex = "\"$key\"\\s*:\\s*\"?([^,\"}]+)\"?".toRegex()
        return regex.find(json)?.groupValues?.get(1)?.trim('"')
    }

    val id = extract("id")?.toLongOrNull()
        ?: throw IllegalArgumentException("id отсутствует")
    val firstName = extract("first_name")
        ?: throw IllegalArgumentException("first_name отсутствует")
    val authDate = extract("auth_date")?.toLongOrNull()
        ?: throw IllegalArgumentException("auth_date отсутствует")
    val hash = extract("hash")
        ?: throw IllegalArgumentException("hash отсутствует")

    return TelegramAuthData(
        id = id,
        firstName = firstName,
        lastName = extract("last_name"),
        username = extract("username"),
        photoUrl = extract("photo_url"),
        authDate = authDate,
        hash = hash
    )
}
