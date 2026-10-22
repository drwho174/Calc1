package com.drwho174.calc1.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

/** Допустимый ввод: цифры и один десятичный разделитель. */
private val DecimalInput = Regex("^\\d*[.,]?\\d*$")

/**
 * Числовое поле с подписью, единицами измерения и сообщением об ошибке.
 *
 * Ввод фильтруется на входе, поэтому в состояние экрана попадают только
 * валидные символы — это заменяет проверки `isNotEmpty()` из старых фрагментов.
 */
@Composable
fun NumberField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    suffix: String? = null,
    isError: Boolean = false,
    errorText: String? = null,
    imeAction: ImeAction = ImeAction.Next,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { candidate ->
            if (candidate.isEmpty() || DecimalInput.matches(candidate)) {
                onValueChange(candidate)
            }
        },
        modifier = modifier.fillMaxWidth(),
        label = { Text(label) },
        suffix = suffix?.let { { Text(it) } },
        singleLine = true,
        isError = isError,
        supportingText = if (isError && errorText != null) {
            { Text(errorText) }
        } else {
            null
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = imeAction,
        ),
    )
}
