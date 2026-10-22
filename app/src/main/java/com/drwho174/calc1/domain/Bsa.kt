package com.drwho174.calc1.domain

import kotlin.math.pow

/** Площадь поверхности тела, три используемые в приложении формулы. */
object Bsa {

    /** Мостлер: 0.0167 * sqrt(h) * sqrt(m). */
    fun mosteller(heightCm: Double, weightKg: Double): Double =
        0.0167 * heightCm.pow(0.5) * weightKg.pow(0.5)

    /** Дюбуа: 0.007184 * h^0.725 * m^0.425. */
    fun dubois(heightCm: Double, weightKg: Double): Double =
        0.007184 * heightCm.pow(0.725) * weightKg.pow(0.425)

    /** Хайкок: 0.024265 * h^0.3964 * m^0.5378. */
    fun haycock(heightCm: Double, weightKg: Double): Double =
        0.024265 * heightCm.pow(0.3964) * weightKg.pow(0.5378)
}
