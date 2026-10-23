package com.drwho174.calc1.ui.calc.oxygen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drwho174.calc1.R
import com.drwho174.calc1.ui.common.toNumberOrNull
import com.drwho174.calc1.domain.OxygenDeliveryInput
import com.drwho174.calc1.domain.calculateOxygenDelivery
import com.drwho174.calc1.ui.common.format
import com.drwho174.calc1.ui.components.NumberField
import com.drwho174.calc1.ui.components.ResultCard
import com.drwho174.calc1.ui.components.ResultGrid
import com.drwho174.calc1.ui.components.ResultItem
import com.drwho174.calc1.ui.components.ResultTone
import com.drwho174.calc1.ui.theme.Calc1Theme

/**
 * Доставка кислорода.
 *
 * Пороги прежние: DO2i > 270 и DO2i/VCO2i >= 5 подсвечиваются зелёным,
 * иначе — красным.
 */
@Composable
fun OxygenDeliveryScreen(modifier: Modifier = Modifier) {
    var bsa by rememberSaveable { mutableStateOf("") }
    var cardiacOutput by rememberSaveable { mutableStateOf("") }
    var hemoglobin by rememberSaveable { mutableStateOf("") }
    var spo2 by rememberSaveable { mutableStateOf("") }
    var pao2 by rememberSaveable { mutableStateOf("") }
    var etco2 by rememberSaveable { mutableStateOf("") }
    var freshGasFlow by rememberSaveable { mutableStateOf("") }
    var showBsaSheet by rememberSaveable { mutableStateOf(false) }

    val bsaValue = bsa.toNumberOrNull()
    val cardiacOutputValue = cardiacOutput.toNumberOrNull()
    val hemoglobinValue = hemoglobin.toNumberOrNull()
    val spo2Value = spo2.toNumberOrNull()
    val pao2Value = pao2.toNumberOrNull()
    val etco2Value = etco2.toNumberOrNull()
    val freshGasFlowValue = freshGasFlow.toNumberOrNull()

    val result = if (
        bsaValue != null && cardiacOutputValue != null && hemoglobinValue != null &&
        spo2Value != null && pao2Value != null && etco2Value != null && freshGasFlowValue != null
    ) {
        calculateOxygenDelivery(
            OxygenDeliveryInput(
                bsaM2 = bsaValue,
                cardiacOutputLMin = cardiacOutputValue,
                hemoglobinGPerL = hemoglobinValue,
                spo2Percent = spo2Value,
                pao2MmHg = pao2Value,
                etco2Percent = etco2Value,
                freshGasFlowLMin = freshGasFlowValue,
            )
        )
    } else {
        null
    }

    val unitMlDl = stringResource(R.string.unit_ml_dl)
    val unitMlMin = stringResource(R.string.unit_ml_min)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            NumberField(
                value = bsa,
                onValueChange = { bsa = it },
                label = stringResource(R.string.bsa),
                suffix = stringResource(R.string.unit_m2),
                hintText = stringResource(R.string.name_body_surface_area),
                modifier = Modifier.weight(1f),
                trailingIcon = {
                    IconButton(onClick = { showBsaSheet = true }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_calculate),
                            contentDescription = stringResource(R.string.name_body_surface_area),
                        )
                    }
                },
            )
            NumberField(
                value = cardiacOutput,
                onValueChange = { cardiacOutput = it },
                label = stringResource(R.string.cardiac_output_short),
                suffix = stringResource(R.string.unit_l_min),
                hintText = stringResource(R.string.cardiac_output),
                modifier = Modifier.weight(1f),
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            NumberField(
                value = hemoglobin,
                onValueChange = { hemoglobin = it },
                label = stringResource(R.string.haemoglobin),
                suffix = stringResource(R.string.gpl),
                modifier = Modifier.weight(1f),
            )
            NumberField(
                value = spo2,
                onValueChange = { spo2 = it },
                label = stringResource(R.string.spo2),
                suffix = "%",
                modifier = Modifier.weight(1f),
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            NumberField(
                value = pao2,
                onValueChange = { pao2 = it },
                label = stringResource(R.string.pao2),
                suffix = stringResource(R.string.unit_mmhg),
                modifier = Modifier.weight(1f),
            )
            NumberField(
                value = etco2,
                onValueChange = { etco2 = it },
                label = stringResource(R.string.etco2),
                suffix = stringResource(R.string.unit_mmhg),
                modifier = Modifier.weight(1f),
            )
        }
        NumberField(
            value = freshGasFlow,
            onValueChange = { freshGasFlow = it },
            label = stringResource(R.string.fresh_gas_flow),
            suffix = stringResource(R.string.unit_l_min),
        )

        // Карточки видны всегда: до ввода в них стоит прочерк.
        val dash = "—"
        // Как в прежней версии: содержание O2 — во всю ширину,
        // ниже четыре компактных результата в две колонки.
        ResultCard(
            title = stringResource(
                R.string.result_with_unit,
                stringResource(R.string.arterial_oxygen_content),
                unitMlDl,
            ),
            value = result?.arterialOxygenContent?.format(1) ?: dash,
        )
        ResultGrid(
            results = listOf(
                ResultItem(
                    title = stringResource(
                        R.string.result_with_unit,
                        stringResource(R.string.do2),
                        unitMlMin,
                    ),
                    value = result?.oxygenDelivery?.format(1) ?: dash,
                ),
                ResultItem(
                    title = stringResource(
                        R.string.result_with_unit,
                        stringResource(R.string.vco2i),
                        unitMlMin,
                    ),
                    value = result?.co2ProductionIndex?.format(1) ?: dash,
                ),
                ResultItem(
                    title = stringResource(
                        R.string.result_with_unit,
                        stringResource(R.string.do2i),
                        stringResource(R.string.unit_ml_min_m2),
                    ),
                    value = result?.oxygenDeliveryIndex?.format(1) ?: dash,
                    tone = when {
                        result == null -> ResultTone.Neutral
                        result.oxygenDeliveryIsAdequate -> ResultTone.Positive
                        else -> ResultTone.Negative
                    },
                ),
                ResultItem(
                    title = stringResource(R.string.do2i_vco2i),
                    value = result?.do2ToVco2Ratio?.format(1) ?: dash,
                    tone = when {
                        result == null -> ResultTone.Neutral
                        result.ratioIsAdequate -> ResultTone.Positive
                        else -> ResultTone.Negative
                    },
                ),
            ),
        )
    }

    if (showBsaSheet) {
        BsaSheet(
            onDismiss = { showBsaSheet = false },
            onResult = { value -> bsa = value.format(2) },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OxygenDeliveryScreenPreview() {
    Calc1Theme {
        OxygenDeliveryScreen()
    }
}
