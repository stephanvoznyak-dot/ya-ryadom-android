# Я рядом — Android

Android-приложение сервиса аренды жилья **«Я рядом»** с интеграцией Telegram.

---

## Технологический стек

- **Kotlin** + Jetpack Compose
- MVVM + Clean Architecture
- Hilt, Navigation Compose, Retrofit, Room, Coil
- Авторизация: **Telegram Login Widget** (официальный)

---

## Текущий прогресс

### Этап 0. Подготовка
- [x] Репозиторий и структура проекта
- [x] Модули (core + feature)

### Этап 1. Фундамент
- [x] Splash-экран
- [x] Экран авторизации
- [x] **Реальный вход через Telegram Login Widget** (WebView)
- [x] Парсинг данных Telegram (id, имя, username, hash и т.д.)
- [x] Навигация Splash → Auth → Home
- [ ] Проверка hash на backend + JWT
- [ ] Сохранение токена (DataStore)
- [ ] Профиль пользователя

### Этап 2–5
См. подробную дорожную карту ниже.

---

## Как работает авторизация сейчас

1. Пользователь нажимает «Войти через Telegram».
2. Открывается WebView с официальным Telegram Login Widget.
3. После подтверждения в Telegram данные возвращаются в приложение.
4. Приложение парсит данные и (пока) считает вход успешным.
5. Происходит переход на главный экран.

> **Важно:** проверка подписи (`hash`) должна выполняться на сервере.  
> Подробная инструкция: [`docs/TELEGRAM_AUTH.md`](docs/TELEGRAM_AUTH.md)

---

## Настройка бота (обязательно)

1. Создайте бота через [@BotFather](https://t.me/BotFather).
2. Выполните `/setdomain` и укажите домен.
3. В файле `TelegramLoginWebView.kt` замените:

```kotlin
const val BOT_USERNAME = "YaRyadomBot" // ← ваше имя бота
```

---

## Структура

```
ya-ryadom-android/
├── app/
├── core/          (common, network, database, ui)
├── feature/
│   └── auth/      ← реализован Telegram Login
├── docs/
│   └── TELEGRAM_AUTH.md
└── ...
```

---

## Дорожная карта

| Этап | Содержание                              | Статус     |
|------|-----------------------------------------|------------|
| 0    | Структура проекта                       | Готово     |
| 1    | Авторизация через Telegram              | В процессе |
| 2    | Поиск и просмотр объявлений             | Ожидает    |
| 3    | Размещение объявлений                   | Ожидает    |
| 4    | Чаты + уведомления                      | Ожидает    |
| 5    | Сборка APK и релиз                      | Ожидает    |

---

**Репозиторий:** https://github.com/stephanvoznyak-dot/ya-ryadom-android
