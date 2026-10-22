package com.drwho174.calc1.ui.settings

import androidx.compose.ui.test.junit4.createComposeRule
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
class SettingsScreenTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun settings_lightTheme_switchOff() {
        compose.setContent {
            Calc1Theme(darkTheme = false) {
                SettingsScreen(darkTheme = false, onDarkThemeChange = {})
            }
        }

        compose.onNodeWithText("Тёмная тема").assertExists()
        compose.onRoot().captureRoboImage("src/test/screenshots/settings_light.png")
    }

    @Test
    fun settings_darkTheme_switchOn() {
        compose.setContent {
            Calc1Theme(darkTheme = true) {
                SettingsScreen(darkTheme = true, onDarkThemeChange = {})
            }
        }

        compose.onNodeWithText("Тёмная тема").assertExists()
        compose.onRoot().captureRoboImage("src/test/screenshots/settings_dark.png")
    }

    @Test
    fun tappingRowInvokesCallback() {
        var value: Boolean? = null
        compose.setContent {
            Calc1Theme {
                SettingsScreen(darkTheme = false, onDarkThemeChange = { value = it })
            }
        }

        compose.onNodeWithText("Тёмная тема").performClick()
        assertEquals(true, value)
    }
}
