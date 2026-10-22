package com.yaryadom.feature.auth.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt-модуль feature:auth.
 * Здесь в будущем будут предоставлены зависимости,
 * связанные с авторизацией (репозиторий, Telegram Auth Client и т.д.).
 */
@Module
@InstallIn(SingletonComponent::class)
object AuthModule
