package com.drwho174.calc1.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.drwho174.calc1.R
import com.drwho174.calc1.domain.estimateCkdEpi2021
import com.drwho174.calc1.ui.common.format

/**
 * Шторка с расчётом СКФ по CKD-EPI 2021 — общая для EuroSCORE II и Mehran Score.
 *
 * Заменяет прежний `CKDDial`, который передавал результат через
 * `FragmentResult` со строковыми ключами.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CkdEpiSheet(
    onDismiss: () -> Unit,
    onResult: (Double) -> Unit,
) {
    var age by rememberSaveable { mutableStateOf("") }
    var creatinine by rememberSaveable { mutableStateOf("") }
    var isFemale by rememberSaveable { mutableStateOf(false) }

    val ageYears = age.toDoubleOrNull()
    val creatinineUmol = creatinine.toDoubleOrNull()
    val gfr = if (ageYears != null && creatinineUmol != null) {
        estimateCkdEpi2021(creatinineUmol, ageYears, isFemale)
    } else {
        null
    }

    val maleLabel = stringResource(R.string.Male)
    val femaleLabel = stringResource(R.string.Female)
    val gfrTitle = stringResource(
        R.string.result_with_unit,
        stringResource(R.string.creatinineClearance),
        stringResource(R.string.gfr_unit),
    )

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(R.string.ckd_epi_title),
                style = MaterialTheme.typography.titleLarge,
            )

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

            if (gfr != null) {
                ResultCard(title = gfrTitle, value = gfr.format(2))
            }

            Button(
                onClick = {
                    gfr?.let(onResult)
                    onDismiss()
                },
                enabled = gfr != null,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.calculate))
            }
        }
    }
}
