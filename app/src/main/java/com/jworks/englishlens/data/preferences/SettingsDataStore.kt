package com.jworks.englishlens.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jworks.englishlens.domain.models.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "englishlens_settings")

class SettingsDataStore(private val context: Context) {

    private object Keys {
        val HIGHLIGHT_COLOR = longPreferencesKey("highlight_color")
        val STROKE_WIDTH = floatPreferencesKey("stroke_width")
    }

    private val defaults = AppSettings()

    val settingsFlow: Flow<AppSettings> = context.dataStore.data.map { prefs ->
        AppSettings(
            highlightColor = prefs[Keys.HIGHLIGHT_COLOR] ?: defaults.highlightColor,
            strokeWidth = prefs[Keys.STROKE_WIDTH] ?: defaults.strokeWidth
        )
    }

    suspend fun updateSettings(settings: AppSettings) {
        context.dataStore.edit { prefs ->
            prefs[Keys.HIGHLIGHT_COLOR] = settings.highlightColor
            prefs[Keys.STROKE_WIDTH] = settings.strokeWidth
        }
    }
}
