package com.drwho174.calc1.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Элемент сетки результатов. */
data class ResultItem(
    val title: String,
    val value: String,
    val tone: ResultTone = ResultTone.Neutral,
)

/**
 * Результаты в несколько столбцов — карточки одинаковой ширины в строке.
 * Если в последнем ряду меньше элементов, пустые места заполняются распорками.
 */
@Composable
fun ResultGrid(
    results: List<ResultItem>,
    modifier: Modifier = Modifier,
    columns: Int = 2,
) {
    require(columns > 0) { "columns must be positive" }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        results.chunked(columns).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { item ->
                    ResultCard(
                        title = item.title,
                        value = item.value,
                        tone = item.tone,
                        modifier = Modifier.weight(1f),
                    )
                }
                repeat(columns - row.size) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}
