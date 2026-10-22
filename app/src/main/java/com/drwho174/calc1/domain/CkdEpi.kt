package com.drwho174.calc1.domain

import kotlin.math.pow

/** Пересчёт мкмоль/л в мг/дл, как в формуле CKD-EPI. */
const val CREATININE_UMOL_PER_MG_DL = 88.4055

/**
 * CKD-EPI 2021 (race-free).
 *
 * Поведение 1:1 с прежней реализацией из GFR.kt / CKDDial.kt, включая половой
 * коэффициент: у женщин результат умножается на 1.012, у мужчин — на 1.0.
 */
fun estimateCkdEpi2021(
    creatinineUmolPerL: Double,
    ageYears: Double,
    isFemale: Boolean,
): Double {
    val creatinine = creatinineUmolPerL / CREATININE_UMOL_PER_MG_DL

    val a: Double
    val b: Double
    if (isFemale) {
        a = 0.7
        b = if (creatinine <= 0.7) -0.241 else -1.2
    } else {
        a = 0.9
        b = if (creatinine <= 0.9) -0.302 else -1.2
    }

    val sexCoefficient = if (isFemale) 1.012 else 1.0
    return 142 * (creatinine / a).pow(b) * 0.9938.pow(ageYears) * sexCoefficient
}
