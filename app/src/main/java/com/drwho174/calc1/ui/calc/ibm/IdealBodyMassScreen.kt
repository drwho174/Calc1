package com.drwho174.calc1.ui.calc.ibm

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
import com.drwho174.calc1.domain.idealBodyMassKg
import com.drwho174.calc1.ui.common.format
import com.drwho174.calc1.ui.components.ChoiceGroup
import com.drwho174.calc1.ui.components.NumberField
import com.drwho174.calc1.ui.components.ResultCard
import com.drwho174.calc1.ui.theme.Calc1Theme

/**
 * Идеальная масса тела.
 *
 * Переключатель пола — в горизонтальной строке, как в исходном RadioGroup.
 * Пол по умолчанию — мужской: раньше при невыбранном поле результат считался
 * с коэффициентом 0, то есть был неверным до первого касания переключателя.
 */
@Composable
fun IdealBodyMassScreen(modifier: Modifier = Modifier) {
    var height by rememberSaveable { mutableStateOf("") }
    var isFemale by rememberSaveable { mutableStateOf(false) }

    val heightCm = height.toDoubleOrNull()
    val maleLabel = stringResource(R.string.Male)
    val femaleLabel = stringResource(R.string.Female)

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

        NumberField(
            value = height,
            onValueChange = { height = it },
            label = stringResource(R.string.name_Height),
            suffix = stringResource(R.string.unit_cm),
        )

        if (heightCm != null) {
            ResultCard(
                title = stringResource(
                    R.string.result_with_unit,
                    stringResource(R.string.name_ideal_body_mass),
                    stringResource(R.string.unit_kg),
                ),
                value = idealBodyMassKg(heightCm, isFemale).format(1),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IdealBodyMassScreenPreview() {
    Calc1Theme {
        IdealBodyMassScreen()
    }
}
