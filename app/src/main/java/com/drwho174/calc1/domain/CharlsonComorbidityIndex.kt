package com.drwho174.calc1.domain

/**
 * Индекс коморбидности Чарлсона (Charlson ME et al., 1987).
 *
 * Веса взяты из оригинальной публикации: 1/2/3/6 баллов за состояние,
 * плюс возрастная поправка (+1 балл за каждую декаду после 50 лет).
 */
enum class CharlsonCondition(val points: Int) {
    MYOCARDIAL_INFARCTION(1),
    HEART_FAILURE(1),
    PERIPHERAL_VASCULAR_DISEASE(1),
    CEREBROVASCULAR_DISEASE(1),
    DEMENTIA(1),
    CHRONIC_PULMONARY_DISEASE(1),
    CONNECTIVE_TISSUE_DISEASE(1),
    PEPTIC_ULCER_DISEASE(1),
    MILD_LIVER_DISEASE(1),
    DIABETES(1),
    HEMIPLEGIA(2),
    MODERATE_RENAL_DISEASE(2),
    DIABETES_WITH_END_ORGAN_DAMAGE(2),
    SOLID_TUMOR(2),
    LEUKEMIA(2),
    LYMPHOMA(2),
    MODERATE_SEVERE_LIVER_DISEASE(3),
    METASTATIC_TUMOR(6),
    AIDS(6),
}

/** Возрастная поправка: по одному баллу за каждую декаду начиная с 50 лет. */
fun charlsonAgePoints(age: Int): Int = when {
    age < 50 -> 0
    else -> (age - 50) / 10 + 1
}

data class CharlsonInput(
    val conditions: Set<CharlsonCondition>,
    val age: Int,
)

/** Сумма баллов: состояния плюс возрастная поправка. */
fun charlsonIndex(input: CharlsonInput): Int =
    input.conditions.sumOf { it.points } + charlsonAgePoints(input.age)

/**
 * Ожидаемая 10-летняя выживаемость, % (Charlson 1987).
 *
 * Для индекса 6 и выше в публикации даётся одно значение — 2 %.
 */
fun charlsonTenYearSurvival(index: Int): Double = when {
    index <= 0 -> 99.0
    index == 1 -> 96.0
    index == 2 -> 90.0
    index == 3 -> 77.0
    index == 4 -> 53.0
    index == 5 -> 21.0
    else -> 2.0
}

/** Качественная градация коморбидности для окраски результата. */
enum class CharlsonSeverity { LOW, MODERATE, HIGH }

fun charlsonSeverity(index: Int): CharlsonSeverity = when {
    index <= 2 -> CharlsonSeverity.LOW
    index <= 4 -> CharlsonSeverity.MODERATE
    else -> CharlsonSeverity.HIGH
}
