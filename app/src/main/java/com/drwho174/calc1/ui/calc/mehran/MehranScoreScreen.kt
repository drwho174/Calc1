package com.drwho174.calc1.ui.calc.mehran

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drwho174.calc1.R
import com.drwho174.calc1.ui.common.toNumberOrNull
import com.drwho174.calc1.domain.EgfrCategory
import com.drwho174.calc1.domain.MehranScoreInput
import com.drwho174.calc1.domain.egfrCategoryFor
import com.drwho174.calc1.domain.mehranCinRiskPercent
import com.drwho174.calc1.domain.mehranDialysisRiskPercent
import com.drwho174.calc1.domain.mehranPoints
import com.drwho174.calc1.ui.common.format
import com.drwho174.calc1.ui.components.BottomResultBar
import com.drwho174.calc1.ui.components.CheckboxRow
import com.drwho174.calc1.ui.components.ChoiceGroup
import com.drwho174.calc1.ui.components.CkdEpiSheet
import com.drwho174.calc1.ui.components.NumberField
import com.drwho174.calc1.ui.components.ResultGrid
import com.drwho174.calc1.ui.components.ResultItem
import com.drwho174.calc1.ui.theme.Calc1Theme

/** Mehran Score: риск контраст-индуцированной нефропатии и диализа. */
@Composable
fun MehranScoreScreen(modifier: Modifier = Modifier) {
    var contrastVolume by rememberSaveable { mutableStateOf("") }
    var heartFailure by rememberSaveable { mutableStateOf(false) }
    var frailty by rememberSaveable { mutableStateOf(false) }
    var iabp by rememberSaveable { mutableStateOf(false) }
    var hypotension by rememberSaveable { mutableStateOf(false) }
    var anaemia by rememberSaveable { mutableStateOf(false) }
    var diabetes by rememberSaveable { mutableStateOf(false) }
    var egfrCategory by remember { mutableStateOf(EgfrCategory.ABOVE_60) }
    var measuredGfr by remember { mutableStateOf<Double?>(null) }
    var showCkdSheet by rememberSaveable { mutableStateOf(false) }

    val contrastValue = contrastVolume.toNumberOrNull()

    val points = if (contrastValue != null) {
        mehranPoints(
            MehranScoreInput(
                heartFailure = heartFailure,
                frailty = frailty,
                iabp = iabp,
                hypotension = hypotension,
                anaemia = anaemia,
                diabetes = diabetes,
                contrastVolumeMl = contrastValue,
                egfrCategory = egfrCategory,
            )
        )
    } else {
        null
    }

    val categoryLabels = mapOf(
        EgfrCategory.ABOVE_60 to stringResource(R.string.egfr_above_60),
        EgfrCategory.FROM_40_TO_60 to stringResource(R.string.egfr_60_40),
        EgfrCategory.FROM_20_TO_40 to stringResource(R.string.egfr_40_20),
        EgfrCategory.BELOW_20 to stringResource(R.string.egfr_below_20),
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
            NumberField(
                value = contrastVolume,
                onValueChange = { contrastVolume = it },
                label = stringResource(R.string.contrast_volume),
                suffix = stringResource(R.string.unit_ml),
            )

            CheckboxRow(
                label = stringResource(R.string.congestive_heart_falure),
                checked = heartFailure,
                onCheckedChange = { heartFailure = it },
            )
            CheckboxRow(
                label = stringResource(R.string.age_older_75),
                checked = frailty,
                onCheckedChange = { frailty = it },
            )
            CheckboxRow(
                label = stringResource(R.string.IABP),
                checked = iabp,
                onCheckedChange = { iabp = it },
            )
            CheckboxRow(
                label = stringResource(R.string.hypotension),
                checked = hypotension,
                onCheckedChange = { hypotension = it },
            )
            CheckboxRow(
                label = stringResource(R.string.anemia),
                checked = anaemia,
                onCheckedChange = { anaemia = it },
            )
            CheckboxRow(
                label = stringResource(R.string.diabetis),
                checked = diabetes,
                onCheckedChange = { diabetes = it },
            )

            Text(
                text = stringResource(R.string.creatinineClearance),
                style = MaterialTheme.typography.titleMedium,
            )
            ChoiceGroup(
                title = null,
                options = EgfrCategory.entries,
                selected = egfrCategory,
                onSelect = { egfrCategory = it },
                label = { categoryLabels.getValue(it) },
            )
            Button(
                onClick = { showCkdSheet = true },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.calculate_gfr))
            }

            // Карточки видны всегда: до расчёта в них стоит прочерк.
            val dash = "—"
            ResultGrid(
                results = listOf(
                    ResultItem(
                        title = stringResource(
                            R.string.result_with_unit,
                            stringResource(R.string.creatinineClearance),
                            stringResource(R.string.gfr_unit),
                        ),
                        value = measuredGfr?.format(2) ?: dash,
                    )
                ),
                columns = 1,
            )

            // Место под закреплённую панель результата.
            Spacer(Modifier.height(160.dp))
        }

        BottomResultBar(
            title = stringResource(R.string.name_mehran_score),
            value = points?.let { mehranCinRiskPercent(it).format(1) + " %" } ?: "—",
            secondary = stringResource(R.string.bar_cin_risk),
            caption = points?.let {
                stringResource(R.string.bar_dialysis_risk) +
                    " · " + mehranDialysisRiskPercent(it).format(1) + " %"
            },
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }

    if (showCkdSheet) {
        CkdEpiSheet(
            onDismiss = { showCkdSheet = false },
            onResult = { gfr ->
                measuredGfr = gfr
                egfrCategory = egfrCategoryFor(gfr)
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MehranScoreScreenPreview() {
    Calc1Theme {
        MehranScoreScreen()
    }
}
