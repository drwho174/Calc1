package com.drwho174.calc1.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.drwho174.calc1.R

/**
 * Кнопка «i» для пункта шкалы: раскрывает короткое пояснение прямо под строкой.
 *
 * Модальные окна здесь намеренно не используются — врач сверяется с
 * определением критерия, не теряя введённые данные.
 */
@Composable
fun InfoToggle(
    expanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onToggle,
        modifier = modifier.size(36.dp),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_info_outline),
            contentDescription = stringResource(
                if (expanded) R.string.action_hide_explanation else R.string.action_show_explanation,
            ),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp),
        )
    }
}

/** Раскрытое пояснение критерия: прижато к левому краю, читается как сноска. */
@Composable
fun ExplanationText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, bottom = 8.dp),
    )
}

/**
 * Строка с чекбоксом и пояснением: клик по тексту переключает критерий,
 * клик по «i» раскрывает определение, не меняя отметку.
 */
@Composable
fun ExplainedCheckboxRow(
    label: String,
    explanation: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier.animateContentSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(
                    value = checked,
                    onValueChange = onCheckedChange,
                    role = Role.Checkbox,
                )
                .padding(top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(checked = checked, onCheckedChange = null)
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
            )
            InfoToggle(expanded = expanded, onToggle = { expanded = !expanded })
        }

        if (expanded) {
            ExplanationText(text = explanation, modifier = Modifier.padding(start = 48.dp))
        }
    }
}

/** Заголовок группы критериев с пояснением по «i». */
@Composable
fun ExplainedGroupTitle(
    title: String,
    explanation: String,
    modifier: Modifier = Modifier,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier.animateContentSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
            )
            InfoToggle(expanded = expanded, onToggle = { expanded = !expanded })
        }

        if (expanded) {
            ExplanationText(text = explanation, modifier = Modifier.padding(bottom = 4.dp))
        }
    }
}
