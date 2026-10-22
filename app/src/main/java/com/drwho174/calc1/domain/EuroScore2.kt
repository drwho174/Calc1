package com.drwho174.calc1.domain

import kotlin.math.E
import kotlin.math.pow

/** Почечная дисфункция в EuroSCORE II. */
enum class RenalDisease(val coefficient: Double) {
    NONE(0.0),
    MILD(0.303553),
    SEVERE(0.8592256),
    DIALYSIS(0.6421508),
}

/** Класс NYHA. */
enum class NyhaClass(val coefficient: Double) {
    I(0.0),
    II(0.1070545),
    III(0.2958358),
    IV(0.5597929),
}

/** Фракция выброса. */
enum class EjectionFraction(val coefficient: Double) {
    GOOD(0.0),
    MODERATE(0.3150652),
    POOR(0.8084096),
    VERY_POOR(0.9346919),
}

/** Давление в лёгочной артерии. */
enum class PulmonaryArteryPressure(val coefficient: Double) {
    LOW(0.0),
    MODERATE(0.1788899),
    HIGH(0.3491475),
}

/** Срочность операции. */
enum class Urgency(val coefficient: Double) {
    ELECTIVE(0.0),
    URGENT(0.3174673),
    EMERGENCY(0.7039121),
    SALVAGE(1.362947),
}

/** Вес процедуры. */
enum class ProcedureWeight(val coefficient: Double) {
    CABG(0.0),
    NON_CABG(0.0062118),
    TWO_PROCEDURES(0.5521478),
    THREE_PROCEDURES(0.9724533),
}

data class EuroScore2Input(
    val ageYears: Double,
    val isFemale: Boolean,
    val diabetes: Boolean = false,
    val pulmonaryDysfunction: Boolean = false,
    val poorMobility: Boolean = false,
    val renalDisease: RenalDisease = RenalDisease.NONE,
    val criticalPreoperativeState: Boolean = false,
    val nyha: NyhaClass = NyhaClass.I,
    val anginaClass4: Boolean = false,
    val extracardiacArteriopathy: Boolean = false,
    val previousCardiacSurgery: Boolean = false,
    val activeEndocarditis: Boolean = false,
    val ejectionFraction: EjectionFraction = EjectionFraction.GOOD,
    val recentMyocardialInfarction: Boolean = false,
    val pulmonaryArteryPressure: PulmonaryArteryPressure = PulmonaryArteryPressure.LOW,
    val urgency: Urgency = Urgency.ELECTIVE,
    val procedureWeight: ProcedureWeight = ProcedureWeight.CABG,
    val aortaSurgery: Boolean = false,
)

/** Почечная дисфункция по значению СКФ, как её выставляет диалог СКФ. */
fun renalDiseaseFor(gfr: Double, dialysis: Boolean): RenalDisease = when {
    dialysis -> RenalDisease.DIALYSIS
    gfr > 85.0 -> RenalDisease.NONE
    gfr >= 51.0 -> RenalDisease.MILD
    else -> RenalDisease.SEVERE
}

/** Возрастной коэффициент: 1.0 до 60 лет, дальше «возраст − 59». */
fun euroScore2AgeFactor(ageYears: Double): Double =
    if (ageYears <= 60.0) 1.0 else ageYears - 60.0 + 1.0

/** Сумма коэффициентов риска (без свободного члена −5.324537). */
fun euroScore2OtherFactors(input: EuroScore2Input): Double {
    var sum = 0.0285181 * euroScore2AgeFactor(input.ageYears)

    if (input.isFemale) sum += 0.2196434
    if (input.diabetes) sum += 0.3542749
    if (input.pulmonaryDysfunction) sum += 0.1886564
    if (input.poorMobility) sum += 0.2407181
    sum += input.renalDisease.coefficient
    if (input.criticalPreoperativeState) sum += 1.086517
    sum += input.nyha.coefficient
    if (input.anginaClass4) sum += 0.2226147
    if (input.extracardiacArteriopathy) sum += 0.5360268
    if (input.previousCardiacSurgery) sum += 1.118599
    if (input.activeEndocarditis) sum += 0.6194522
    sum += input.ejectionFraction.coefficient
    if (input.recentMyocardialInfarction) sum += 0.1528943
    sum += input.pulmonaryArteryPressure.coefficient
    sum += input.urgency.coefficient
    sum += input.procedureWeight.coefficient
    if (input.aortaSurgery) sum += 0.6527205

    return sum
}

/** Сумма факторов: otherFactors − 5.324537. */
fun euroScore2FactorsSum(input: EuroScore2Input): Double =
    euroScore2OtherFactors(input) - 5.324537

/**
 * Прогнозируемая смертность, %:
 * 100 * e^sum / (1 + e^sum).
 */
fun euroScore2PredictedMortality(input: EuroScore2Input): Double {
    val sum = euroScore2FactorsSum(input)
    val exponent = E.pow(sum)
    return 100 * (exponent / (1.0 + exponent))
}
