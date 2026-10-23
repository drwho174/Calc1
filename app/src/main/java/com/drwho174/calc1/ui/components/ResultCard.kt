package com.drwho174.calc1.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.drwho174.calc1.ui.theme.signalGreen
import com.drwho174.calc1.ui.theme.signalRed

/** Смысловая окраска результата — замена `setCardBackgroundColor` из фрагментов. */
enum class ResultTone { Neutral, Positive, Negative }

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
            // Как в прежней версии: карточки результата на secondaryContainer.
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

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = container, contentColor = content),
    ) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(text = title, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}
