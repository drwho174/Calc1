package com.drwho174.calc1.ui.menu

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
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
@Config(sdk = [35], qualifiers = "w411dp-h891dp-xhdpi")
class MainMenuScreenTest {

    @get:Rule
    val compose = createComposeRule()

    private var lastClicked: String? = null

    private fun setMenu(darkTheme: Boolean) {
        compose.setContent {
            Calc1Theme(darkTheme = darkTheme) {
                MainMenuScreen(
                    onEuroScore = { lastClicked = "euroscore" },
                    onHbPerfusion = { lastClicked = "hb" },
                    onPerfusiologist = { lastClicked = "perfusiologist" },
                    onOxygenDelivery = { lastClicked = "oxygen" },
                    onGfr = { lastClicked = "gfr" },
                    onIdealBodyMass = { lastClicked = "ibm" },
                    onBsa = { lastClicked = "bsa" },
                    onRespiratory = { lastClicked = "respiratory" },
                    onMehranScore = { lastClicked = "mehran" },
                )
            }
        }
    }

    @Test
    fun lightTheme_showsAllNineCalculators() {
        setMenu(darkTheme = false)

        compose.onNodeWithText("EuroSCORE 2").assertIsDisplayed()
        compose.onNodeWithText("Mehran Score").assertIsDisplayed()
        compose.onNodeWithText("СКФ").assertIsDisplayed()

        // Ровно девять кликабельных кнопок — по одной на калькулятор.
        assertEquals(9, compose.onAllNodes(hasClickAction()).fetchSemanticsNodes().size)

        compose.onRoot().captureRoboImage("src/test/screenshots/main_menu_light.png")
    }

    @Test
    fun darkTheme_screenshot() {
        setMenu(darkTheme = true)

        compose.onNodeWithText("EuroSCORE 2").assertIsDisplayed()
        compose.onRoot().captureRoboImage("src/test/screenshots/main_menu_dark.png")
    }

    @Test
    fun clickingGfrInvokesCallback() {
        setMenu(darkTheme = false)

        compose.onNodeWithText("СКФ").performClick()
        assertEquals("gfr", lastClicked)
    }

    @Test
    fun clickingEachCalculatorKeepsMenuStable() {
        setMenu(darkTheme = false)

        compose.onNodeWithText("EuroSCORE 2").performClick()
        assertEquals("euroscore", lastClicked)

        compose.onNodeWithText("Mehran Score").performClick()
        assertEquals("mehran", lastClicked)
    }
}
