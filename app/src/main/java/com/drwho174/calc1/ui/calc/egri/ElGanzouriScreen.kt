package com.drwho174.calc1.ui.calc.egri

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
import com.drwho174.calc1.ui.common.pointsLabel
import com.drwho174.calc1.domain.AirwayRiskClass
import com.drwho174.calc1.domain.BodyWeightBand
import com.drwho174.calc1.domain.DifficultIntubationHistory
import com.drwho174.calc1.domain.ElGanzouriInput
import com.drwho174.calc1.domain.MallampatiClass
import com.drwho174.calc1.domain.MouthOpening
import com.drwho174.calc1.domain.NeckMovement
import com.drwho174.calc1.domain.Prognathia
import com.drwho174.calc1.domain.ThyromentalDistance
import com.drwho174.calc1.domain.elGanzouriRiskClass
import com.drwho174.calc1.domain.elGanzouriScore
import com.drwho174.calc1.ui.components.BottomResultBar
import com.drwho174.calc1.ui.components.ChoiceGroup
import com.drwho174.calc1.ui.components.ResultTone
import com.drwho174.calc1.ui.components.RiskBadge
import com.drwho174.calc1.ui.theme.Calc1Theme

private fun mallampatiLabel(value: MallampatiClass): Int = when (value) {
    MallampatiClass.FIRST_SECOND -> R.string.egri_mallampati_0
    MallampatiClass.THIRD -> R.string.egri_mallampati_1
    MallampatiClass.FOURTH -> R.string.egri_mallampati_2
}

private fun thyromentalLabel(value: ThyromentalDistance): Int = when (value) {
    ThyromentalDistance.AT_LEAST_SIX_HALF -> R.string.egri_thyromental_0
    ThyromentalDistance.FROM_SIX -> R.string.egri_thyromental_1
    ThyromentalDistance.BELOW_SIX -> R.string.egri_thyromental_2
}

private fun mouthOpeningLabel(value: MouthOpening): Int = when (value) {
    MouthOpening.AT_LEAST_FIVE -> R.string.egri_mouth_0
    MouthOpening.BELOW_FIVE -> R.string.egri_mouth_1
}

private fun neckMovementLabel(value: NeckMovement): Int = when (value) {
    NeckMovement.ABOVE_NINETY -> R.string.egri_neck_0
    NeckMovement.FROM_EIGHTY -> R.string.egri_neck_1
    NeckMovement.BELOW_EIGHTY -> R.string.egri_neck_2
}

private fun prognathiaLabel(value: Prognathia): Int = when (value) {
    Prognathia.POSSIBLE -> R.string.egri_prognathia_0
    Prognathia.IMPOSSIBLE -> R.string.egri_prognathia_1
}

private fun weightLabel(value: BodyWeightBand): Int = when (value) {
    BodyWeightBand.BELOW_110 -> R.string.egri_weight_0
    BodyWeightBand.FROM_110_TO_120 -> R.string.egri_weight_1
    BodyWeightBand.ABOVE_120 -> R.string.egri_weight_2
}

private fun historyLabel(value: DifficultIntubationHistory): Int = when (value) {
    DifficultIntubationHistory.ABSENT -> R.string.egri_history_0
    DifficultIntubationHistory.PRESENT -> R.string.egri_history_1
}

