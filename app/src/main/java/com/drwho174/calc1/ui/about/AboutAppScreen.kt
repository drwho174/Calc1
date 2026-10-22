package com.drwho174.calc1.ui.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.drwho174.calc1.R

/** Экран «О программе»: версия, автор и контакты. */
@Composable
fun AboutAppScreen(
    versionName: String,
    email: String,
    telegram: String,
    onEmailClick: () -> Unit,
    onTelegramClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_icon),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 24.dp, bottom = 8.dp)
                .size(160.dp),
        )

        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = stringResource(R.string.version), style = MaterialTheme.typography.bodyLarge)
            Text(text = versionName, style = MaterialTheme.typography.bodyLarge)
        }

        Text(
            text = stringResource(R.string.author_name),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp),
        )

        ContactRow(
            label = stringResource(R.string.author_email_label),
            value = email,
            onClick = onEmailClick,
        )
        ContactRow(
            label = stringResource(R.string.author_telegram_label),
            value = telegram,
            onClick = onTelegramClick,
        )
    }
}

@Composable
private fun ContactRow(
    label: String,
    value: String,
    onClick: () -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable(onClick = onClick),
        )
    }
}
