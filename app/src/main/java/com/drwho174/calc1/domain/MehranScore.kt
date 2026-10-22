package com.drwho174.calc1.domain

import kotlin.math.roundToInt

/** Категория СКФ в Mehran Score — баллы из таблицы калькулятора. */
enum class EgfrCategory(val points: Int) {
    ABOVE_60(0),
    FROM_40_TO_60(2),
    FROM_20_TO_40(4),
    BELOW_20(6),
}

/** Категория СКФ по значению, как её выбирает диалог СКФ. */
fun egfrCategoryFor(gfr: Double): EgfrCategory = when {
    gfr > 60.0 -> EgfrCategory.ABOVE_60
    gfr >= 40.0 -> EgfrCategory.FROM_40_TO_60
    gfr >= 20.0 -> EgfrCategory.FROM_20_TO_40
    else -> EgfrCategory.BELOW_20
}

data class MehranScoreInput(
    val heartFailure: Boolean = false,
    val frailty: Boolean = false,
    val iabp: Boolean = false,
    val hypotension: Boolean = false,
    val anaemia: Boolean = false,
    val diabetes: Boolean = false,
    val contrastVolumeMl: Double,
    val egfrCategory: EgfrCategory,
)

/** Сумма баллов Mehran Score. */
fun mehranPoints(input: MehranScoreInput): Int {
    // 1 балл за каждые 100 мл контраста, округление как в исходном фрагменте.
    val contrastPoints = (input.contrastVolumeMl / 100).roundToInt()

    var sum = 0
    if (input.heartFailure) sum += 5
    if (input.frailty) sum += 4
    if (input.iabp) sum += 5
    if (input.hypotension) sum += 5
    if (input.anaemia) sum += 3
    if (input.diabetes) sum += 5
    sum += contrastPoints
    sum += input.egfrCategory.points
    return sum
}

/** Риск контраст-индуцированной нефропатии, %. */
fun mehranCinRiskPercent(points: Int): Double = when {
    points <= 5 -> 7.5
    points in 6..10 -> 14.0
    points in 11..15 -> 26.1
    else -> 57.3
}

/** Риск диализа, %. */
fun mehranDialysisRiskPercent(points: Int): Double = when {
    points <= 5 -> 0.04
    points in 6..10 -> 0.12
    points in 11..15 -> 1.09
    else -> 12.6
}
