# Авторизация через Telegram + проверка на сервере

## Как это работает

1. Пользователь нажимает «Войти через Telegram» в приложении.
2. Открывается официальный Telegram Login Widget (WebView).
3. Telegram возвращает данные + `hash`.
4. Приложение отправляет эти данные на **ваш backend** (`POST /auth/telegram`).
5. Backend проверяет `hash` по **официальному алгоритму Telegram** с использованием `BOT_TOKEN`.
6. Если подпись верна — backend выдаёт JWT-токен.
7. Приложение сохраняет токен и считает пользователя авторизованным.

> Telegram **не** предоставляет публичный endpoint «проверь за меня».  
> Проверка всегда выполняется на **вашем** сервере с помощью bot token.

---

## 1. Создание бота

1. Откройте [@BotFather](https://t.me/BotFather).
2. `/newbot` → получите токен.
3. `/setdomain` → укажите домен (для Login Widget).

## 2. Настройка backend

```bash
cd backend
cp .env.example .env
```

В `.env` укажите:

```
BOT_TOKEN=ваш_токен_от_BotFather
JWT_SECRET=любой_длинный_секрет
```

Запуск:

```bash
npm install
npm run dev
```

## 3. Настройка Android

В файле `TelegramLoginWebView.kt`:

```kotlin
const val BOT_USERNAME = "ИмяВашегоБота" // без @
```

В `AuthViewModel.kt` при необходимости измените `baseUrl`:

- Эмулятор Android → `http://10.0.2.2:3000/`
- Реальное устройство → `http://IP-вашего-компьютера:3000/`

## 4. Алгоритм проверки (уже реализован)

Файл `backend/src/telegramAuth.js` содержит точную реализацию из документации Telegram:

1. Собирается `data_check_string` (все поля кроме hash, отсортированные).
2. `secret_key = SHA256(bot_token)`
3. `HMAC-SHA256` сравнивается с полученным `hash`.
4. Дополнительно проверяется, что `auth_date` не старше 24 часов.

---

## Полезные ссылки

- Официальная документация: https://core.telegram.org/widgets/login#checking-authorization
- Backend: `/backend`
