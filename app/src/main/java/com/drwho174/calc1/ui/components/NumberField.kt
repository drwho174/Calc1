package com.drwho174.calc1.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/** Допустимый ввод: цифры и один десятичный разделитель. */
private val DecimalInput = Regex("^\\d*[.,]?\\d*$")

/**
 * Числовое поле с подписью, единицами измерения и сообщением об ошибке.
 *
 * @param labelAbove подпись выносится над полем. Нужно для длинных подписей:
 * во «плавающем» виде они переносятся и меняют высоту поля при вводе.
 * @param trailingIcon действие в правом краю поля (например, вызов калькулятора ППТ).
 *
 * Нажатие «Далее»/«Готово» скрывает клавиатуру и снимает фокус.
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
    labelAbove: Boolean = false,
    hintText: String? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val keyboardActions = KeyboardActions(
        onNext = {
            keyboardController?.hide()
            focusManager.clearFocus()
        },
        onDone = {
            keyboardController?.hide()
            focusManager.clearFocus()
        },
        onGo = {
            keyboardController?.hide()
            focusManager.clearFocus()
        },
    )

    if (labelAbove) {
        Column(modifier = modifier.fillMaxWidth()) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp),
            )
            FieldContent(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                label = null,
                suffix = suffix,
                isError = isError,
                errorText = errorText,
                hintText = hintText,
                imeAction = imeAction,
                trailingIcon = trailingIcon,
                keyboardActions = keyboardActions,
            )
        }
    } else {
        FieldContent(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.fillMaxWidth(),
            label = label,
            suffix = suffix,
            isError = isError,
            errorText = errorText,
            hintText = hintText,
            imeAction = imeAction,
            trailingIcon = trailingIcon,
            keyboardActions = keyboardActions,
        )
    }
}

@Composable
private fun FieldContent(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    label: String?,
    suffix: String?,
    isError: Boolean,
    errorText: String?,
    hintText: String?,
    imeAction: ImeAction,
    trailingIcon: (@Composable () -> Unit)?,
    keyboardActions: KeyboardActions,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { candidate ->
            if (candidate.isEmpty() || DecimalInput.matches(candidate)) {
                onValueChange(candidate)
            }
        },
        modifier = modifier,
        label = label?.let { { Text(it) } },
        suffix = suffix?.let { { Text(it) } },
        trailingIcon = trailingIcon,
        singleLine = true,
        isError = isError,
        supportingText = when {
            isError && errorText != null -> { { Text(errorText) } }
            hintText != null -> { { Text(hintText) } }
            else -> null
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = imeAction,
        ),
        keyboardActions = keyboardActions,
    )
}
