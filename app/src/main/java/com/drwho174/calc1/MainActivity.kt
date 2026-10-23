package com.drwho174.calc1

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
import com.drwho174.calc1.data.SettingsRepository
import com.drwho174.calc1.data.ThemeMode
import com.drwho174.calc1.ui.Calc1App
import com.drwho174.calc1.ui.theme.Calc1Theme
import kotlinx.coroutines.launch

/**
 * Единственная Activity приложения.
 *
 * Наследуется от [ComponentActivity]: AppCompat и Material Components больше
 * не нужны — тему рисует Compose, окно использует платформенную тему.
 */
class MainActivity : ComponentActivity() {

    private val settingsRepository by lazy { SettingsRepository(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val themeMode by settingsRepository.themeMode
                .collectAsStateWithLifecycle(initialValue = ThemeMode.SYSTEM)

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
                )
            }
        }
    }
}
