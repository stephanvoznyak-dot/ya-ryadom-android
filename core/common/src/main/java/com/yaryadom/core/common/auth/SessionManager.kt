package com.yaryadom.core.common.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

class SessionManager(private val context: Context) {

    private object Keys {
        val TELEGRAM_ID = longPreferencesKey("telegram_id")
        val FIRST_NAME = stringPreferencesKey("first_name")
        val LAST_NAME = stringPreferencesKey("last_name")
        val USERNAME = stringPreferencesKey("username")
        val PHOTO_URL = stringPreferencesKey("photo_url")
        val TOKEN = stringPreferencesKey("token")
    }

    val sessionFlow: Flow<UserSession?> = context.dataStore.data.map { prefs ->
        val id = prefs[Keys.TELEGRAM_ID] ?: return@map null
        val firstName = prefs[Keys.FIRST_NAME] ?: return@map null

        UserSession(
            telegramId = id,
            firstName = firstName,
            lastName = prefs[Keys.LAST_NAME],
            username = prefs[Keys.USERNAME],
            photoUrl = prefs[Keys.PHOTO_URL],
            token = prefs[Keys.TOKEN]
        )
    }

    suspend fun saveSession(session: UserSession) {
        context.dataStore.edit { prefs ->
            prefs[Keys.TELEGRAM_ID] = session.telegramId
            prefs[Keys.FIRST_NAME] = session.firstName
            session.lastName?.let { prefs[Keys.LAST_NAME] = it } ?: prefs.remove(Keys.LAST_NAME)
            session.username?.let { prefs[Keys.USERNAME] = it } ?: prefs.remove(Keys.USERNAME)
            session.photoUrl?.let { prefs[Keys.PHOTO_URL] = it } ?: prefs.remove(Keys.PHOTO_URL)
            session.token?.let { prefs[Keys.TOKEN] = it } ?: prefs.remove(Keys.TOKEN)
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}
