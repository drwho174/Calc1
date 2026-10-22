package com.drwho174.calc1.textandsettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.drwho174.calc1.R
import com.drwho174.calc1.contract.HasCustomTitle
import com.drwho174.calc1.data.SettingsRepository
import com.drwho174.calc1.data.ThemeMode
import com.drwho174.calc1.ui.host.composeScreen
import com.drwho174.calc1.ui.settings.SettingsScreen
import kotlinx.coroutines.launch

/**
 * Настройки на Compose.
 *
 * Значение хранится в DataStore; смена темы дополнительно применяется через
 * AppCompatDelegate, потому что каркас пока фрагментный. На финальном этапе
 * (Navigation 3) тема станет состоянием Compose и AppCompatDelegate уйдёт.
 */
class SettingsOnMainMenu : Fragment(), HasCustomTitle {

    private val settingsRepository by lazy { SettingsRepository(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = composeScreen {
        val scope = rememberCoroutineScope()
        val themeMode by settingsRepository.themeMode
            .collectAsStateWithLifecycle(initialValue = ThemeMode.SYSTEM)

        SettingsScreen(
            themeMode = themeMode,
            onThemeModeChange = { mode ->
                scope.launch {
                    settingsRepository.setThemeMode(mode)
                    AppCompatDelegate.setDefaultNightMode(mode.toNightMode())
                }
            },
        )
    }

    override fun getTitleRes(): Int = R.string.name_settings
}

internal fun ThemeMode.toNightMode(): Int = when (this) {
    ThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
    ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
}
