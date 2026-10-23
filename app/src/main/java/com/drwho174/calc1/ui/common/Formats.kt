package com.drwho174.calc1.ui.common

/**
 * Форматирование чисел как в прежнем UI: `String.format` без явной локали,
 * поэтому на русской локали разделитель остаётся запятой.
 */
fun Double.format(decimals: Int): String = String.format("%.${decimals}f", this)

/** То же для значений слайдеров, которые приходят как Float. */
fun Float.format(decimals: Int): String = toDouble().format(decimals)

/**
 * Разбор введённого числа с учётом русской раскладки: клавиатура даёт запятую,
 * а [String.toDoubleOrNull] понимает только точку.
 */
fun String.toNumberOrNull(): Double? = trim().replace(',', '.').toDoubleOrNull()
