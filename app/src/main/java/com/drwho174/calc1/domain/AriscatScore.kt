package com.drwho174.calc1.domain

/**
 * ARISCAT (Canet J et al., 2010) — риск послеоперационных лёгочных осложнений
 * у взрослых после некардиохирургических вмешательств.
 *
 * Семь факторов, сумма 0–123 балла.
 */
enum class AriscatAge(val points: Int) { UP_TO_50(0), FROM_51_TO_80(3), ABOVE_80(16) }

enum class AriscatSaturation(val points: Int) { AT_LEAST_96(0), FROM_91_TO_95(8), UP_TO_90(24) }

enum class AriscatIncision(val points: Int) { PERIPHERAL(0), UPPER_ABDOMINAL(15), INTRATHORACIC(24) }

enum class AriscatDuration(val points: Int) { BELOW_TWO_HOURS(0), FROM_TWO_TO_THREE_HOURS(16), ABOVE_THREE_HOURS(23) }

data class AriscatInput(
    val age: AriscatAge = AriscatAge.UP_TO_50,
    val saturation: AriscatSaturation = AriscatSaturation.AT_LEAST_96,
    val respiratoryInfectionLastMonth: Boolean = false,
    val anemia: Boolean = false,
    val incision: AriscatIncision = AriscatIncision.PERIPHERAL,
    val duration: AriscatDuration = AriscatDuration.BELOW_TWO_HOURS,
    val emergency: Boolean = false,
)

/** Баллы за респираторную инфекцию за последний месяц. */
const val ARISCAT_RESPIRATORY_INFECTION_POINTS: Int = 17

/** Баллы за предоперационную анемию (Hb ≤ 10 г/дл). */
const val ARISCAT_ANEMIA_POINTS: Int = 11

/** Баллы за экстренное вмешательство. */
const val ARISCAT_EMERGENCY_POINTS: Int = 8

/** Сумма баллов ARISCAT. */
fun ariscatScore(input: AriscatInput): Int =
    input.age.points +
        input.saturation.points +
        (if (input.respiratoryInfectionLastMonth) ARISCAT_RESPIRATORY_INFECTION_POINTS else 0) +
        (if (input.anemia) ARISCAT_ANEMIA_POINTS else 0) +
        input.incision.points +
        input.duration.points +
        (if (input.emergency) ARISCAT_EMERGENCY_POINTS else 0)

/** Градация риска: < 26 низкий, 26–44 средний, ≥ 45 высокий. */
enum class AriscatRisk { LOW, MODERATE, HIGH }

fun ariscatRisk(score: Int): AriscatRisk = when {
    score < 26 -> AriscatRisk.LOW
    score < 45 -> AriscatRisk.MODERATE
    else -> AriscatRisk.HIGH
}
