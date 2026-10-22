# Я рядом — Android

Android-приложение сервиса аренды жилья **«Я рядом»** с интеграцией Telegram.

Цель: предоставить пользователям удобный APK-клиент для поиска и размещения жилья, с авторизацией через Telegram, уведомлениями и быстрым переходом в чаты Telegram.

---

## Технологический стек

### Android (клиент)
| Компонент              | Технология                          |
|------------------------|-------------------------------------|
| Язык                   | Kotlin                              |
| UI                     | Jetpack Compose                     |
| Архитектура            | MVVM + Clean Architecture           |
| Dependency Injection   | Hilt                                |
| Навигация              | Navigation Compose                  |
| Сеть                   | Retrofit + OkHttp                   |
| Локальная БД           | Room                                |
| Асинхронность          | Coroutines + Flow                   |
| Изображения            | Coil                                |
| Авторизация            | Telegram Login                      |
| Уведомления            | Firebase Cloud Messaging            |

### Backend (планируется)
| Компонент              | Технология                          |
|------------------------|-------------------------------------|
| Runtime                | NestJS или FastAPI                  |
| База данных            | PostgreSQL                          |
| Auth                   | JWT + Telegram Auth                 |
| Хранение файлов        | S3-совместимое хранилище            |

---

## Текущая структура проекта

```
ya-ryadom-android/
├── app/                          # Главный модуль приложения
│   ├── src/main/
│   │   ├── java/com/yaryadom/app/
│   │   │   ├── YaRyadomApp.kt
│   │   │   ├── MainActivity.kt
│   │   │   ├── navigation/
│   │   │   ├── ui/theme/
│   │   │   └── ui/screens/
│   │   ├── AndroidManifest.xml
│   │   └── res/
│   └── build.gradle.kts
├── core/
│   ├── common/                   # Общие утилиты
│   ├── network/                  # Retrofit, API-клиенты
│   ├── database/                 # Room
│   └── ui/                       # Общие Compose-компоненты
├── feature/
│   ├── auth/                     # Авторизация через Telegram
│   ├── search/                   # Поиск объявлений
│   ├── listings/                 # Создание и управление объявлениями
│   └── profile/                  # Профиль пользователя
├── gradle/
│   └── libs.versions.toml        # Version Catalog
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

---

## Дорожная карта

### Этап 0. Подготовка
- [x] Создание репозитория
- [x] Выбор технологического стека
- [x] Формирование дорожной карты
- [x] Базовая структура Android-проекта (Gradle, модули)
- [ ] Настройка Backend-скелета

### Этап 1. Фундамент (следующий)
- [ ] Авторизация через Telegram
- [ ] Базовый профиль пользователя
- [ ] Полноценная навигация
- [ ] Подключение к Backend API
- [ ] Локальное хранение токена

### Этап 2. Функции арендатора
- [ ] Поиск объявлений с фильтрами
- [ ] Карточка объявления
- [ ] Избранное и заявки

### Этап 3. Функции арендодателя
- [ ] Создание и редактирование объявлений
- [ ] Загрузка фото

### Этап 4. Коммуникация
- [ ] Deep-links в Telegram
- [ ] Push-уведомления

### Этап 5. Релиз
- [ ] Сборка APK
- [ ] Полировка и документация

---

## Как открыть проект

1. Клонировать репозиторий:
   ```bash
   git clone https://github.com/stephanvoznyak-dot/ya-ryadom-android.git
   ```
2. Открыть в Android Studio (Hedgehog или новее).
3. Дождаться синхронизации Gradle.
4. Запустить на эмуляторе или устройстве.

> Примечание: иконки и некоторые ресурсы пока отсутствуют — будут добавлены на следующих шагах.

---

**Репозиторий:** https://github.com/stephanvoznyak-dot/ya-ryadom-android
