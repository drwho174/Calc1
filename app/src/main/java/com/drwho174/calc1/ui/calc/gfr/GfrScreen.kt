package com.drwho174.calc1.ui.calc.gfr

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
import com.drwho174.calc1.domain.estimateCkdEpi2021
import com.drwho174.calc1.ui.common.format
import com.drwho174.calc1.ui.components.ChoiceGroup
import com.drwho174.calc1.ui.components.NumberField
import com.drwho174.calc1.ui.components.ResultCard
import com.drwho174.calc1.ui.theme.Calc1Theme

/**
 * СКФ по CKD-EPI 2021.
 *
 * Пол по умолчанию — мужской (как в прежней разметке), единицы измерения
 * вынесены в подпись карточки, чтобы значение не переносилось на вторую строку.
 */
@Composable
fun GfrScreen(modifier: Modifier = Modifier) {
    var age by rememberSaveable { mutableStateOf("") }
    var creatinine by rememberSaveable { mutableStateOf("") }
    var isFemale by rememberSaveable { mutableStateOf(false) }

    val ageYears = age.toNumberOrNull()
    val creatinineUmol = creatinine.toNumberOrNull()

    val maleLabel = stringResource(R.string.Male)
    val femaleLabel = stringResource(R.string.Female)
    val gfrTitle = stringResource(
        R.string.result_with_unit,
        stringResource(R.string.creatinineClearance),
        stringResource(R.string.gfr_unit),
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
            title = stringResource(R.string.Sex),
            options = listOf(false, true),
            selected = isFemale,
            onSelect = { isFemale = it },
            label = { female -> if (female) femaleLabel else maleLabel },
            horizontal = true,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            NumberField(
                value = age,
                onValueChange = { age = it },
                label = stringResource(R.string.age),
                suffix = stringResource(R.string.unit_years),
                modifier = Modifier.weight(1f),
            )
            NumberField(
                value = creatinine,
                onValueChange = { creatinine = it },
                label = stringResource(R.string.creatinine),
                suffix = stringResource(R.string.unit_umol_l),
                modifier = Modifier.weight(1f),
            )
        }

        if (ageYears != null && creatinineUmol != null) {
            ResultCard(
                title = gfrTitle,
                value = estimateCkdEpi2021(
                    creatinineUmolPerL = creatinineUmol,
                    ageYears = ageYears,
                    isFemale = isFemale,
                ).format(2),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GfrScreenPreview() {
    Calc1Theme {
        GfrScreen()
    }
}
