package com.drwho174.calc1.ui.components

import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.drwho174.calc1.ui.theme.signalGreen
import com.drwho174.calc1.ui.theme.signalRed

/** Смысловая окраска результата. */
enum class ResultTone { Neutral, Positive, Negative }

/**
 * Компактный результат одной строкой: подпись слева, значение справа.
 *
 * Формат повторяет прежнюю версию: «пилюля» с закруглением 8 dp, нейтральная —
 * на secondaryContainer, пороговые — зелёная/красная.
 */
@Composable
fun ResultCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    tone: ResultTone = ResultTone.Neutral,
) {
    val container: Color
    val content: Color
    when (tone) {
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
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = container,
        contentColor = content,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}
