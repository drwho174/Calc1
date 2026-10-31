package com.drwho174.calc1

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.drwho174.calc1.data.AppLocale
import com.drwho174.calc1.data.LanguageMode
import com.drwho174.calc1.data.SettingsRepository
import com.drwho174.calc1.data.ThemeMode
import com.drwho174.calc1.navigation.CalculatorId
import com.drwho174.calc1.navigation.calculatorIdsOf
import com.drwho174.calc1.navigation.storageKey
import com.drwho174.calc1.ui.Calc1App
import com.drwho174.calc1.ui.theme.Calc1Theme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Единственная Activity приложения.
 *
 * Наследуется от [ComponentActivity]: AppCompat и Material Components больше
 * не нужны — тему рисует Compose, окно использует платформенную тему.
 */
class MainActivity : ComponentActivity() {

    private val settingsRepository by lazy { SettingsRepository(applicationContext) }

    /** Язык, с которым созданы ресурсы этой Activity. */
    private var appliedLanguage = LanguageMode.SYSTEM

    /**
     * Язык нужно применить до создания ресурсов, поэтому сохранённый выбор
     * читается синхронно: это один небольшой файл DataStore, чтение занимает
     * единицы миллисекунд.
     */
    override fun attachBaseContext(newBase: Context) {
        val mode = runBlocking {
            runCatching { SettingsRepository(newBase).languageMode.first() }
                .getOrDefault(LanguageMode.SYSTEM)
        }
        appliedLanguage = mode

        val localizedContext = AppLocale.wrap(newBase, mode)
        AppLocale.applyDefaultLocale(localizedContext)
        super.attachBaseContext(localizedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val themeMode by settingsRepository.themeMode
                .collectAsStateWithLifecycle(initialValue = ThemeMode.SYSTEM)

            val languageMode by settingsRepository.languageMode
                .collectAsStateWithLifecycle(initialValue = appliedLanguage)

            // null — избранное ещё не прочитано из DataStore. Главный экран
            // фиксирует стартовую вкладку по первому значению, поэтому «пусто»
            // и «не загружено» должны различаться.
            val favoriteIds by settingsRepository.favoriteCalculators
                .map<Set<String>, Set<CalculatorId>?> { stored -> calculatorIdsOf(stored) }
                .collectAsStateWithLifecycle(initialValue = null)

            val darkTheme = when (themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            // Иконки системных панелей должны контрастировать с фоном Compose-темы.
            LaunchedEffect(darkTheme) {
                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = !darkTheme
                    isAppearanceLightNavigationBars = !darkTheme
                }
            }

            Calc1Theme(darkTheme = darkTheme) {
                Calc1App(
                    themeMode = themeMode,
                    onThemeModeChange = { mode ->
                        lifecycleScope.launch { settingsRepository.setThemeMode(mode) }
                    },
                    languageMode = languageMode,
                    onLanguageModeChange = { mode ->
                        lifecycleScope.launch {
                            settingsRepository.setLanguageMode(mode)
                            // Язык ресурсов применяется в attachBaseContext,
                            // поэтому экран нужно создать заново.
                            this@MainActivity.recreate()
                        }
                    },
                    favoriteIds = favoriteIds,
                    onToggleFavorite = { id ->
                        lifecycleScope.launch { settingsRepository.toggleFavorite(id.storageKey) }
                    },
                )
            }
        }
    }
}
