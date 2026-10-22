package com.drwho174.calc1.ui.common

/**
 * Форматирование чисел как в прежнем UI: `String.format` без явной локали,
 * поэтому на русской локали разделитель остаётся запятой.
 */
fun Double.format(decimals: Int): String = String.format("%.${decimals}f", this)
