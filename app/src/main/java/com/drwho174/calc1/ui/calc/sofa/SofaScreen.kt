package com.drwho174.calc1.ui.calc.sofa

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
import com.drwho174.calc1.domain.SofaOrgan
import com.drwho174.calc1.domain.SofaSeverity
import com.drwho174.calc1.domain.sofaMortalityRange
import com.drwho174.calc1.domain.sofaScore
import com.drwho174.calc1.domain.sofaSeverity
import com.drwho174.calc1.ui.components.ChoiceGroup
import com.drwho174.calc1.ui.components.ResultGrid
import com.drwho174.calc1.ui.components.ResultItem
import com.drwho174.calc1.ui.components.ResultTone
import com.drwho174.calc1.ui.theme.Calc1Theme

/** Баллы по органу: 0–4. */
private val sofaPoints = listOf(0, 1, 2, 3, 4)

private fun sofaOptionLabel(organ: SofaOrgan, points: Int): Int = when (organ) {
    SofaOrgan.RESPIRATION -> when (points) {
        0 -> R.string.sofa_respiration_0
        1 -> R.string.sofa_respiration_1
        2 -> R.string.sofa_respiration_2
        3 -> R.string.sofa_respiration_3
        else -> R.string.sofa_respiration_4
    }

    SofaOrgan.COAGULATION -> when (points) {
        0 -> R.string.sofa_coagulation_0
        1 -> R.string.sofa_coagulation_1
        2 -> R.string.sofa_coagulation_2
        3 -> R.string.sofa_coagulation_3
        else -> R.string.sofa_coagulation_4
    }

    SofaOrgan.LIVER -> when (points) {
        0 -> R.string.sofa_liver_0
        1 -> R.string.sofa_liver_1
        2 -> R.string.sofa_liver_2
        3 -> R.string.sofa_liver_3
        else -> R.string.sofa_liver_4
    }

    SofaOrgan.CARDIOVASCULAR -> when (points) {
        0 -> R.string.sofa_cardiovascular_0
        1 -> R.string.sofa_cardiovascular_1
        2 -> R.string.sofa_cardiovascular_2
        3 -> R.string.sofa_cardiovascular_3
        else -> R.string.sofa_cardiovascular_4
    }

    SofaOrgan.CENTRAL_NERVOUS -> when (points) {
        0 -> R.string.sofa_cns_0
        1 -> R.string.sofa_cns_1
        2 -> R.string.sofa_cns_2
        3 -> R.string.sofa_cns_3
        else -> R.string.sofa_cns_4
    }

    SofaOrgan.RENAL -> when (points) {
        0 -> R.string.sofa_renal_0
        1 -> R.string.sofa_renal_1
        2 -> R.string.sofa_renal_2
        3 -> R.string.sofa_renal_3
        else -> R.string.sofa_renal_4
    }
}

private fun sofaTitle(organ: SofaOrgan): Int = when (organ) {
    SofaOrgan.RESPIRATION -> R.string.sofa_respiration
    SofaOrgan.COAGULATION -> R.string.sofa_coagulation
    SofaOrgan.LIVER -> R.string.sofa_liver
    SofaOrgan.CARDIOVASCULAR -> R.string.sofa_cardiovascular
    SofaOrgan.CENTRAL_NERVOUS -> R.string.sofa_cns
    SofaOrgan.RENAL -> R.string.sofa_renal
}

private fun sofaInfo(organ: SofaOrgan): Int = when (organ) {
    SofaOrgan.RESPIRATION -> R.string.sofa_respiration_info
    SofaOrgan.COAGULATION -> R.string.sofa_coagulation_info
    SofaOrgan.LIVER -> R.string.sofa_liver_info
    SofaOrgan.CARDIOVASCULAR -> R.string.sofa_cardiovascular_info
    SofaOrgan.CENTRAL_NERVOUS -> R.string.sofa_cns_info
    SofaOrgan.RENAL -> R.string.sofa_renal_info
}

/**
 * SOFA: по каждому органу выбирается худшая категория за сутки.
 *
 * Такой ввод повторяет бланк шкалы и не требует пересчитывать баллы в голове.
 */
@Composable
fun SofaScreen(modifier: Modifier = Modifier) {
    var respiration by rememberSaveable { mutableStateOf(0) }
    var coagulation by rememberSaveable { mutableStateOf(0) }
    var liver by rememberSaveable { mutableStateOf(0) }
    var cardiovascular by rememberSaveable { mutableStateOf(0) }
    var centralNervous by rememberSaveable { mutableStateOf(0) }
    var renal by rememberSaveable { mutableStateOf(0) }

    val selectedByOrgan = mapOf(
        SofaOrgan.RESPIRATION to respiration,
        SofaOrgan.COAGULATION to coagulation,
        SofaOrgan.LIVER to liver,
        SofaOrgan.CARDIOVASCULAR to cardiovascular,
        SofaOrgan.CENTRAL_NERVOUS to centralNervous,
        SofaOrgan.RENAL to renal,
    )
    val score = sofaScore(selectedByOrgan)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SofaOrgan.entries.forEach { organ ->
            ChoiceGroup(
                title = stringResource(sofaTitle(organ)),
                explanation = stringResource(sofaInfo(organ)),
                options = sofaPoints,
                selected = selectedByOrgan.getValue(organ),
                onSelect = { value ->
                    when (organ) {
                        SofaOrgan.RESPIRATION -> respiration = value
                        SofaOrgan.COAGULATION -> coagulation = value
                        SofaOrgan.LIVER -> liver = value
                        SofaOrgan.CARDIOVASCULAR -> cardiovascular = value
                        SofaOrgan.CENTRAL_NERVOUS -> centralNervous = value
                        SofaOrgan.RENAL -> renal = value
                    }
                },
                label = { value -> stringResource(sofaOptionLabel(organ, value)) },
            )
        }

        ResultGrid(
            results = listOf(
                ResultItem(
                    title = stringResource(R.string.result_score),
                    value = score.toString(),
                ),
                ResultItem(
                    title = stringResource(R.string.result_mortality),
                    value = sofaMortalityRange(score),
                ),
                ResultItem(
                    title = stringResource(R.string.result_risk),
                    value = stringResource(sofaRiskLabel(sofaSeverity(score))),
                    tone = sofaRiskTone(sofaSeverity(score)),
                ),
            ),
            columns = 1,
        )
    }
}

private fun sofaRiskLabel(severity: SofaSeverity): Int = when (severity) {
    SofaSeverity.LOW -> R.string.risk_low
    SofaSeverity.MODERATE -> R.string.risk_moderate
    SofaSeverity.HIGH -> R.string.risk_high
    SofaSeverity.VERY_HIGH -> R.string.risk_very_high
}

private fun sofaRiskTone(severity: SofaSeverity): ResultTone = when (severity) {
    SofaSeverity.LOW -> ResultTone.Positive
    SofaSeverity.MODERATE -> ResultTone.Neutral
    SofaSeverity.HIGH, SofaSeverity.VERY_HIGH -> ResultTone.Negative
}

@Preview(showBackground = true)
@Composable
private fun SofaScreenPreview() {
    Calc1Theme {
        SofaScreen()
    }
}
