package com.drwho174.calc1.domain

/**
 * Доставка кислорода и производные индексы.
 *
 * Все входные значения приходят из UI уже проверенными, поэтому функции
 * не бросают исключений на пустых строках.
 */
data class OxygenDeliveryInput(
    val bsaM2: Double,
    val cardiacOutputLMin: Double,
    val hemoglobinGPerL: Double,
    val spo2Percent: Double,
    val pao2MmHg: Double,
    val etco2Percent: Double,
    val freshGasFlowLMin: Double,
)

data class OxygenDeliveryResult(
    /** Артериальное содержание O2, мл/дл. */
    val arterialOxygenContent: Double,
    /** DO2, мл/мин. */
    val oxygenDelivery: Double,
    /** DO2i, мл/мин/м². */
    val oxygenDeliveryIndex: Double,
    /** VCO2i, мл/мин. */
    val co2ProductionIndex: Double,
    /** DO2i / VCO2i. */
    val do2ToVco2Ratio: Double,
    /** Порог из приложения: DO2i > 270 считается достаточным. */
    val oxygenDeliveryIsAdequate: Boolean,
    /** Порог из приложения: DO2i/VCO2i >= 5 считается достаточным. */
    val ratioIsAdequate: Boolean,
)

fun calculateOxygenDelivery(input: OxygenDeliveryInput): OxygenDeliveryResult {
    val arterialOxygenContent =
        (input.hemoglobinGPerL / 10 * 1.34 * input.spo2Percent / 100) +
            (input.pao2MmHg * 0.003)

    val oxygenDelivery = input.cardiacOutputLMin * arterialOxygenContent * 10
    val oxygenDeliveryIndex = oxygenDelivery / input.bsaM2
    val co2ProductionIndex =
        (input.etco2Percent * input.freshGasFlowLMin * 1000) / (input.bsaM2 * 760)
    val ratio = oxygenDeliveryIndex / co2ProductionIndex

    return OxygenDeliveryResult(
        arterialOxygenContent = arterialOxygenContent,
        oxygenDelivery = oxygenDelivery,
        oxygenDeliveryIndex = oxygenDeliveryIndex,
        co2ProductionIndex = co2ProductionIndex,
        do2ToVco2Ratio = ratio,
        oxygenDeliveryIsAdequate = oxygenDeliveryIndex > 270.0,
        ratioIsAdequate = ratio >= 5,
    )
}
