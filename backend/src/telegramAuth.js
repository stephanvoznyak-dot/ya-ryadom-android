const crypto = require('crypto');

/**
 * Официальная проверка данных авторизации Telegram Login Widget.
 * Документация: https://core.telegram.org/widgets/login#checking-authorization
 *
 * @param {Object} data - данные от Telegram (id, first_name, auth_date, hash и т.д.)
 * @param {string} botToken - токен бота от BotFather
 * @returns {boolean} true, если подпись верна
 */
function verifyTelegramAuth(data, botToken) {
  if (!data || !data.hash || !botToken) {
    return false;
  }

  const { hash, ...rest } = data;

  // 1. Собираем строку проверки: ключи в алфавитном порядке, формат key=value, через \n
  const dataCheckString = Object.keys(rest)
    .sort()
    .map((key) => `${key}=${rest[key]}`)
    .join('\n');

  // 2. secret_key = SHA256(bot_token)
  const secretKey = crypto
    .createHash('sha256')
    .update(botToken)
    .digest();

  // 3. HMAC-SHA256(data_check_string, secret_key)
  const calculatedHash = crypto
    .createHmac('sha256', secretKey)
    .update(dataCheckString)
    .digest('hex');

  // 4. Сравниваем с полученным hash
  return calculatedHash === hash;
}

/**
 * Проверяем, что auth_date не слишком старый (защита от replay-атак).
 * По умолчанию — не старше 24 часов.
 */
function isAuthDateValid(authDate, maxAgeSeconds = 86400) {
  const now = Math.floor(Date.now() / 1000);
  return now - Number(authDate) <= maxAgeSeconds;
}

module.exports = {
  verifyTelegramAuth,
  isAuthDateValid,
};
