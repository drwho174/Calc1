package com.drwho174.calc1.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
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
 * Тема хранится как [ThemeMode]. Для обратной совместимости читаются оба
 * прежних источника: булев ключ `THEME_SET` в DataStore (промежуточная версия)
 * и тот же ключ в SharedPreferences (версия на PreferenceFragmentCompat).
 * Если настроек нет вообще — используется [ThemeMode.SYSTEM].
 */
class SettingsRepository(context: Context) {

    private val appContext = context.applicationContext
    private val modeKey = stringPreferencesKey(KEY_MODE)
    private val legacyDataStoreKey = booleanPreferencesKey(LEGACY_KEY)
    private val perfusionRateKey = floatPreferencesKey(KEY_PERFUSION_RATE)

    val themeMode: Flow<ThemeMode> = appContext.settingsDataStore.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { preferences -> resolveMode(preferences) }

    /** Последний выбранный индекс перфузии (скорость на м²). */
    val perfusionRate: Flow<Float> = appContext.settingsDataStore.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { preferences -> preferences[perfusionRateKey] ?: DEFAULT_PERFUSION_RATE }

    suspend fun setThemeMode(mode: ThemeMode) {
        appContext.settingsDataStore.edit { preferences ->
            preferences[modeKey] = mode.name
            preferences.remove(legacyDataStoreKey)
        }
        legacyPreferences().edit().remove(LEGACY_KEY).apply()
    }

    suspend fun setPerfusionRate(rate: Float) {
        appContext.settingsDataStore.edit { preferences ->
            preferences[perfusionRateKey] = rate
        }
    }

    private fun resolveMode(preferences: Preferences): ThemeMode {
        preferences[modeKey]?.let { stored ->
            runCatching { ThemeMode.valueOf(stored) }.getOrNull()?.let { return it }
        }
        preferences[legacyDataStoreKey]?.let { dark ->
            return if (dark) ThemeMode.DARK else ThemeMode.LIGHT
        }
        val legacyPreferences = legacyPreferences()
        if (legacyPreferences.contains(LEGACY_KEY)) {
            return if (legacyPreferences.getBoolean(LEGACY_KEY, false)) {
                ThemeMode.DARK
            } else {
                ThemeMode.LIGHT
            }
        }
        return ThemeMode.SYSTEM
    }

    private fun legacyPreferences() =
        appContext.getSharedPreferences(LEGACY_STORE, Context.MODE_PRIVATE)

    private companion object {
        const val KEY_MODE = "THEME_MODE"
        const val KEY_PERFUSION_RATE = "PERFUSION_RATE"
        const val LEGACY_KEY = "THEME_SET"
        const val LEGACY_STORE = "settings"
        const val DEFAULT_PERFUSION_RATE = 2.4f
    }
}
