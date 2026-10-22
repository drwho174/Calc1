package com.drwho174.calc1.domain

import kotlin.math.pow

/**
 * Расчёт гемоглобина на перфузии по Allen и Nadler.
 *
 * Формулы перенесены из HbPerfusion.kt без изменений, включая коэффициенты
 * 0.045 / 0.0328 и деление объёмов на 1000 (литры).
 */
data class HbPerfusionInput(
    val heightCm: Double,
    val weightKg: Double,
    val isFemale: Boolean,
    /** Исходный гемоглобин, г/л. */
    val hemoglobinGPerL: Double,
    /** Инфузия, мл. */
    val infusionMl: Double,
    /** Эксфузия, мл. */
    val exfusionMl: Double,
    /** Диурез, мл. */
    val diuresisMl: Double,
    /** Забор на ИК, мл. */
    val cpbExfusionMl: Double,
    /** Прайм, мл. */
    val primeMl: Double,
)

data class HbPerfusionResult(
    val beforeCpbAllen: Double,
    val afterCpbAllen: Double,
    val beforeCpbNadler: Double,
    val afterCpbNadler: Double,
)

fun calculateHbPerfusion(input: HbPerfusionInput): HbPerfusionResult {
    fun allen(): Double {
        val heightM = input.heightCm / 100
        return if (input.isFemale) {
            0.414 * heightM.pow(3) + 0.0328 * input.weightKg - 0.03
        } else {
            0.417 * heightM.pow(3) + 0.045 * input.weightKg - 0.03
        }
    }

    fun nadler(): Double {
        val heightM = input.heightCm / 100
        return if (input.isFemale) {
            (356 * heightM.pow(3) + 33.1 * input.weightKg + 183) / 1000
        } else {
            (367 * heightM.pow(3) + 32.2 * input.weightKg + 604) / 1000
        }
    }

    fun pair(circulatingBloodVolume: Double): Pair<Double, Double> {
        val exfusedHb = input.hemoglobinGPerL * input.exfusionMl / 1000
        val totalHb = input.hemoglobinGPerL * circulatingBloodVolume
        val balance = circulatingBloodVolume -
            input.exfusionMl / 1000 -
            input.diuresisMl / 1000 +
            input.infusionMl / 1000

        val beforeCpb = (totalHb - exfusedHb) / balance
        val cpbExfusedHb = beforeCpb * input.cpbExfusionMl / 1000
        val afterCpb =
            (totalHb - cpbExfusedHb - exfusedHb) /
                (balance - input.cpbExfusionMl / 1000 + input.primeMl / 1000)

        return beforeCpb to afterCpb
    }

    val (beforeAllen, afterAllen) = pair(allen())
    val (beforeNadler, afterNadler) = pair(nadler())

    return HbPerfusionResult(
        beforeCpbAllen = beforeAllen,
        afterCpbAllen = afterAllen,
        beforeCpbNadler = beforeNadler,
        afterCpbNadler = afterNadler,
    )
}
