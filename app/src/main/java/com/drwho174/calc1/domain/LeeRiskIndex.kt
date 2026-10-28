package com.drwho174.calc1.domain

/**
 * Индекс Lee — Revised Cardiac Risk Index (Lee TH et al., Circulation 1999).
 *
 * Шесть независимых предикторов крупных кардиальных осложнений (MACE)
 * при внесердечных операциях; сумма 0–6.
 */
data class LeeRiskInput(
    val highRiskSurgery: Boolean = false,
    val ischemicHeartDisease: Boolean = false,
    val heartFailure: Boolean = false,
    val cerebrovascularDisease: Boolean = false,
    val insulinTherapy: Boolean = false,
    val creatinineAbove177: Boolean = false,
)

/** Сумма предикторов. */
fun leeRiskScore(input: LeeRiskInput): Int = listOf(
    input.highRiskSurgery,
    input.ischemicHeartDisease,
    input.heartFailure,
    input.cerebrovascularDisease,
    input.insulinTherapy,
    input.creatinineAbove177,
).count { it }

/**
 * Риск MACE (сердечная смерть, инфаркт миокарда, остановка сердца), %.
 *
 * Значения валидационной когорты Lee 1999: 0 → 0,4 %, 1 → 0,9 %,
 * 2 → 7 %, 3 и более → 11 %.
 */
fun leeMaceRiskPercent(score: Int): Double = when {
    score <= 0 -> 0.4
    score == 1 -> 0.9
    score == 2 -> 7.0
    else -> 11.0
}

/** Качественная градация риска для окраски результата. */
enum class LeeRiskClass { LOW, MODERATE, HIGH, VERY_HIGH }

fun leeRiskClass(score: Int): LeeRiskClass = when {
    score <= 0 -> LeeRiskClass.LOW
    score == 1 -> LeeRiskClass.MODERATE
    score == 2 -> LeeRiskClass.HIGH
    else -> LeeRiskClass.VERY_HIGH
}
