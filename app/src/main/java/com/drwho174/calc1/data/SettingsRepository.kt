package com.drwho174.calc1.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
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
    private val languageKey = stringPreferencesKey(KEY_LANGUAGE)
    private val legacyDataStoreKey = booleanPreferencesKey(LEGACY_KEY)
    private val perfusionRateKey = floatPreferencesKey(KEY_PERFUSION_RATE)
    private val favoritesKey = stringSetPreferencesKey(KEY_FAVORITES)

    val themeMode: Flow<ThemeMode> = appContext.settingsDataStore.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { preferences -> resolveMode(preferences) }

    /**
     * Язык интерфейса. Читается синхронно в [com.drwho174.calc1.MainActivity.attachBaseContext],
     * чтобы ресурсы создавались уже на нужном языке.
     */
    val languageMode: Flow<LanguageMode> = appContext.settingsDataStore.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { preferences -> resolveLanguage(preferences) }

    /** Последний выбранный индекс перфузии (скорость на м²). */
    val perfusionRate: Flow<Float> = appContext.settingsDataStore.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { preferences -> preferences[perfusionRateKey] ?: DEFAULT_PERFUSION_RATE }

    /**
     * Избранные калькуляторы: ключи — имена `CalculatorId`.
     *
     * Набор хранится как есть: DataStore не сохраняет порядок, поэтому
     * главный экран сортирует избранное по общему списку калькуляторов.
     */
    val favoriteCalculators: Flow<Set<String>> = appContext.settingsDataStore.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { preferences -> preferences[favoritesKey].orEmpty() }

    suspend fun setThemeMode(mode: ThemeMode) {
        appContext.settingsDataStore.edit { preferences ->
            preferences[modeKey] = mode.name
            preferences.remove(legacyDataStoreKey)
        }
        legacyPreferences().edit().remove(LEGACY_KEY).apply()
    }

    suspend fun setLanguageMode(mode: LanguageMode) {
        appContext.settingsDataStore.edit { preferences ->
            preferences[languageKey] = mode.name
        }
    }

    suspend fun setPerfusionRate(rate: Float) {
        appContext.settingsDataStore.edit { preferences ->
            preferences[perfusionRateKey] = rate
        }
    }

    /** Переключает калькулятор в избранном; значение читается и пишется атомарно. */
    suspend fun toggleFavorite(calculatorId: String) {
        appContext.settingsDataStore.edit { preferences ->
            val current = preferences[favoritesKey].orEmpty()
            preferences[favoritesKey] = if (calculatorId in current) {
                current - calculatorId
            } else {
                current + calculatorId
            }
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

    private fun resolveLanguage(preferences: Preferences): LanguageMode {
        preferences[languageKey]?.let { stored ->
            runCatching { LanguageMode.valueOf(stored) }.getOrNull()?.let { return it }
        }
        return LanguageMode.SYSTEM
    }

    private fun legacyPreferences() =
        appContext.getSharedPreferences(LEGACY_STORE, Context.MODE_PRIVATE)

    private companion object {
        const val KEY_MODE = "THEME_MODE"
        const val KEY_LANGUAGE = "LANGUAGE_MODE"
        const val KEY_PERFUSION_RATE = "PERFUSION_RATE"
        const val KEY_FAVORITES = "FAVORITE_CALCULATORS"
        const val LEGACY_KEY = "THEME_SET"
        const val LEGACY_STORE = "settings"
        const val DEFAULT_PERFUSION_RATE = 2.4f
    }
}
