package com.drwho174.calc1.ui.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.drwho174.calc1.data.AppLocale
import com.drwho174.calc1.ui.components.MarkdownText

/**
 * Справка по калькулятору: markdown-файл из `assets/about/<язык>/`.
 *
 * Если перевода нет, берётся английский вариант — он же служит запасным
 * для остального интерфейса.
 */
@Composable
fun AboutScreen(
    assetPath: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val language = AppLocale.assetLanguage(context)

    // Файлы справки — несколько килобайт, поэтому читаем синхронно в remember:
    // это детерминированно для скриншот-тестов и не требует состояния загрузки.
    val markdown = remember(assetPath, language) {
        val candidates = listOf(
            "about/$language/$assetPath",
            "about/en/$assetPath",
            "about/$assetPath",
        )
        candidates.firstNotNullOfOrNull { path ->
            runCatching {
                context.assets.open(path).bufferedReader().use { it.readText() }
            }.getOrNull()
        }.orEmpty()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        MarkdownText(markdown = markdown)
    }
}
