# Настройка авторизации через Telegram

## 1. Создание бота

1. Откройте [@BotFather](https://t.me/BotFather) в Telegram.
2. Отправьте команду `/newbot`.
3. Укажите имя и username бота (например, `YaRyadomBot`).
4. Сохраните **токен бота** (нужен для backend).

## 2. Привязка домена

Для работы Login Widget необходимо привязать домен:

```
/setdomain
```

Укажите домен, с которого будет загружаться виджет.

Для разработки можно использовать:
- GitHub Pages
- ngrok
- любой временный HTTPS-домен

## 3. Указание имени бота в приложении

Откройте файл:

```
feature/auth/src/main/java/com/yaryadom/feature/auth/TelegramLoginWebView.kt
```

Замените значение:

```kotlin
object TelegramConfig {
    const val BOT_USERNAME = "YaRyadomBot" // ← ваше имя бота без @
}
```

## 4. Проверка подписи (обязательно на backend)

После получения данных от Telegram **никогда** не доверяйте им на клиенте.

На сервере необходимо проверить `hash` по алгоритму:

1. Собрать строку из всех полей (кроме hash) в формате `key=value`, отсортированных по ключу, разделённых `\n`.
2. Вычислить `secret_key = SHA256(bot_token)`.
3. Вычислить `HMAC-SHA256(data_check_string, secret_key)`.
4. Сравнить результат с полученным `hash`.

Документация: https://core.telegram.org/widgets/login#checking-authorization

## 5. Текущее состояние в приложении

- UI и WebView с официальным виджетом реализованы.
- Данные успешно парсятся.
- Пока отсутствует отправка на backend и проверка hash (будет добавлено после появления API).

## Рекомендуемый следующий шаг

Реализовать endpoint на backend:

```
POST /auth/telegram
Body: { id, first_name, last_name, username, photo_url, auth_date, hash }
```

В ответ сервер возвращает JWT-токен, который приложение сохраняет.
