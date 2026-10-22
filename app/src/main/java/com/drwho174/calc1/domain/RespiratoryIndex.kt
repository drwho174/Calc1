package com.drwho174.calc1.domain

/** Атмосферное давление, мм рт. ст. */
const val ATMOSPHERE_PRESSURE = 760.0

/** Давление насыщенного водяного пара при 37 °C, мм рт. ст. */
const val WATER_STEAM_PRESSURE = 47.0

/** A-a градиент: FiO2/100 * (760 − 47) − PaCO2/0.8 − PaO2. */
fun aaGradient(fio2Percent: Double, pao2MmHg: Double, paco2MmHg: Double): Double =
    (fio2Percent / 100 * (ATMOSPHERE_PRESSURE - WATER_STEAM_PRESSURE) -
        (paco2MmHg / 0.8)) - pao2MmHg

/** Респираторный индекс: A-a градиент / PaO2. */
fun respiratoryIndex(fio2Percent: Double, pao2MmHg: Double, paco2MmHg: Double): Double =
    aaGradient(fio2Percent, pao2MmHg, paco2MmHg) / pao2MmHg

/** P/F ratio: PaO2 * 100 / FiO2. */
fun pfRatio(fio2Percent: Double, pao2MmHg: Double): Double =
    pao2MmHg * 100 / fio2Percent

/** S/F ratio: SpO2 * 100 / FiO2. */
fun sfRatio(fio2Percent: Double, spo2Percent: Double): Double =
    spo2Percent * 100 / fio2Percent
