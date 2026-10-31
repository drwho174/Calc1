package com.drwho174.calc1.ui.menu

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import com.drwho174.calc1.navigation.CalculatorId
import com.drwho174.calc1.navigation.calculatorDestinations
import com.drwho174.calc1.ui.theme.Calc1Theme
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = "ru-w411dp-h891dp-xhdpi")
class MainMenuScreenTest {

    @get:Rule
    val compose = createComposeRule()

    private var lastClicked: CalculatorId? = null
    private var lastToggled: CalculatorId? = null
    private var lastTab: MenuTab? = null

    private val favorites = setOf(CalculatorId.PERFUSIOLOGIST, CalculatorId.OXYGEN)

    private fun setMenu(
        tab: MenuTab = MenuTab.FAVORITES,
        favoriteIds: Set<CalculatorId> = favorites,
        darkTheme: Boolean = false,
    ) {
        compose.setContent {
            Calc1Theme(darkTheme = darkTheme) {
                MainMenuScreen(
                    calculators = calculatorDestinations,
                    favoriteIds = favoriteIds,
                    selectedTab = tab,
                    onTabSelected = { lastTab = it },
                    onToggleFavorite = { lastToggled = it },
                    onCalculatorClick = { lastClicked = it },
                )
            }
        }
    }

    @Test
    fun defaultTabIsAllWithoutFavorites() {
        assertEquals(MenuTab.ALL, defaultMenuTab(emptySet()))
    }

    @Test
    fun defaultTabIsFavoritesWhenNotEmpty() {
        assertEquals(MenuTab.FAVORITES, defaultMenuTab(favorites))
    }

    @Test
    fun favoritesTab_showsOnlyFavoriteCalculators() {
        setMenu(tab = MenuTab.FAVORITES)

        compose.onNodeWithText("Калькулятор перфузиолога").assertIsDisplayed()
        compose.onNodeWithText("Доставка кислорода").assertIsDisplayed()
        compose.onNodeWithText("EuroSCORE 2").assertDoesNotExist()
    }

    @Test
    fun allTab_showsAllCalculators() {
        setMenu(tab = MenuTab.ALL)

        compose.onNodeWithText("EuroSCORE 2").assertIsDisplayed()
        compose.onNodeWithText("Mehran Score").assertIsDisplayed()
        compose.onNodeWithText("СКФ").assertIsDisplayed()

        compose.onRoot().captureRoboImage("src/test/screenshots/main_menu_all_light.png")
    }

    @Test
    fun favoritesTab_screenshot() {
        setMenu(tab = MenuTab.FAVORITES)

        compose.onRoot().captureRoboImage("src/test/screenshots/main_menu_favorites_light.png")
    }

    @Test
    fun darkTheme_screenshot() {
        setMenu(tab = MenuTab.ALL, darkTheme = true)

        compose.onNodeWithText("EuroSCORE 2").assertIsDisplayed()
        compose.onRoot().captureRoboImage("src/test/screenshots/main_menu_all_dark.png")
    }

    @Test
    fun emptyFavorites_suggestsAllAndScreenshot() {
        setMenu(tab = MenuTab.FAVORITES, favoriteIds = emptySet())

        compose.onNodeWithText("В избранном пока пусто").assertIsDisplayed()
        compose.onRoot().captureRoboImage("src/test/screenshots/main_menu_empty_light.png")

        compose.onNodeWithText("Показать все калькуляторы").performClick()
        assertEquals(MenuTab.ALL, lastTab)
    }

    @Test
    fun tabsReportSelection() {
        setMenu(tab = MenuTab.ALL)

        compose.onNodeWithText("Избранное").performClick()
        assertEquals(MenuTab.FAVORITES, lastTab)

        compose.onNodeWithText("Все").performClick()
        assertEquals(MenuTab.ALL, lastTab)
    }

    @Test
    fun clickingCardOpensCalculator() {
        setMenu(tab = MenuTab.FAVORITES)

        compose.onNodeWithText("Доставка кислорода").performClick()
        assertEquals(CalculatorId.OXYGEN, lastClicked)
    }

    @Test
    fun starInFavoritesListRemovesCalculator() {
        setMenu(tab = MenuTab.FAVORITES)

        compose.onAllNodesWithContentDescription("Убрать из избранного")[0].performClick()
        assertEquals(CalculatorId.PERFUSIOLOGIST, lastToggled)
    }

    @Test
    fun starInAllListAddsCalculator() {
        setMenu(tab = MenuTab.ALL)

        compose.onNodeWithText("EuroSCORE 2").assertIsDisplayed()
        compose.onAllNodesWithContentDescription("Добавить в избранное")[0].performClick()
        assertEquals(CalculatorId.EUROSCORE, lastToggled)
    }

    @Test
    fun swipeSwitchesPagesAndReportsTab() {
        var tab by mutableStateOf(MenuTab.FAVORITES)
        compose.setContent {
            Calc1Theme(darkTheme = false) {
                MainMenuScreen(
                    calculators = calculatorDestinations,
                    favoriteIds = favorites,
                    selectedTab = tab,
                    onTabSelected = { tab = it },
                    onToggleFavorite = {},
                    onCalculatorClick = {},
                )
            }
        }

        compose.onNodeWithText("Калькулятор перфузиолога").assertIsDisplayed()

        // Свайп влево по странице: «Избранное» → «Все».
        compose.onRoot().performTouchInput { swipeLeft() }
        compose.mainClock.advanceTimeBy(1_000)
        compose.waitForIdle()

        compose.onNodeWithText("EuroSCORE 2").assertIsDisplayed()
        assertEquals(MenuTab.ALL, tab)
    }

    @Test
    fun starDescriptionFollowsState() {
        setMenu(tab = MenuTab.FAVORITES)

        // Оба избранных калькулятора помечены звездой, добавления нет ни у кого.
        compose.onAllNodesWithContentDescription("Убрать из избранного").assertCountEquals(2)
        compose.onAllNodesWithContentDescription("Добавить в избранное").assertCountEquals(0)
    }
}
