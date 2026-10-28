package com.drwho174.calc1.domain

/**
 * Индекс Эль-Ганзури (el-Ganzouri AR et al., 1996) — предоперационная оценка
 * риска трудной интубации по семи доступным критериям.
 *
 * Сумма — от 0 до 11 баллов.
 */
enum class MallampatiClass(val points: Int) { FIRST_SECOND(0), THIRD(1), FOURTH(2) }

enum class ThyromentalDistance(val points: Int) { AT_LEAST_SIX_HALF(0), FROM_SIX(1), BELOW_SIX(2) }

enum class MouthOpening(val points: Int) { AT_LEAST_FIVE(0), BELOW_FIVE(1) }

enum class NeckMovement(val points: Int) { ABOVE_NINETY(0), FROM_EIGHTY(1), BELOW_EIGHTY(2) }

enum class Prognathia(val points: Int) { POSSIBLE(0), IMPOSSIBLE(1) }

enum class BodyWeightBand(val points: Int) { BELOW_110(0), FROM_110_TO_120(1), ABOVE_120(2) }

enum class DifficultIntubationHistory(val points: Int) { ABSENT(0), PRESENT(1) }

data class ElGanzouriInput(
    val mallampati: MallampatiClass = MallampatiClass.FIRST_SECOND,
    val thyromental: ThyromentalDistance = ThyromentalDistance.AT_LEAST_SIX_HALF,
    val mouthOpening: MouthOpening = MouthOpening.AT_LEAST_FIVE,
    val neckMovement: NeckMovement = NeckMovement.ABOVE_NINETY,
    val prognathia: Prognathia = Prognathia.POSSIBLE,
    val weight: BodyWeightBand = BodyWeightBand.BELOW_110,
    val difficultIntubation: DifficultIntubationHistory = DifficultIntubationHistory.ABSENT,
)

/** Сумма баллов риска трудной интубации, 0–11. */
fun elGanzouriScore(input: ElGanzouriInput): Int =
    input.mallampati.points +
        input.thyromental.points +
        input.mouthOpening.points +
        input.neckMovement.points +
        input.prognathia.points +
        input.weight.points +
        input.difficultIntubation.points

/** Класс риска: 0–2 низкий, 3–4 средний, 5 и выше высокий. */
enum class AirwayRiskClass { LOW, MODERATE, HIGH }

fun elGanzouriRiskClass(score: Int): AirwayRiskClass = when {
    score <= 2 -> AirwayRiskClass.LOW
    score <= 4 -> AirwayRiskClass.MODERATE
    else -> AirwayRiskClass.HIGH
}
