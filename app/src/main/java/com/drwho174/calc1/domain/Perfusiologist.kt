package com.drwho174.calc1.domain

import kotlin.math.roundToInt

/**
 * Калькулятор перфузиолога: ППТ по Дюбуа, индекс перфузии и связка
 * «скорость ↔ процент».
 *
 * В исходном фрагменте три слайдера подстраивали друг друга через обратную
 * связь; здесь оставлены чистые функции пересчёта, а согласование значений
 * делает UI-слой.
 */
object Perfusiologist {

    /** ППТ по Дюбуа — та же формула, что и в BSA. */
    fun bodySurfaceArea(heightCm: Double, weightKg: Double): Double =
        Bsa.dubois(heightCm, weightKg)

    /** Индекс перфузии = скорость перфузии * ППТ. */
    fun perfusionIndex(perfusionRate: Double, bsaM2: Double): Double =
        perfusionRate * bsaM2

    /** Скорость перфузии = индекс перфузии * процент / 100. */
    fun perfusionSpeed(perfusionIndex: Double, percent: Double): Double =
        perfusionIndex * percent / 100

    /** Процент, соответствующий скорости: speed * 100 / index. */
    fun percentForSpeed(perfusionIndex: Double, speed: Double): Double =
        speed * 100 / perfusionIndex

    /**
     * Округление процента до шага 5, как в слайдере (0..200).
     * Возвращает null, если значение вне диапазона слайдера.
     */
    fun snapPercent(percent: Double): Int? {
        val snapped = (percent / 5).roundToInt() * 5
        return snapped.takeIf { it in 0..200 }
    }

    /**
     * Округление скорости до 0.1, как в слайдере (0..8).
     * Возвращает null, если значение вне диапазона слайдера.
     */
    fun snapSpeed(speed: Double): Double? {
        val snapped = (speed * 10).roundToInt() / 10.0
        return snapped.takeIf { it in 0.0..8.0 }
    }
}
