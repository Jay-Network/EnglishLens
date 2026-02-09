package com.jworks.englishlens.domain.repository

import com.jworks.englishlens.domain.models.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settings: Flow<AppSettings>
    suspend fun updateSettings(settings: AppSettings)
}
