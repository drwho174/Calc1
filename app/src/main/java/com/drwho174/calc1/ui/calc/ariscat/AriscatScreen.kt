package com.drwho174.calc1.ui.calc.ariscat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drwho174.calc1.R
import com.drwho174.calc1.domain.AriscatAge
import com.drwho174.calc1.domain.AriscatDuration
import com.drwho174.calc1.domain.AriscatIncision
import com.drwho174.calc1.domain.AriscatInput
import com.drwho174.calc1.domain.AriscatRisk
import com.drwho174.calc1.domain.AriscatSaturation
import com.drwho174.calc1.domain.ariscatRisk
import com.drwho174.calc1.domain.ariscatScore
import com.drwho174.calc1.ui.components.ChoiceGroup
import com.drwho174.calc1.ui.components.ExplainedCheckboxRow
import com.drwho174.calc1.ui.components.ResultGrid
import com.drwho174.calc1.ui.components.ResultItem
import com.drwho174.calc1.ui.components.ResultTone
import com.drwho174.calc1.ui.theme.Calc1Theme

private fun ageLabel(value: AriscatAge): Int = when (value) {
    AriscatAge.UP_TO_50 -> R.string.ariscat_age_0
    AriscatAge.FROM_51_TO_80 -> R.string.ariscat_age_1
    AriscatAge.ABOVE_80 -> R.string.ariscat_age_2
}

private fun saturationLabel(value: AriscatSaturation): Int = when (value) {
    AriscatSaturation.AT_LEAST_96 -> R.string.ariscat_saturation_0
    AriscatSaturation.FROM_91_TO_95 -> R.string.ariscat_saturation_1
    AriscatSaturation.UP_TO_90 -> R.string.ariscat_saturation_2
}

private fun incisionLabel(value: AriscatIncision): Int = when (value) {
    AriscatIncision.PERIPHERAL -> R.string.ariscat_incision_0
    AriscatIncision.UPPER_ABDOMINAL -> R.string.ariscat_incision_1
    AriscatIncision.INTRATHORACIC -> R.string.ariscat_incision_2
}

private fun durationLabel(value: AriscatDuration): Int = when (value) {
    AriscatDuration.BELOW_TWO_HOURS -> R.string.ariscat_duration_0
    AriscatDuration.FROM_TWO_TO_THREE_HOURS -> R.string.ariscat_duration_1
    AriscatDuration.ABOVE_THREE_HOURS -> R.string.ariscat_duration_2
}

/** ARISCAT: семь факторов послеоперационных лёгочных осложнений. */
@Composable
fun AriscatScreen(modifier: Modifier = Modifier) {
    var age by rememberSaveable { mutableStateOf(AriscatAge.UP_TO_50) }
    var saturation by rememberSaveable { mutableStateOf(AriscatSaturation.AT_LEAST_96) }
    var respiratoryInfection by rememberSaveable { mutableStateOf(false) }
    var anemia by rememberSaveable { mutableStateOf(false) }
    var incision by rememberSaveable { mutableStateOf(AriscatIncision.PERIPHERAL) }
    var duration by rememberSaveable { mutableStateOf(AriscatDuration.BELOW_TWO_HOURS) }
    var emergency by rememberSaveable { mutableStateOf(false) }

    val score = ariscatScore(
        AriscatInput(
            age = age,
            saturation = saturation,
            respiratoryInfectionLastMonth = respiratoryInfection,
            anemia = anemia,
            incision = incision,
            duration = duration,
            emergency = emergency,
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ChoiceGroup(
            title = stringResource(R.string.ariscat_age),
            explanation = stringResource(R.string.ariscat_age_info),
            options = AriscatAge.entries,
            selected = age,
            onSelect = { age = it },
            label = { stringResource(ageLabel(it)) },
        )
        ChoiceGroup(
            title = stringResource(R.string.ariscat_saturation),
            explanation = stringResource(R.string.ariscat_saturation_info),
            options = AriscatSaturation.entries,
            selected = saturation,
            onSelect = { saturation = it },
            label = { stringResource(saturationLabel(it)) },
        )
        ExplainedCheckboxRow(
            label = stringResource(R.string.ariscat_infection),
            explanation = stringResource(R.string.ariscat_infection_info),
            checked = respiratoryInfection,
            onCheckedChange = { respiratoryInfection = it },
        )
        ExplainedCheckboxRow(
            label = stringResource(R.string.ariscat_anemia),
            explanation = stringResource(R.string.ariscat_anemia_info),
            checked = anemia,
            onCheckedChange = { anemia = it },
        )
        ChoiceGroup(
            title = stringResource(R.string.ariscat_incision),
            explanation = stringResource(R.string.ariscat_incision_info),
            options = AriscatIncision.entries,
            selected = incision,
            onSelect = { incision = it },
            label = { stringResource(incisionLabel(it)) },
        )
        ChoiceGroup(
            title = stringResource(R.string.ariscat_duration),
            explanation = stringResource(R.string.ariscat_duration_info),
            options = AriscatDuration.entries,
            selected = duration,
            onSelect = { duration = it },
            label = { stringResource(durationLabel(it)) },
        )
        ExplainedCheckboxRow(
            label = stringResource(R.string.ariscat_emergency),
            explanation = stringResource(R.string.ariscat_emergency_info),
            checked = emergency,
            onCheckedChange = { emergency = it },
        )

        ResultGrid(
            results = listOf(
                ResultItem(
                    title = stringResource(R.string.result_score),
                    value = score.toString(),
                ),
                ResultItem(
                    title = stringResource(R.string.result_risk),
                    value = stringResource(ariscatRiskLabel(ariscatRisk(score))),
                    tone = ariscatRiskTone(ariscatRisk(score)),
                ),
            ),
            columns = 1,
        )
    }
}

private fun ariscatRiskLabel(risk: AriscatRisk): Int = when (risk) {
    AriscatRisk.LOW -> R.string.risk_low
    AriscatRisk.MODERATE -> R.string.risk_moderate
    AriscatRisk.HIGH -> R.string.risk_high
}

private fun ariscatRiskTone(risk: AriscatRisk): ResultTone = when (risk) {
    AriscatRisk.LOW -> ResultTone.Positive
    AriscatRisk.MODERATE -> ResultTone.Neutral
    AriscatRisk.HIGH -> ResultTone.Negative
}

@Preview(showBackground = true)
@Composable
private fun AriscatScreenPreview() {
    Calc1Theme {
        AriscatScreen()
    }
}
