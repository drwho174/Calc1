package com.drwho174.calc1.domain

/**
 * SOFA (Sequential Organ Failure Assessment, Vincent JL et al., 1996).
 *
 * Экран спрашивает худшую категорию по каждому органу за сутки: 0–4 балла.
 * Сумма лежит в диапазоне 0–24.
 */
enum class SofaOrgan { RESPIRATION, COAGULATION, LIVER, CARDIOVASCULAR, CENTRAL_NERVOUS, RENAL }

/** Максимальный балл по одному органу. */
const val SOFA_MAX_ORGAN_POINTS: Int = 4

/** Максимально возможная сумма. */
const val SOFA_MAX_POINTS: Int = 24

/** Сумма баллов по органам; отсутствующие органы считаются нулевыми. */
fun sofaScore(points: Map<SofaOrgan, Int>): Int =
    SofaOrgan.entries.sumOf { (points[it] ?: 0).coerceIn(0, SOFA_MAX_ORGAN_POINTS) }

/**
 * Ориентировочная госпитальная летальность по сумме баллов, %.
 *
 * Диапазоны соответствуют сводной таблице валидационных когорт SOFA
 * (Vincent 1998, Ferreira 2001) и даются как интервал, а не точка.
 */
fun sofaMortalityRange(total: Int): String = when {
    total <= 1 -> "< 9"
    total <= 3 -> "9–16"
    total <= 5 -> "17–24"
    total <= 7 -> "25–33"
    total <= 9 -> "34–43"
    total <= 11 -> "44–53"
    total <= 13 -> "54–63"
    total <= 15 -> "64–73"
    else -> "> 73"
}

/** Условная градация для окраски результата: по тем же диапазонам летальности. */
enum class SofaSeverity { LOW, MODERATE, HIGH, VERY_HIGH }

fun sofaSeverity(total: Int): SofaSeverity = when {
    total <= 3 -> SofaSeverity.LOW
    total <= 7 -> SofaSeverity.MODERATE
    total <= 11 -> SofaSeverity.HIGH
    else -> SofaSeverity.VERY_HIGH
}
