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
}

data class CalculatorDestination(
    val id: CalculatorId,
    @StringRes val titleRes: Int,
    val aboutAsset: String,
)

/** Порядок совпадает с порядком кнопок в главном меню. */
val calculatorDestinations: List<CalculatorDestination> = listOf(
    CalculatorDestination(
        CalculatorId.EUROSCORE,
        R.string.name_euroscore_2,
        "EUROSCORE_2.md",
    ),
    CalculatorDestination(
        CalculatorId.HB_PERFUSION,
        R.string.name_hb_on_perfusion,
        "Hb_On_Perfusion.md",
    ),
    CalculatorDestination(
        CalculatorId.PERFUSIOLOGIST,
        R.string.name_perfusion_calculator,
        "PerfusiologistCalc.md",
    ),
    CalculatorDestination(
        CalculatorId.OXYGEN,
        R.string.name_oxygen_delivery,
        "Oxygen_Delivery.md",
    ),
    CalculatorDestination(
        CalculatorId.GFR,
        R.string.name_glomerular_filtration_rate,
        "GFR_about.md",
    ),
    CalculatorDestination(
        CalculatorId.IDEAL_BODY_MASS,
        R.string.name_ideal_body_mass,
        "Ideal_Body_Mass.md",
    ),
    CalculatorDestination(
        CalculatorId.BSA,
        R.string.name_body_surface_area,
        "Body_Surface_Area.md",
    ),
    CalculatorDestination(
        CalculatorId.RESPIRATORY,
        R.string.name_oxygenation_params,
        "RespiratoryIndex.md",
    ),
    CalculatorDestination(
        CalculatorId.MEHRAN,
        R.string.name_mehran_score,
        "MehraneScore.md",
    ),
)

fun CalculatorId.destination(): CalculatorDestination =
    calculatorDestinations.first { it.id == this }
