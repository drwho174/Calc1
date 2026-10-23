package com.drwho174.calc1.ui.calc.hb

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.drwho174.calc1.ui.common.toNumberOrNull
import com.drwho174.calc1.domain.HbPerfusionInput
import com.drwho174.calc1.domain.calculateHbPerfusion
import com.drwho174.calc1.ui.common.format
import com.drwho174.calc1.ui.components.ChoiceGroup
import com.drwho174.calc1.ui.components.NumberField
import com.drwho174.calc1.ui.components.ResultGrid
import com.drwho174.calc1.ui.components.ResultItem
import com.drwho174.calc1.ui.theme.Calc1Theme

/** Расчёт гемоглобина на перфузии: объёмы крови по Allen и Nadler. */
@Composable
fun HbPerfusionScreen(modifier: Modifier = Modifier) {
    var isFemale by rememberSaveable { mutableStateOf(false) }
    var height by rememberSaveable { mutableStateOf("") }
    var weight by rememberSaveable { mutableStateOf("") }
    var initialHb by rememberSaveable { mutableStateOf("") }
    var infusion by rememberSaveable { mutableStateOf("") }
    var exfusion by rememberSaveable { mutableStateOf("") }
    var diuresis by rememberSaveable { mutableStateOf("") }
    var cpbExfusion by rememberSaveable { mutableStateOf("") }
    var prime by rememberSaveable { mutableStateOf("") }

    val heightValue = height.toNumberOrNull()
    val weightValue = weight.toNumberOrNull()
    val hbValue = initialHb.toNumberOrNull()
    val infusionValue = infusion.toNumberOrNull()
    val exfusionValue = exfusion.toNumberOrNull()
    val diuresisValue = diuresis.toNumberOrNull()
    val cpbExfusionValue = cpbExfusion.toNumberOrNull()
    val primeValue = prime.toNumberOrNull()

    val result = if (
        heightValue != null && weightValue != null && hbValue != null &&
        infusionValue != null && exfusionValue != null && diuresisValue != null &&
        cpbExfusionValue != null && primeValue != null
    ) {
        calculateHbPerfusion(
            HbPerfusionInput(
                heightCm = heightValue,
                weightKg = weightValue,
                isFemale = isFemale,
                hemoglobinGPerL = hbValue,
                infusionMl = infusionValue,
                exfusionMl = exfusionValue,
                diuresisMl = diuresisValue,
                cpbExfusionMl = cpbExfusionValue,
                primeMl = primeValue,
            )
        )
    } else {
        null
    }

    val maleLabel = stringResource(R.string.Male)
    val femaleLabel = stringResource(R.string.Female)
    val unitMl = stringResource(R.string.unit_ml)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ChoiceGroup(
            title = stringResource(R.string.Sex),
            options = listOf(false, true),
            selected = isFemale,
            onSelect = { isFemale = it },
            label = { female -> if (female) femaleLabel else maleLabel },
            horizontal = true,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            NumberField(
                value = height,
                onValueChange = { height = it },
                label = stringResource(R.string.name_Height),
                suffix = stringResource(R.string.unit_cm),
                modifier = Modifier.weight(1f),
            )
            NumberField(
                value = weight,
                onValueChange = { weight = it },
                label = stringResource(R.string.name_Weight),
                suffix = stringResource(R.string.unit_kg),
                modifier = Modifier.weight(1f),
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            NumberField(
                value = initialHb,
                onValueChange = { initialHb = it },
                label = stringResource(R.string.HbInitial),
                suffix = stringResource(R.string.gpl),
                modifier = Modifier.weight(1f),
            )
            NumberField(
                value = infusion,
                onValueChange = { infusion = it },
                label = stringResource(R.string.InfusionVol),
                suffix = unitMl,
                modifier = Modifier.weight(1f),
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            NumberField(
                value = exfusion,
                onValueChange = { exfusion = it },
                label = stringResource(R.string.ExfusionVol),
                suffix = unitMl,
                modifier = Modifier.weight(1f),
            )
            NumberField(
                value = diuresis,
                onValueChange = { diuresis = it },
                label = stringResource(R.string.Diuresis),
                suffix = unitMl,
                modifier = Modifier.weight(1f),
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            NumberField(
                value = cpbExfusion,
                onValueChange = { cpbExfusion = it },
                label = stringResource(R.string.CPBExfusionVol),
                suffix = unitMl,
                modifier = Modifier.weight(1f),
            )
            NumberField(
                value = prime,
                onValueChange = { prime = it },
                label = stringResource(R.string.PrimeVol),
                suffix = unitMl,
                modifier = Modifier.weight(1f),
            )
        }

        if (result != null) {
            val gpl = stringResource(R.string.gpl)

            ResultGrid(
                results = listOf(                    ResultItem(
                        stringResource(
                            R.string.result_with_unit,
                            stringResource(R.string.hbbp1),
                            gpl,
                        ),
                        result.beforeCpbAllen.format(2),
                    ),
                    ResultItem(
                        stringResource(
                            R.string.result_with_unit,
                            stringResource(R.string.hbap1),
                            gpl,
                        ),
                        result.afterCpbAllen.format(2),
                    ),
                    ResultItem(
                        stringResource(
                            R.string.result_with_unit,
                            stringResource(R.string.hbbp2),
                            gpl,
                        ),
                        result.beforeCpbNadler.format(2),
                    ),
                    ResultItem(
                        stringResource(
                            R.string.result_with_unit,
                            stringResource(R.string.hbap2),
                            gpl,
                        ),
                        result.afterCpbNadler.format(2),
                    ),
                ),
                columns = 1,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HbPerfusionScreenPreview() {
    Calc1Theme {
        HbPerfusionScreen()
    }
}
