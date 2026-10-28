package com.drwho174.calc1.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.drwho174.calc1.ui.theme.signalGreen
import com.drwho174.calc1.ui.theme.signalRed

/** Плашка класса риска: подпись и её окраска. */
data class RiskBadge(
    val label: String,
    val tone: ResultTone,
)

/**
 * Закреплённая снизу панель результата — как в MDCalc и EuroSCORE: карточка
 * с крупным значением, которая остаётся на месте, пока содержимое экрана
 * прокручивается под ней.
 *
 * Кроме основного значения панель умеет показывать вторую строку
 * ([secondary]) и цветную плашку риска ([risk]).
 *
 * Вызывать внутри `Box`, выравнивая по `Alignment.BottomCenter`.
 */
@Composable
fun BottomResultBar(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    caption: String? = null,
    secondary: String? = null,
    risk: RiskBadge? = null,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        shadowElevation = 12.dp,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 2.dp),
            )
            if (secondary != null || risk != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    if (secondary != null) {
                        Text(
                            text = secondary,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f),
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    if (risk != null) {
                        RiskPill(badge = risk)
                    }
                }
            }
            if (caption != null) {
                Text(
                    text = caption,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }
    }
}

@Composable
private fun RiskPill(badge: RiskBadge) {
    val container: Color
    val content: Color
    when (badge.tone) {
        ResultTone.Neutral -> {
            container = MaterialTheme.colorScheme.secondaryContainer
            content = MaterialTheme.colorScheme.onSecondaryContainer
        }
        ResultTone.Positive -> {
            container = signalGreen
            content = Color.White
        }
        ResultTone.Negative -> {
            container = signalRed
            content = Color.White
        }
    }

    Surface(
        shape = RoundedCornerShape(percent = 50),
        color = container,
        contentColor = content,
    ) {
        Text(
            text = badge.label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
        )
    }
}
