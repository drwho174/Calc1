package com.drwho174.calc1.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import com.drwho174.calc1.data.ThemeMode
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
@Config(sdk = [35], qualifiers = "w411dp-h891dp-xhdpi")
class NavigationTest {

    @get:Rule
    val compose = createComposeRule()

    private fun setApp() {
        compose.setContent {
            Calc1Theme(darkTheme = false) {
                Calc1App(themeMode = ThemeMode.SYSTEM, onThemeModeChange = {})
            }
        }
    }

    private fun settle() {
        compose.waitForIdle()
        compose.mainClock.advanceTimeBy(1_000)
        compose.waitForIdle()
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
    fun backStackSurvivesStateRestoration() {
        val restorationTester = StateRestorationTester(compose)
        restorationTester.setContent {
            Calc1Theme(darkTheme = false) {
                Calc1App(themeMode = ThemeMode.SYSTEM, onThemeModeChange = {})
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
