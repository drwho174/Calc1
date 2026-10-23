package com.drwho174.calc1.ui.calc.oxygen

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
import com.drwho174.calc1.domain.Bsa
import com.drwho174.calc1.ui.common.format
import com.drwho174.calc1.ui.components.NumberField
import com.drwho174.calc1.ui.components.ResultCard

/**
 * Шторка расчёта площади поверхности тела по Дюбуа.
 *
 * Вызывается кнопкой в правом краю поля «ППТ» на экране доставки кислорода;
 * результат подставляется в поле, чтобы не вводить его вручную.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BsaSheet(
    onDismiss: () -> Unit,
    onResult: (Double) -> Unit,
) {
    var height by rememberSaveable { mutableStateOf("") }
    var weight by rememberSaveable { mutableStateOf("") }

    val heightCm = height.toDoubleOrNull()
    val weightKg = weight.toDoubleOrNull()
    val bsa = if (heightCm != null && weightKg != null) {
        Bsa.dubois(heightCm, weightKg)
    } else {
        null
    }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(R.string.name_body_surface_area),
                style = MaterialTheme.typography.titleLarge,
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

            if (bsa != null) {
                ResultCard(
                    title = stringResource(
                        R.string.result_with_unit,
                        stringResource(R.string.bsa_dubois),
                        stringResource(R.string.unit_m2),
                    ),
                    value = bsa.format(2),
                )
            }

            Button(
                onClick = {
                    bsa?.let(onResult)
                    onDismiss()
                },
                enabled = bsa != null,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.action_apply))
            }
        }
    }
}
