require('dotenv').config();
const express = require('express');
const cors = require('cors');
const jwt = require('jsonwebtoken');
const { verifyTelegramAuth, isAuthDateValid } = require('./telegramAuth');

const app = express();
const PORT = process.env.PORT || 3000;

// Обязательные переменные окружения
const BOT_TOKEN = process.env.BOT_TOKEN;
const JWT_SECRET = process.env.JWT_SECRET || 'change-me-in-production';

if (!BOT_TOKEN) {
  console.warn('⚠️  BOT_TOKEN не задан в .env — проверка Telegram Auth будет падать');
}

app.use(cors());
app.use(express.json());

/**
 * POST /auth/telegram
 * Принимает данные от Telegram Login Widget и проверяет их.
 *
 * Body пример:
 * {
 *   "id": 123456789,
 *   "first_name": "Ivan",
 *   "last_name": "Ivanov",
 *   "username": "ivan",
 *   "photo_url": "https://...",
 *   "auth_date": 1710000000,
 *   "hash": "abc123..."
 * }
 */
app.post('/auth/telegram', (req, res) => {
  try {
    const data = req.body;

    if (!data || !data.id || !data.hash || !data.auth_date) {
      return res.status(400).json({
        success: false,
        error: 'Недостаточно данных для авторизации',
      });
    }

    // 1. Проверяем подпись (официальный алгоритм Telegram)
    const isValid = verifyTelegramAuth(data, BOT_TOKEN);

    if (!isValid) {
      return res.status(401).json({
        success: false,
        error: 'Неверная подпись Telegram (hash)',
      });
    }

    // 2. Проверяем свежесть auth_date (не старше 24 часов)
    if (!isAuthDateValid(data.auth_date)) {
      return res.status(401).json({
        success: false,
        error: 'Данные авторизации устарели',
      });
    }

    // 3. Формируем JWT
    const payload = {
      telegramId: data.id,
      firstName: data.first_name,
      lastName: data.last_name || null,
      username: data.username || null,
      photoUrl: data.photo_url || null,
    };

    const token = jwt.sign(payload, JWT_SECRET, { expiresIn: '30d' });

    return res.json({
      success: true,
      token,
      user: payload,
    });
  } catch (err) {
    console.error('Ошибка /auth/telegram:', err);
    return res.status(500).json({
      success: false,
      error: 'Внутренняя ошибка сервера',
    });
  }
});

// Health-check
app.get('/health', (req, res) => {
  res.json({ status: 'ok', service: 'ya-ryadom-backend' });
});

app.listen(PORT, () => {
  console.log(`🚀 Ya Ryadom backend запущен на порту ${PORT}`);
  console.log(`   POST /auth/telegram — проверка входа через Telegram`);
});
