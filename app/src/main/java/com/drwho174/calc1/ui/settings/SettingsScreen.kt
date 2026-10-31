package com.drwho174.calc1.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drwho174.calc1.R
import com.drwho174.calc1.data.LanguageMode
import com.drwho174.calc1.data.ThemeMode
import com.drwho174.calc1.ui.components.ChoiceGroup
import com.drwho174.calc1.ui.theme.Calc1Theme

/** Настройки: язык интерфейса и тема. */
@Composable
fun SettingsScreen(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
    languageMode: LanguageMode = LanguageMode.SYSTEM,
    onLanguageModeChange: (LanguageMode) -> Unit = {},
) {
    val themeLabels = mapOf(
        ThemeMode.SYSTEM to stringResource(R.string.settings_theme_system),
        ThemeMode.LIGHT to stringResource(R.string.settings_theme_light),
        ThemeMode.DARK to stringResource(R.string.settings_theme_dark),
    )

    // Названия языков всегда пишутся на самих языках — так их узнают
    // носители, даже если текущий интерфейс им непонятен.
    val languageLabels = mapOf(
        LanguageMode.SYSTEM to stringResource(R.string.settings_language_system),
        LanguageMode.ENGLISH to stringResource(R.string.language_english),
        LanguageMode.RUSSIAN to stringResource(R.string.language_russian),
        LanguageMode.CHINESE to stringResource(R.string.language_chinese),
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        ChoiceGroup(
            title = stringResource(R.string.settings_language),
            options = LanguageMode.entries,
            selected = languageMode,
            onSelect = onLanguageModeChange,
            label = { mode -> languageLabels.getValue(mode) },
        )

        Text(
            text = stringResource(R.string.settings_language_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        ChoiceGroup(
            title = stringResource(R.string.settings_theme),
            options = ThemeMode.entries,
            selected = themeMode,
            onSelect = onThemeModeChange,
            label = { mode -> themeLabels.getValue(mode) },
        )

        Text(
            text = stringResource(R.string.settings_theme_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    Calc1Theme {
        SettingsScreen(
            themeMode = ThemeMode.SYSTEM,
            onThemeModeChange = {},
            languageMode = LanguageMode.SYSTEM,
            onLanguageModeChange = {},
        )
    }
}
