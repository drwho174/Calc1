package com.drwho174.calc1.ui.calc.perfusiologist

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drwho174.calc1.R
import com.drwho174.calc1.domain.Perfusiologist
import com.drwho174.calc1.ui.common.format
import com.drwho174.calc1.ui.components.NumberField
import com.drwho174.calc1.ui.components.ResultGrid
import com.drwho174.calc1.ui.components.ResultItem
import com.drwho174.calc1.ui.components.SliderField
import com.drwho174.calc1.ui.theme.Calc1Theme
import kotlin.math.roundToInt

/**
 * Калькулятор перфузиолога: ППТ по Дюбуа и три связанных слайдера.
 *
 * Процент и скорость согласованы так же, как в прежних слушателях:
 * изменение скорости пересчитывает процент с шагом 5 (0..200).
 */
@Composable
fun PerfusiologistScreen(modifier: Modifier = Modifier) {
    var height by rememberSaveable { mutableStateOf("") }
    var weight by rememberSaveable { mutableStateOf("") }
    var rate by rememberSaveable { mutableStateOf(2.4f) }
    var percent by rememberSaveable { mutableStateOf(100f) }

    val heightCm = height.toDoubleOrNull()
    val weightKg = weight.toDoubleOrNull()

    val bsa = if (heightCm != null && weightKg != null) {
        Perfusiologist.bodySurfaceArea(heightCm, weightKg)
    } else {
        null
    }
    val perfusionIndex = bsa?.let { Perfusiologist.perfusionIndex(rate.toDouble(), it) }
    val speed = perfusionIndex?.let { Perfusiologist.perfusionSpeed(it, percent.toDouble()) }

    val dash = "—"

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

        ResultGrid(
            results = listOf(
                ResultItem(
                    title = stringResource(
                        R.string.result_with_unit,
                        stringResource(R.string.name_body_surface_area),
                        stringResource(R.string.unit_m2),
                    ),
                    value = bsa?.format(3) ?: dash,
                ),
                ResultItem(
                    title = stringResource(
                        R.string.result_with_unit,
                        stringResource(R.string.name_PerfusionRate),
                        stringResource(R.string.unit_l_min),
                    ),
                    value = speed?.format(2) ?: dash,
                ),
                ResultItem(
                    title = stringResource(
                        R.string.result_with_unit,
                        stringResource(R.string.name_PerfusoionPerfomance),
                        "%",
                    ),
                    value = percent.roundToInt().toString(),
                ),
            ),
            columns = 1,
        )

        SliderField(
            label = stringResource(R.string.name_PerfusionIndex),
            value = rate,
            valueRange = 2.0f..2.7f,
            steps = 0,
            valueText = rate.format(1),
            onValueChange = { rate = (it * 10).roundToInt() / 10f },
        )

        SliderField(
            label = stringResource(R.string.name_PerfusoionPerfomance),
            value = percent,
            valueRange = 0f..200f,
            steps = 0,
            valueText = "${percent.roundToInt()}%",
            onValueChange = { percent = (it / 5).roundToInt() * 5f },
        )

        val speedSliderValue = speed?.toFloat()?.coerceIn(0f, 8f) ?: 0f
        SliderField(
            label = stringResource(R.string.name_PerfusionRate),
            value = speedSliderValue,
            valueRange = 0f..8f,
            steps = 0,
            valueText = speed?.format(2) ?: dash,
            onValueChange = { newSpeed ->
                val index = perfusionIndex
                if (index != null && index > 0) {
                    val snapped = Perfusiologist.snapPercent(newSpeed * 100 / index)
                    if (snapped != null) percent = snapped.toFloat()
                }
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PerfusiologistScreenPreview() {
    Calc1Theme {
        PerfusiologistScreen()
    }
}
