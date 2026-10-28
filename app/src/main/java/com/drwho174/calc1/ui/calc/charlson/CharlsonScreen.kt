package com.drwho174.calc1.ui.calc.charlson

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drwho174.calc1.R
import com.drwho174.calc1.domain.CharlsonCondition
import com.drwho174.calc1.domain.CharlsonInput
import com.drwho174.calc1.domain.CharlsonSeverity
import com.drwho174.calc1.domain.charlsonIndex
import com.drwho174.calc1.domain.charlsonSeverity
import com.drwho174.calc1.domain.charlsonTenYearSurvival
import com.drwho174.calc1.ui.common.format
import com.drwho174.calc1.ui.common.pointsLabel
import com.drwho174.calc1.ui.common.toNumberOrNull
import com.drwho174.calc1.ui.components.BottomResultBar
import com.drwho174.calc1.ui.components.ExplainedCheckboxRow
import com.drwho174.calc1.ui.components.NumberField
import com.drwho174.calc1.ui.components.ResultTone
import com.drwho174.calc1.ui.components.RiskBadge
import com.drwho174.calc1.ui.theme.Calc1Theme

/** Строка критерия: состояние, название и пояснение. */
private data class ConditionRow(
    val condition: CharlsonCondition,
    val labelRes: Int,
    val infoRes: Int,
)

private val conditionRows = listOf(
    ConditionRow(
        CharlsonCondition.MYOCARDIAL_INFARCTION,
        R.string.charlson_myocardial_infarction,
        R.string.charlson_myocardial_infarction_info,
    ),
    ConditionRow(
        CharlsonCondition.HEART_FAILURE,
        R.string.charlson_heart_failure,
        R.string.charlson_heart_failure_info,
    ),
    ConditionRow(
        CharlsonCondition.PERIPHERAL_VASCULAR_DISEASE,
        R.string.charlson_peripheral_vascular,
        R.string.charlson_peripheral_vascular_info,
    ),
    ConditionRow(
        CharlsonCondition.CEREBROVASCULAR_DISEASE,
        R.string.charlson_cerebrovascular,
        R.string.charlson_cerebrovascular_info,
    ),
    ConditionRow(
        CharlsonCondition.DEMENTIA,
        R.string.charlson_dementia,
        R.string.charlson_dementia_info,
    ),
    ConditionRow(
        CharlsonCondition.CHRONIC_PULMONARY_DISEASE,
        R.string.charlson_pulmonary,
        R.string.charlson_pulmonary_info,
    ),
    ConditionRow(
        CharlsonCondition.CONNECTIVE_TISSUE_DISEASE,
        R.string.charlson_connective_tissue,
        R.string.charlson_connective_tissue_info,
    ),
    ConditionRow(
        CharlsonCondition.PEPTIC_ULCER_DISEASE,
        R.string.charlson_ulcer,
        R.string.charlson_ulcer_info,
    ),
    ConditionRow(
        CharlsonCondition.MILD_LIVER_DISEASE,
        R.string.charlson_mild_liver,
        R.string.charlson_mild_liver_info,
    ),
    ConditionRow(
        CharlsonCondition.DIABETES,
        R.string.charlson_diabetes,
        R.string.charlson_diabetes_info,
    ),
    ConditionRow(
        CharlsonCondition.HEMIPLEGIA,
        R.string.charlson_hemiplegia,
        R.string.charlson_hemiplegia_info,
    ),
    ConditionRow(
        CharlsonCondition.MODERATE_RENAL_DISEASE,
        R.string.charlson_renal,
        R.string.charlson_renal_info,
    ),
    ConditionRow(
        CharlsonCondition.DIABETES_WITH_END_ORGAN_DAMAGE,
        R.string.charlson_diabetes_end_organ,
        R.string.charlson_diabetes_end_organ_info,
    ),
    ConditionRow(
        CharlsonCondition.SOLID_TUMOR,
        R.string.charlson_solid_tumor,
        R.string.charlson_solid_tumor_info,
    ),
    ConditionRow(
        CharlsonCondition.LEUKEMIA,
        R.string.charlson_leukemia,
        R.string.charlson_leukemia_info,
    ),
    ConditionRow(
        CharlsonCondition.LYMPHOMA,
        R.string.charlson_lymphoma,
        R.string.charlson_lymphoma_info,
    ),
    ConditionRow(
        CharlsonCondition.MODERATE_SEVERE_LIVER_DISEASE,
        R.string.charlson_moderate_liver,
        R.string.charlson_moderate_liver_info,
    ),
    ConditionRow(
        CharlsonCondition.METASTATIC_TUMOR,
        R.string.charlson_metastatic,
        R.string.charlson_metastatic_info,
    ),
    ConditionRow(
        CharlsonCondition.AIDS,
        R.string.charlson_aids,
        R.string.charlson_aids_info,
    ),
)

/**
 * Шкала коморбидности Чарлсона: 19 состояний, возрастная поправка
 * и 10-летняя выживаемость.
 */
@Composable
fun CharlsonScreen(modifier: Modifier = Modifier) {
    var age by rememberSaveable { mutableStateOf("") }
    var selectedNames by rememberSaveable { mutableStateOf(emptySet<String>()) }

    val ageYears = age.toNumberOrNull()
    val conditions = selectedNames.mapNotNullTo(LinkedHashSet()) { name ->
        CharlsonCondition.entries.firstOrNull { it.name == name }
    }
    val index = ageYears?.let { charlsonIndex(CharlsonInput(conditions, it.toInt())) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            NumberField(
                value = age,
                onValueChange = { age = it },
                label = stringResource(R.string.charlson_age),
                suffix = stringResource(R.string.unit_years),
                hintText = stringResource(R.string.charlson_age_info),
            )

            conditionRows.forEach { row ->
                val checked = row.condition.name in selectedNames
                ExplainedCheckboxRow(
                    label = stringResource(row.labelRes),
                    explanation = stringResource(row.infoRes),
                    checked = checked,
                    onCheckedChange = { on ->
                        selectedNames = if (on) {
                            selectedNames + row.condition.name
                        } else {
                            selectedNames - row.condition.name
                        }
                    },
                )
            }

            // Место под закреплённую панель результата.
            Spacer(Modifier.height(160.dp))
        }

        val dash = "—"
        BottomResultBar(
            title = stringResource(R.string.name_charlson_score),
            value = index?.let { pointsLabel(it) } ?: dash,
            secondary = index?.let {
                stringResource(R.string.bar_ten_year_survival) +
                    " · " + charlsonTenYearSurvival(it).format(0) + " %"
            },
            risk = index?.let {
                val severity = charlsonSeverity(it)
                RiskBadge(
                    label = stringResource(charlsonRiskBadge(severity)),
                    tone = charlsonRiskTone(severity),
                )
            },
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

private fun charlsonRiskBadge(severity: CharlsonSeverity): Int = when (severity) {
    CharlsonSeverity.LOW -> R.string.risk_badge_low
    CharlsonSeverity.MODERATE -> R.string.risk_badge_moderate
    CharlsonSeverity.HIGH -> R.string.risk_badge_high
}

private fun charlsonRiskTone(severity: CharlsonSeverity): ResultTone = when (severity) {
    CharlsonSeverity.LOW -> ResultTone.Positive
    CharlsonSeverity.MODERATE -> ResultTone.Neutral
    CharlsonSeverity.HIGH -> ResultTone.Negative
}

@Preview(showBackground = true)
@Composable
private fun CharlsonScreenPreview() {
    Calc1Theme {
        CharlsonScreen()
    }
}
