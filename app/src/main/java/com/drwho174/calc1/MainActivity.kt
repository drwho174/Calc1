package com.drwho174.calc1

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
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
 * Больше нет ни тулбара из XML, ни контейнера фрагментов: вся навигация —
 * Navigation 3 внутри Compose, а тема читается из DataStore и передаётся
 * в композицию.
 */
class MainActivity : AppCompatActivity() {

    private val settingsRepository by lazy { SettingsRepository(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val themeMode by settingsRepository.themeMode
                .collectAsStateWithLifecycle(initialValue = ThemeMode.SYSTEM)

            val darkTheme = when (themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            // Иконки статус-бара должны контрастировать с фоном Compose-темы.
            LaunchedEffect(darkTheme) {
                WindowCompat.getInsetsController(window, window.decorView)
                    .isAppearanceLightStatusBars = !darkTheme
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
