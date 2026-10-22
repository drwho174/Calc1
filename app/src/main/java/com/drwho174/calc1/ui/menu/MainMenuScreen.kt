package com.drwho174.calc1.ui.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drwho174.calc1.R
import com.drwho174.calc1.ui.theme.Calc1Theme

private class MenuEntry(
    val labelRes: Int,
    val onClick: () -> Unit,
)

/**
 * Главное меню — первый экран, переведённый на Compose.
 *
 * Пока живёт внутри старого фрагмента и старого тулбара, поэтому не рисует
 * собственный топ-бар: заголовок и пункты «Настройки»/«О программе» остаются
 * в Activity.
 */
@Composable
fun MainMenuScreen(
    onEuroScore: () -> Unit,
    onHbPerfusion: () -> Unit,
    onPerfusiologist: () -> Unit,
    onOxygenDelivery: () -> Unit,
    onGfr: () -> Unit,
    onIdealBodyMass: () -> Unit,
    onBsa: () -> Unit,
    onRespiratory: () -> Unit,
    onMehranScore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Порядок кнопок сохранён как в fragment_main_menu.xml.
    val entries = listOf(
        MenuEntry(R.string.name_euroscore_2, onEuroScore),
        MenuEntry(R.string.name_hb_on_perfusion, onHbPerfusion),
        MenuEntry(R.string.name_perfusion_calculator, onPerfusiologist),
        MenuEntry(R.string.name_oxygen_delivery, onOxygenDelivery),
        MenuEntry(R.string.name_glomerular_filtration_rate, onGfr),
        MenuEntry(R.string.name_ideal_body_mass, onIdealBodyMass),
        MenuEntry(R.string.name_body_surface_area, onBsa),
        MenuEntry(R.string.name_oxygenation_params, onRespiratory),
        MenuEntry(R.string.name_mehran_score, onMehranScore),
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(entries) { entry ->
            Button(
                onClick = entry.onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp),
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(
                    text = stringResource(entry.labelRes),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainMenuPreview() {
    Calc1Theme {
        MainMenuScreen(
            onEuroScore = {},
            onHbPerfusion = {},
            onPerfusiologist = {},
            onOxygenDelivery = {},
            onGfr = {},
            onIdealBodyMass = {},
            onBsa = {},
            onRespiratory = {},
            onMehranScore = {},
        )
    }
}
