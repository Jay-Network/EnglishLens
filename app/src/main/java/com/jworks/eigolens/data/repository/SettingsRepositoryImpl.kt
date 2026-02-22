package com.jworks.eigolens.data.repository

import com.jworks.eigolens.data.preferences.SettingsDataStore
import com.jworks.eigolens.domain.models.AppSettings
import com.jworks.eigolens.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: SettingsDataStore
) : SettingsRepository {

    override val settings: Flow<AppSettings> = dataStore.settingsFlow

    override suspend fun updateSettings(settings: AppSettings) {
        dataStore.updateSettings(settings)
    }
}
