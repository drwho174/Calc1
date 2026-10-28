package com.drwho174.calc1.navigation

import androidx.annotation.StringRes
import androidx.navigation3.runtime.NavKey
import com.drwho174.calc1.R
import kotlinx.serialization.Serializable

/**
 * Ключи навигации Navigation 3.
 *
 * Все ключи реализуют [NavKey] и помечены `@Serializable` — это требование
 * `rememberNavBackStack`, чтобы стек переживал пересоздание Activity
 * и смерть процесса.
 */
@Serializable
sealed interface CalcRoute : NavKey

@Serializable
data object MainMenuRoute : CalcRoute

@Serializable
data object SettingsRoute : CalcRoute

@Serializable
data object AppAboutRoute : CalcRoute

@Serializable
data class CalculatorRoute(val id: CalculatorId) : CalcRoute

@Serializable
data class AboutRoute(val assetName: String) : CalcRoute

@Serializable
enum class CalculatorId {
    EUROSCORE,
    HB_PERFUSION,
    PERFUSIOLOGIST,
    OXYGEN,
    GFR,
    IDEAL_BODY_MASS,
    BSA,
    RESPIRATORY,
    MEHRAN,
    CHARLSON,
    SOFA,
    EL_GANZOURI,
    ARISCAT,
    LEE_RCRI,
}

data class CalculatorDestination(
    val id: CalculatorId,
    @StringRes val titleRes: Int,
    @StringRes val subtitleRes: Int,
    val aboutAsset: String,
)

/**
 * Единственный источник главного меню: и вкладка «Все», и «Избранное»
 * строятся из этого списка, поэтому новый калькулятор добавляется одной
 * записью — правки в UI не нужны.
 *
 * Порядок записей — порядок карточек на вкладке «Все».
 */
val calculatorDestinations: List<CalculatorDestination> = listOf(
    CalculatorDestination(
        CalculatorId.EUROSCORE,
        R.string.name_euroscore_2,
        R.string.subtitle_euroscore_2,
        "EUROSCORE_2.md",
    ),
    CalculatorDestination(
        CalculatorId.HB_PERFUSION,
        R.string.name_hb_on_perfusion,
        R.string.subtitle_hb_on_perfusion,
        "Hb_On_Perfusion.md",
    ),
    CalculatorDestination(
        CalculatorId.PERFUSIOLOGIST,
        R.string.name_perfusion_calculator,
        R.string.subtitle_perfusion_calculator,
        "PerfusiologistCalc.md",
    ),
    CalculatorDestination(
        CalculatorId.OXYGEN,
        R.string.name_oxygen_delivery,
        R.string.subtitle_oxygen_delivery,
        "Oxygen_Delivery.md",
    ),
    CalculatorDestination(
        CalculatorId.GFR,
        R.string.name_glomerular_filtration_rate,
        R.string.subtitle_glomerular_filtration_rate,
        "GFR_about.md",
    ),
    CalculatorDestination(
        CalculatorId.IDEAL_BODY_MASS,
        R.string.name_ideal_body_mass,
        R.string.subtitle_ideal_body_mass,
        "Ideal_Body_Mass.md",
    ),
    CalculatorDestination(
        CalculatorId.BSA,
        R.string.name_body_surface_area,
        R.string.subtitle_body_surface_area,
        "Body_Surface_Area.md",
    ),
    CalculatorDestination(
        CalculatorId.RESPIRATORY,
        R.string.name_oxygenation_params,
        R.string.subtitle_oxygenation_params,
        "RespiratoryIndex.md",
    ),
    CalculatorDestination(
        CalculatorId.MEHRAN,
        R.string.name_mehran_score,
        R.string.subtitle_mehran_score,
        "MehraneScore.md",
    ),
    CalculatorDestination(
        CalculatorId.CHARLSON,
        R.string.name_charlson_score,
        R.string.subtitle_charlson_score,
        "Charlson_Comorbidity_Index.md",
    ),
    CalculatorDestination(
        CalculatorId.SOFA,
        R.string.name_sofa_score,
        R.string.subtitle_sofa_score,
        "SOFA.md",
    ),
    CalculatorDestination(
        CalculatorId.EL_GANZOURI,
        R.string.name_el_ganzouri,
        R.string.subtitle_el_ganzouri,
        "El_Ganzouri.md",
    ),
    CalculatorDestination(
        CalculatorId.ARISCAT,
        R.string.name_ariscat,
        R.string.subtitle_ariscat,
        "ARISCAT.md",
    ),
    CalculatorDestination(
        CalculatorId.LEE_RCRI,
        R.string.name_lee_rcri,
        R.string.subtitle_lee_rcri,
        "Lee_RCRI.md",
    ),
)

fun CalculatorId.destination(): CalculatorDestination =
    calculatorDestinations.first { it.id == this }

/** Ключ калькулятора в настройках: имя константы переживает перестановки в enum. */
val CalculatorId.storageKey: String get() = name

/** Обратное преобразование сохранённого набора: неизвестные ключи отбрасываются. */
fun calculatorIdsOf(stored: Set<String>): Set<CalculatorId> =
    CalculatorId.entries.filterTo(LinkedHashSet()) { it.storageKey in stored }
