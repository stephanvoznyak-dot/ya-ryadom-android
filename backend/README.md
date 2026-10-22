# Ya Ryadom — Backend

Минимальный backend для проверки авторизации через Telegram Login Widget.

## Запуск

```bash
cd backend
cp .env.example .env
# Отредактируйте .env — вставьте BOT_TOKEN от BotFather

npm install
npm run dev
```

Сервер поднимется на `http://localhost:3000`.

## Endpoint

### `POST /auth/telegram`

Проверяет данные, полученные от Telegram Login Widget.

**Request body:**
```json
{
  "id": 123456789,
  "first_name": "Ivan",
  "last_name": "Ivanov",
  "username": "ivan",
  "photo_url": "https://t.me/i/userpic/...",
  "auth_date": 1710000000,
  "hash": "a1b2c3d4e5f6..."
}
```

**Успешный ответ (200):**
```json
{
  "success": true,
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "telegramId": 123456789,
    "firstName": "Ivan",
    "lastName": "Ivanov",
    "username": "ivan",
    "photoUrl": "https://..."
  }
}
```

**Ошибки:**
- `400` — недостаточно данных
- `401` — неверная подпись (hash) или устаревшие данные
- `500` — внутренняя ошибка

## Как работает проверка

Используется **официальный алгоритм** Telegram:

1. Собирается строка `key=value` из всех полей кроме `hash`, ключи сортируются.
2. `secret_key = SHA256(bot_token)`
3. `HMAC-SHA256(data_check_string, secret_key)` сравнивается с `hash`.

Документация: https://core.telegram.org/widgets/login#checking-authorization

## Важно

- Никогда не храните `BOT_TOKEN` в клиентском приложении.
- Всегда проверяйте `hash` на сервере.
- `auth_date` ограничивается 24 часами (защита от повторного использования).
