package com.drwho174.calc1.data

import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
import java.util.Locale

/**
 * Подбор языка приложения.
 *
 * Язык хранится в настройках ([LanguageMode]) и применяется подменой конфигурации
 * контекста: у приложения нет AppCompat, поэтому платформенный механизм per-app
 * locale (API 33+) не используется — так поведение одинаково на всех API.
 *
 * Если выбран системный язык, а система настроена на язык без перевода
 * (немецкий, итальянский и т. п.), берётся английский: он же лежит в values/
 * и служит запасным для ресурсов.
 */
object AppLocale {

    /** Запасной язык для неподдерживаемых системных языков. */
    private const val FALLBACK_LANGUAGE = "en"

    /** Языки, для которых в приложении есть перевод. */
    private val supportedLanguages = setOf("en", "ru", "zh")

    /** Языки, которые нужно применить для выбранного режима. */
    fun localesFor(mode: LanguageMode, systemLocales: LocaleList): LocaleList {
        return when (mode) {
            LanguageMode.SYSTEM -> {
                val systemLanguage = if (systemLocales.isEmpty) null else systemLocales[0].language
                if (systemLanguage in supportedLanguages) {
                    systemLocales
                } else {
                    LocaleList.forLanguageTags(FALLBACK_LANGUAGE)
                }
            }

            LanguageMode.ENGLISH -> LocaleList.forLanguageTags("en")
            LanguageMode.RUSSIAN -> LocaleList.forLanguageTags("ru")
            LanguageMode.CHINESE -> LocaleList.forLanguageTags("zh")
        }
    }

    /** Контекст с ресурсами выбранного языка. */
    fun wrap(base: Context, mode: LanguageMode): Context {
        val locales = localesFor(mode, base.resources.configuration.locales)
        val configuration = Configuration(base.resources.configuration).apply {
            setLocales(locales)
        }
        return base.createConfigurationContext(configuration)
    }

    /**
     * Переводит Locale по умолчанию на язык приложения.
     *
     * Нужно для чисел: `String.format` и разделитель дробной части берут
     * Locale по умолчанию, а не язык ресурсов.
     */
    fun applyDefaultLocale(context: Context) {
        val locales = context.resources.configuration.locales
        if (locales.isEmpty) return
        Locale.setDefault(locales[0])
    }

    /** Код языка для файлов справки в assets: `en`, `ru` или `zh`. */
    fun assetLanguage(context: Context): String {
        val locales = context.resources.configuration.locales
        return if (locales.isEmpty) FALLBACK_LANGUAGE else locales[0].language
    }
}
