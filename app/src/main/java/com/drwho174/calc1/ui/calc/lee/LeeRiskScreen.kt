package com.drwho174.calc1.ui.calc.lee

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
import com.drwho174.calc1.domain.LeeRiskClass
import com.drwho174.calc1.domain.LeeRiskInput
import com.drwho174.calc1.domain.leeMaceRiskPercent
import com.drwho174.calc1.domain.leeRiskClass
import com.drwho174.calc1.domain.leeRiskScore
import com.drwho174.calc1.ui.common.format
import com.drwho174.calc1.ui.components.ExplainedCheckboxRow
import com.drwho174.calc1.ui.components.ResultGrid
import com.drwho174.calc1.ui.components.ResultItem
import com.drwho174.calc1.ui.components.ResultTone
import com.drwho174.calc1.ui.theme.Calc1Theme

/**
 * Индекс Lee (RCRI): шесть предикторов и риск крупных кардиальных осложнений.
 *
 * Все критерии бинарные, поэтому сумма известна сразу и прочерка нет.
 */
@Composable
fun LeeRiskScreen(modifier: Modifier = Modifier) {
    var highRiskSurgery by rememberSaveable { mutableStateOf(false) }
    var ischemicHeartDisease by rememberSaveable { mutableStateOf(false) }
    var heartFailure by rememberSaveable { mutableStateOf(false) }
    var cerebrovascularDisease by rememberSaveable { mutableStateOf(false) }
    var insulinTherapy by rememberSaveable { mutableStateOf(false) }
    var creatinineAbove177 by rememberSaveable { mutableStateOf(false) }

    val score = leeRiskScore(
        LeeRiskInput(
            highRiskSurgery = highRiskSurgery,
            ischemicHeartDisease = ischemicHeartDisease,
            heartFailure = heartFailure,
            cerebrovascularDisease = cerebrovascularDisease,
            insulinTherapy = insulinTherapy,
            creatinineAbove177 = creatinineAbove177,
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
        ExplainedCheckboxRow(
            label = stringResource(R.string.lee_high_risk_surgery),
            explanation = stringResource(R.string.lee_high_risk_surgery_info),
            checked = highRiskSurgery,
            onCheckedChange = { highRiskSurgery = it },
        )
        ExplainedCheckboxRow(
            label = stringResource(R.string.lee_ischemic_heart_disease),
            explanation = stringResource(R.string.lee_ischemic_heart_disease_info),
            checked = ischemicHeartDisease,
            onCheckedChange = { ischemicHeartDisease = it },
        )
        ExplainedCheckboxRow(
            label = stringResource(R.string.lee_heart_failure),
            explanation = stringResource(R.string.lee_heart_failure_info),
            checked = heartFailure,
            onCheckedChange = { heartFailure = it },
        )
        ExplainedCheckboxRow(
            label = stringResource(R.string.lee_cerebrovascular_disease),
            explanation = stringResource(R.string.lee_cerebrovascular_disease_info),
            checked = cerebrovascularDisease,
            onCheckedChange = { cerebrovascularDisease = it },
        )
        ExplainedCheckboxRow(
            label = stringResource(R.string.lee_insulin_therapy),
            explanation = stringResource(R.string.lee_insulin_therapy_info),
            checked = insulinTherapy,
            onCheckedChange = { insulinTherapy = it },
        )
        ExplainedCheckboxRow(
            label = stringResource(R.string.lee_creatinine_above_177),
            explanation = stringResource(R.string.lee_creatinine_above_177_info),
            checked = creatinineAbove177,
            onCheckedChange = { creatinineAbove177 = it },
        )

        ResultGrid(
            results = listOf(
                ResultItem(
                    title = stringResource(R.string.result_score),
                    value = score.toString(),
                ),
                ResultItem(
                    title = stringResource(R.string.result_mace),
                    value = leeMaceRiskPercent(score).format(1),
                ),
                ResultItem(
                    title = stringResource(R.string.result_risk),
                    value = stringResource(leeRiskLabel(leeRiskClass(score))),
                    tone = leeRiskTone(leeRiskClass(score)),
                ),
            ),
            columns = 1,
        )
    }
}

private fun leeRiskLabel(riskClass: LeeRiskClass): Int = when (riskClass) {
    LeeRiskClass.LOW -> R.string.risk_low
    LeeRiskClass.MODERATE -> R.string.risk_moderate
    LeeRiskClass.HIGH -> R.string.risk_high
    LeeRiskClass.VERY_HIGH -> R.string.risk_very_high
}

private fun leeRiskTone(riskClass: LeeRiskClass): ResultTone = when (riskClass) {
    LeeRiskClass.LOW -> ResultTone.Positive
    LeeRiskClass.MODERATE -> ResultTone.Neutral
    LeeRiskClass.HIGH, LeeRiskClass.VERY_HIGH -> ResultTone.Negative
}

@Preview(showBackground = true)
@Composable
private fun LeeRiskScreenPreview() {
    Calc1Theme {
        LeeRiskScreen()
    }
}