/** Шкала Эль-Ганзури: семь предоперационных критериев трудной интубации. */
@Composable
fun ElGanzouriScreen(modifier: Modifier = Modifier) {
    var mallampati by rememberSaveable { mutableStateOf(MallampatiClass.FIRST_SECOND) }
    var thyromental by rememberSaveable { mutableStateOf(ThyromentalDistance.AT_LEAST_SIX_HALF) }
    var mouthOpening by rememberSaveable { mutableStateOf(MouthOpening.AT_LEAST_FIVE) }
    var neckMovement by rememberSaveable { mutableStateOf(NeckMovement.ABOVE_NINETY) }
    var prognathia by rememberSaveable { mutableStateOf(Prognathia.POSSIBLE) }
    var weight by rememberSaveable { mutableStateOf(BodyWeightBand.BELOW_110) }
    var history by rememberSaveable { mutableStateOf(DifficultIntubationHistory.ABSENT) }

    val score = elGanzouriScore(
        ElGanzouriInput(
            mallampati = mallampati,
            thyromental = thyromental,
            mouthOpening = mouthOpening,
            neckMovement = neckMovement,
            prognathia = prognathia,
            weight = weight,
            difficultIntubation = history,
        )
    )

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
            ChoiceGroup(
                title = stringResource(R.string.egri_mallampati),
                explanation = stringResource(R.string.egri_mallampati_info),
                options = MallampatiClass.entries,
                selected = mallampati,
                onSelect = { mallampati = it },
                label = { stringResource(mallampatiLabel(it)) },
            )
            ChoiceGroup(
                title = stringResource(R.string.egri_thyromental),
                explanation = stringResource(R.string.egri_thyromental_info),
                options = ThyromentalDistance.entries,
                selected = thyromental,
                onSelect = { thyromental = it },
                label = { stringResource(thyromentalLabel(it)) },
            )
            ChoiceGroup(
                title = stringResource(R.string.egri_mouth),
                explanation = stringResource(R.string.egri_mouth_info),
                options = MouthOpening.entries,
                selected = mouthOpening,
                onSelect = { mouthOpening = it },
                label = { stringResource(mouthOpeningLabel(it)) },
            )
            ChoiceGroup(
                title = stringResource(R.string.egri_neck),
                explanation = stringResource(R.string.egri_neck_info),
                options = NeckMovement.entries,
                selected = neckMovement,
                onSelect = { neckMovement = it },
                label = { stringResource(neckMovementLabel(it)) },
            )
            ChoiceGroup(
                title = stringResource(R.string.egri_prognathia),
                explanation = stringResource(R.string.egri_prognathia_info),
                options = Prognathia.entries,
                selected = prognathia,
                onSelect = { prognathia = it },
                label = { stringResource(prognathiaLabel(it)) },
            )
            ChoiceGroup(
                title = stringResource(R.string.egri_weight),
                explanation = stringResource(R.string.egri_weight_info),
                options = BodyWeightBand.entries,
                selected = weight,
                onSelect = { weight = it },
                label = { stringResource(weightLabel(it)) },
            )
            ChoiceGroup(
                title = stringResource(R.string.egri_history),
                explanation = stringResource(R.string.egri_history_info),
                options = DifficultIntubationHistory.entries,
                selected = history,
                onSelect = { history = it },
                label = { stringResource(historyLabel(it)) },
            )

            // Место под закреплённую панель результата.
            Spacer(Modifier.height(160.dp))
        }

        val riskClass = elGanzouriRiskClass(score)
        BottomResultBar(
            title = stringResource(R.string.name_el_ganzouri),
            value = pointsLabel(score),
            secondary = stringResource(R.string.bar_airway_risk),
            risk = RiskBadge(
                label = stringResource(airwayRiskBadge(riskClass)),
                tone = airwayRiskTone(riskClass),
            ),
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

private fun airwayRiskBadge(risk: AirwayRiskClass): Int = when (risk) {
    AirwayRiskClass.LOW -> R.string.risk_badge_low
    AirwayRiskClass.MODERATE -> R.string.risk_badge_moderate
    AirwayRiskClass.HIGH -> R.string.risk_badge_high
}

private fun airwayRiskTone(risk: AirwayRiskClass): ResultTone = when (risk) {
    AirwayRiskClass.LOW -> ResultTone.Positive
    AirwayRiskClass.MODERATE -> ResultTone.Neutral
    AirwayRiskClass.HIGH -> ResultTone.Negative
}

@Preview(showBackground = true)
@Composable
private fun ElGanzouriScreenPreview() {
    Calc1Theme {
        ElGanzouriScreen()
    }
}
