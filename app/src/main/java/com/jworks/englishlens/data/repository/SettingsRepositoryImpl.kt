package com.jworks.englishlens.data.repository

import com.jworks.englishlens.data.preferences.SettingsDataStore
import com.jworks.englishlens.domain.models.AppSettings
import com.jworks.englishlens.domain.repository.SettingsRepository
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
