package com.jworks.eigolens.domain.repository

import com.jworks.eigolens.domain.models.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settings: Flow<AppSettings>
    suspend fun updateSettings(settings: AppSettings)
}
