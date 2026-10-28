package com.drwho174.calc1.ui.menu

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drwho174.calc1.R
import com.drwho174.calc1.navigation.CalculatorDestination
import com.drwho174.calc1.navigation.CalculatorId
import com.drwho174.calc1.navigation.calculatorDestinations
import com.drwho174.calc1.ui.theme.Calc1Theme

/** Вкладки главного экрана. Порядок задаёт порядок сегментов. */
enum class MenuTab(
    @StringRes val labelRes: Int,
    @DrawableRes val iconRes: Int,
) {
    FAVORITES(R.string.tab_favorites, R.drawable.ic_star_filled),
    ALL(R.string.tab_all, R.drawable.ic_list),
}

/**
 * Вкладка, которую нужно показать, пока пользователь не выбрал другую сам:
 * избранное, если оно не пусто, иначе — полный список (пустой экран избранного
 * на старте выглядит как ошибка).
 */
fun defaultMenuTab(favoriteIds: Set<CalculatorId>): MenuTab =
    if (favoriteIds.isEmpty()) MenuTab.ALL else MenuTab.FAVORITES

/**
 * Главное меню: переключатель разделов и ленивый список карточек.
 *
 * Экран без собственного состояния — выбранная вкладка и избранное приходят
 * снаружи, поэтому его удобно проверять в тестах и превью.
 *
 * [calculators] — полный список; порядок карточек на обеих вкладках один и тот же.
 */
@Composable
fun MainMenuScreen(
    calculators: List<CalculatorDestination>,
    favoriteIds: Set<CalculatorId>,
    selectedTab: MenuTab,
    onTabSelected: (MenuTab) -> Unit,
    onToggleFavorite: (CalculatorId) -> Unit,
    onCalculatorClick: (CalculatorId) -> Unit,
    modifier: Modifier = Modifier,
) {
    val visibleCalculators = if (selectedTab == MenuTab.FAVORITES) {
        calculators.filter { it.id in favoriteIds }
    } else {
        calculators
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        MenuTabs(
            selected = selectedTab,
            onSelect = onTabSelected,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 12.dp),
        )

        if (visibleCalculators.isEmpty() && selectedTab == MenuTab.FAVORITES) {
            EmptyFavorites(
                onShowAll = { onTabSelected(MenuTab.ALL) },
                modifier = Modifier.weight(1f),
            )
        } else {
            CalculatorList(
                calculators = visibleCalculators,
                favoriteIds = favoriteIds,
                onCalculatorClick = onCalculatorClick,
                onToggleFavorite = onToggleFavorite,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MenuTabs(
    selected: MenuTab,
    onSelect: (MenuTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabs = MenuTab.entries
    SingleChoiceSegmentedButtonRow(modifier = modifier.fillMaxWidth()) {
        tabs.forEachIndexed { index, tab ->
            SegmentedButton(
                selected = tab == selected,
                onClick = { onSelect(tab) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = tabs.size),
                icon = {
                    // Слот иконки не наследует цвет сегмента, поэтому тон задаём явно:
                    // он совпадает с SegmentedButtonDefaults.colors().
                    Icon(
                        painter = painterResource(tab.iconRes),
                        contentDescription = null,
                        tint = if (tab == selected) {
                            MaterialTheme.colorScheme.onSecondaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                        modifier = Modifier.size(18.dp),
                    )
                },
                label = { Text(text = stringResource(tab.labelRes)) },
            )
        }
    }
}

@Composable
private fun CalculatorList(
    calculators: List<CalculatorDestination>,
    favoriteIds: Set<CalculatorId>,
    onCalculatorClick: (CalculatorId) -> Unit,
    onToggleFavorite: (CalculatorId) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        // Ключ по идентификатору: при смене вкладки и правке избранного Compose
        // переиспользует карточки, а не пересоздаёт список целиком.
        items(items = calculators, key = { it.id }) { calculator ->
            CalculatorCard(
                title = stringResource(calculator.titleRes),
                subtitle = stringResource(calculator.subtitleRes),
                isFavorite = calculator.id in favoriteIds,
                onClick = { onCalculatorClick(calculator.id) },
                onToggleFavorite = { onToggleFavorite(calculator.id) },
            )
        }
    }
}

@Composable
private fun CalculatorCard(
    title: String,
    subtitle: String,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onClick)
                .heightIn(min = 72.dp)
                .padding(start = 16.dp, end = 4.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    painter = painterResource(
                        if (isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_outline,
                    ),
                    contentDescription = stringResource(
                        if (isFavorite) R.string.action_remove_favorite else R.string.action_add_favorite,
                    ),
                    tint = if (isFavorite) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline
                    },
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}

@Composable
private fun EmptyFavorites(
    onShowAll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_star_outline),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(34.dp),
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.favorites_empty_title),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.favorites_empty_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedButton(onClick = onShowAll) {
            Text(text = stringResource(R.string.favorites_show_all))
        }
    }
}

@Preview(showBackground = true, name = "Избранное")
@Composable
private fun MainMenuFavoritesPreview() {
    Calc1Theme {
        MainMenuScreen(
            calculators = calculatorDestinations,
            favoriteIds = setOf(CalculatorId.PERFUSIOLOGIST, CalculatorId.OXYGEN),
            selectedTab = MenuTab.FAVORITES,
            onTabSelected = {},
            onToggleFavorite = {},
            onCalculatorClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Все калькуляторы")
@Composable
private fun MainMenuAllPreview() {
    Calc1Theme {
        MainMenuScreen(
            calculators = calculatorDestinations,
            favoriteIds = setOf(CalculatorId.PERFUSIOLOGIST, CalculatorId.OXYGEN),
            selectedTab = MenuTab.ALL,
            onTabSelected = {},
            onToggleFavorite = {},
            onCalculatorClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Пустое избранное")
@Composable
private fun MainMenuEmptyPreview() {
    Calc1Theme {
        MainMenuScreen(
            calculators = calculatorDestinations,
            favoriteIds = emptySet(),
            selectedTab = MenuTab.FAVORITES,
            onTabSelected = {},
            onToggleFavorite = {},
            onCalculatorClick = {},
        )
    }
}
