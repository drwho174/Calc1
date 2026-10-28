package com.drwho174.calc1.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.drwho174.calc1.R

/**
 * «4 балла», «1 балл», «12 баллов» — русская форма слова по числу.
 *
 * Обычные `plurals` здесь не подходят: вне русской локали Android выбирает
 * форму `other`, и на скриншотах появлялось бы «40 балла».
 */
@Composable
fun pointsLabel(count: Int): String {
    val word = when {
        count % 10 == 1 && count % 100 != 11 -> stringResource(R.string.point_one)
        count % 10 in 2..4 && count % 100 !in 12..14 -> stringResource(R.string.point_few)
        else -> stringResource(R.string.point_many)
    }
    return "$count $word"
}
