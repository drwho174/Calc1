package com.drwho174.calc1.domain

/**
 * Идеальная масса тела: 50 кг для мужчин и 45.5 кг для женщин,
 * далее 0.91 * (рост − 152.4).
 */
fun idealBodyMassKg(heightCm: Double, isFemale: Boolean): Double {
    val base = if (isFemale) 45.5 else 50.0
    return base + 0.91 * (heightCm - 152.4)
}
