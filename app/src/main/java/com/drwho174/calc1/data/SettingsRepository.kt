package com.drwho174.calc1.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "calc1_settings",
)

/**
 * Настройки приложения на DataStore.
 *
 * При первом чтении, если в DataStore ещё нет значения, берётся старый ключ
 * `THEME_SET` из SharedPreferences — так настройки не теряются после перехода
 * с `PreferenceFragmentCompat`.
 */
class SettingsRepository(context: Context) {

    private val appContext = context.applicationContext
    private val themeKey = booleanPreferencesKey(KEY_THEME)

    val darkTheme: Flow<Boolean> = appContext.settingsDataStore.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { preferences -> preferences[themeKey] ?: legacyTheme() }

    suspend fun setDarkTheme(enabled: Boolean) {
        appContext.settingsDataStore.edit { preferences ->
            preferences[themeKey] = enabled
        }
        // Старый ключ больше не нужен: единственный источник — DataStore.
        legacyPreferences().edit().remove(KEY_THEME).apply()
    }

    private fun legacyPreferences() =
        appContext.getSharedPreferences(LEGACY_STORE, Context.MODE_PRIVATE)

    private fun legacyTheme(): Boolean = legacyPreferences().getBoolean(KEY_THEME, false)

    private companion object {
        const val KEY_THEME = "THEME_SET"
        const val LEGACY_STORE = "settings"
    }
}
