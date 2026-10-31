package com.drwho174.calc1.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import com.drwho174.calc1.data.ThemeMode
import com.drwho174.calc1.navigation.CalculatorId
import com.drwho174.calc1.ui.theme.Calc1Theme
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = "ru-w411dp-h891dp-xhdpi")
class NavigationTest {

    @get:Rule
    val compose = createComposeRule()

    private fun setApp(
        darkTheme: Boolean = false,
        favorites: Set<CalculatorId> = emptySet(),
    ) {
        compose.setContent {
            Calc1Theme(darkTheme = darkTheme) {
                Calc1App(
                    themeMode = ThemeMode.SYSTEM,
                    onThemeModeChange = {},
                    favoriteIds = favorites,
                    onToggleFavorite = {},
                )
            }
        }
    }

    private fun settle() {
        compose.waitForIdle()
        compose.mainClock.advanceTimeBy(1_000)
        compose.waitForIdle()
    }

    /**
     * Приложение с живым избранным: нажатие на звезду реально меняет набор,
     * поэтому видно, переключается ли вкладка вслед за ним.
     */
    private fun setAppWithTogglingFavorites(initial: Set<CalculatorId>) {
        var favorites by mutableStateOf(initial)
        compose.setContent {
            Calc1Theme(darkTheme = false) {
                Calc1App(
                    themeMode = ThemeMode.SYSTEM,
                    onThemeModeChange = {},
                    favoriteIds = favorites,
                    onToggleFavorite = { id ->
                        favorites = if (id in favorites) favorites - id else favorites + id
                    },
                )
            }
        }
    }

    @Test
    fun startsOnMainMenuWithTopBar() {
        setApp()
        settle()

        compose.onNodeWithText("EuroSCORE 2").assertIsDisplayed()
        compose.onNodeWithText("Главное меню").assertIsDisplayed()
        compose.onRoot().captureRoboImage("src/test/screenshots/nav_menu.png")
    }

    @Test
    fun opensCalculatorAndReturnsBack() {
        setApp()
        settle()

        compose.onNodeWithText("EuroSCORE 2").performClick()
        settle()
        compose.onNodeWithText("Возраст").assertIsDisplayed()
        compose.onRoot().captureRoboImage("src/test/screenshots/nav_calculator.png")

        compose.onNodeWithContentDescription("Назад").performClick()
        settle()
        compose.onNodeWithText("EuroSCORE 2").assertIsDisplayed()
    }

    @Test
    fun opensSettingsFromTopBar() {
        setApp()
        settle()

        compose.onNodeWithContentDescription("Настройки").performClick()
        settle()
        compose.onNodeWithText("Тема оформления").assertIsDisplayed()
    }

    @Test
    fun opensCalculatorHelpFromTopBar() {
        setApp()
        settle()

        compose.onNodeWithText("СКФ").performClick()
        settle()
        compose.onNodeWithContentDescription("О калькуляторе").performClick()
        settle()
        compose.onNodeWithText("О калькуляторе").assertIsDisplayed()
    }

    @Test
    fun startsOnFavoritesWhenUserHasThem() {
        setApp(favorites = setOf(CalculatorId.OXYGEN))
        settle()

        compose.onNodeWithText("Доставка кислорода").assertIsDisplayed()
        compose.onNodeWithText("EuroSCORE 2").assertDoesNotExist()
        compose.onRoot().captureRoboImage("src/test/screenshots/nav_menu_favorites.png")
    }

    @Test
    fun emptyFavoritesTabShowsHint() {
        setApp()
        settle()

        compose.onNodeWithText("Избранное").performClick()
        settle()

        compose.onNodeWithText("В избранном пока пусто").assertIsDisplayed()
        compose.onRoot().captureRoboImage("src/test/screenshots/nav_menu_empty.png")
    }

    @Test
    fun chosenTabSurvivesCalculatorRoundTrip() {
        setApp(favorites = setOf(CalculatorId.OXYGEN))
        settle()

        compose.onNodeWithText("Все").performClick()
        settle()
        compose.onNodeWithText("EuroSCORE 2").performClick()
        settle()
        compose.onNodeWithContentDescription("Назад").performClick()
        settle()

        // Если бы выбор вкладки терялся, открылось бы избранное без EuroSCORE 2.
        compose.onNodeWithText("EuroSCORE 2").assertIsDisplayed()
    }

    @Test
    fun starringOnAllTabKeepsTheList() {
        setAppWithTogglingFavorites(initial = emptySet())
        settle()

        // Избранное пусто → открыт «Все».
        compose.onNodeWithText("EuroSCORE 2").assertIsDisplayed()

        compose.onAllNodesWithContentDescription("Добавить в избранное")[0].performClick()
        settle()

        // Экран не дёрнулся: по-прежнему весь список, звезда закрасилась.
        compose.onNodeWithText("EuroSCORE 2").assertIsDisplayed()
        compose.onNodeWithText("Mehran Score").assertIsDisplayed()
        compose.onAllNodesWithContentDescription("Убрать из избранного").assertCountEquals(1)
        compose.onRoot().captureRoboImage("src/test/screenshots/nav_menu_after_star.png")
    }

    @Test
    fun unstarringLastFavoriteKeepsPlaceholder() {
        setAppWithTogglingFavorites(initial = setOf(CalculatorId.OXYGEN))
        settle()

        compose.onNodeWithText("Доставка кислорода").assertIsDisplayed()

        compose.onNodeWithContentDescription("Убрать из избранного").performClick()
        settle()

        // Вкладка осталась «Избранное»: видна заглушка, полного списка нет.
        compose.onNodeWithText("В избранном пока пусто").assertIsDisplayed()
        compose.onNodeWithText("EuroSCORE 2").assertDoesNotExist()
        compose.onRoot().captureRoboImage("src/test/screenshots/nav_menu_empty_after_unstar.png")
    }

    @Test
    fun darkThemeScreenshot() {
        setApp(darkTheme = true)
        settle()

        compose.onRoot().captureRoboImage("src/test/screenshots/nav_menu_dark.png")
    }

    @Test
    fun backStackSurvivesStateRestoration() {
        val restorationTester = StateRestorationTester(compose)
        restorationTester.setContent {
            Calc1Theme(darkTheme = false) {
                Calc1App(
                    themeMode = ThemeMode.SYSTEM,
                    onThemeModeChange = {},
                    favoriteIds = emptySet(),
                    onToggleFavorite = {},
                )
            }
        }
        settle()

        compose.onNodeWithText("EuroSCORE 2").performClick()
        settle()
        compose.onNodeWithText("Возраст").assertIsDisplayed()

        // Проверяет сериализацию ключей NavKey: если ключи не сохраняются,
        // восстановление упадёт или вернёт меню вместо калькулятора.
        restorationTester.emulateSavedInstanceStateRestore()
        settle()

        compose.onNodeWithText("Возраст").assertIsDisplayed()
    }
}
