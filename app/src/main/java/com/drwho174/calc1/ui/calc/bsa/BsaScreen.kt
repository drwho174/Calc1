package com.drwho174.calc1.ui.calc.bsa

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
import com.drwho174.calc1.domain.Bsa
import com.drwho174.calc1.ui.common.format
import com.drwho174.calc1.ui.components.NumberField
import com.drwho174.calc1.ui.components.ResultCard
import com.drwho174.calc1.ui.theme.Calc1Theme

/**
 * Площадь поверхности тела: три формулы считаются «на лету».
 *
 * Рост и вес стоят в одной горизонтали, как в исходной разметке.
 */
@Composable
fun BsaScreen(modifier: Modifier = Modifier) {
    var height by rememberSaveable { mutableStateOf("") }
    var weight by rememberSaveable { mutableStateOf("") }

    val heightCm = height.toDoubleOrNull()
    val weightKg = weight.toDoubleOrNull()
    val unitM2 = stringResource(R.string.unit_m2)

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

        if (heightCm != null && weightKg != null) {
            ResultCard(
                title = stringResource(R.string.bsa_mosteller),
                value = "${Bsa.mosteller(heightCm, weightKg).format(1)} $unitM2",
            )
            ResultCard(
                title = stringResource(R.string.bsa_dubois),
                value = "${Bsa.dubois(heightCm, weightKg).format(1)} $unitM2",
            )
            ResultCard(
                title = stringResource(R.string.bsa_haycock),
                value = "${Bsa.haycock(heightCm, weightKg).format(1)} $unitM2",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BsaScreenPreview() {
    Calc1Theme {
        BsaScreen()
    }
}
