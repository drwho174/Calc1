package com.drwho174.calc1.ui.calc.respiratory

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
import com.drwho174.calc1.domain.aaGradient
import com.drwho174.calc1.domain.pfRatio
import com.drwho174.calc1.domain.respiratoryIndex
import com.drwho174.calc1.domain.sfRatio
import com.drwho174.calc1.ui.common.format
import com.drwho174.calc1.ui.components.NumberField
import com.drwho174.calc1.ui.components.ResultGrid
import com.drwho174.calc1.ui.components.ResultItem
import com.drwho174.calc1.ui.theme.Calc1Theme

/**
 * Показатели оксигенации.
 *
 * Поля ввода — в две колонки по два поля, результаты — в две колонки.
 * P/F считается при FiO2 + PaO2, S/F — при FiO2 + SpO2, A-a и RI — при трёх газах.
 */
@Composable
fun RespiratoryScreen(modifier: Modifier = Modifier) {
    var pao2 by rememberSaveable { mutableStateOf("") }
    var paco2 by rememberSaveable { mutableStateOf("") }
    var fio2 by rememberSaveable { mutableStateOf("") }
    var spo2 by rememberSaveable { mutableStateOf("") }

    val pao2Value = pao2.toDoubleOrNull()
    val paco2Value = paco2.toDoubleOrNull()
    val fio2Value = fio2.toDoubleOrNull()
    val spo2Value = spo2.toDoubleOrNull()

    val mmhg = stringResource(R.string.unit_mmhg)
    val pfTitle = stringResource(R.string.pf_ratio)
    val sfTitle = stringResource(R.string.sf_ratio)
    val aaTitle = stringResource(
        R.string.result_with_unit,
        stringResource(R.string.aa_gradient),
        mmhg,
    )
    val riTitle = stringResource(R.string.respiratory_index)

    val results = buildList {
        if (fio2Value != null && pao2Value != null) {
            add(ResultItem(pfTitle, pfRatio(fio2Value, pao2Value).format(0)))
        }
        if (fio2Value != null && spo2Value != null) {
            add(ResultItem(sfTitle, sfRatio(fio2Value, spo2Value).format(1)))
        }
        if (fio2Value != null && pao2Value != null && paco2Value != null) {
            add(
                ResultItem(
                    aaTitle,
                    aaGradient(fio2Value, pao2Value, paco2Value).format(1),
                )
            )
            add(
                ResultItem(
                    riTitle,
                    respiratoryIndex(fio2Value, pao2Value, paco2Value).format(1),
                )
            )
        }
    }

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
                value = pao2,
                onValueChange = { pao2 = it },
                label = stringResource(R.string.pao2),
                suffix = mmhg,
                modifier = Modifier.weight(1f),
            )
            NumberField(
                value = paco2,
                onValueChange = { paco2 = it },
                label = stringResource(R.string.paco2),
                suffix = mmhg,
                modifier = Modifier.weight(1f),
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            NumberField(
                value = fio2,
                onValueChange = { fio2 = it },
                label = stringResource(R.string.fio2),
                suffix = "%",
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

        if (results.isNotEmpty()) {
            ResultGrid(results = results)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RespiratoryScreenPreview() {
    Calc1Theme {
        RespiratoryScreen()
    }
}
