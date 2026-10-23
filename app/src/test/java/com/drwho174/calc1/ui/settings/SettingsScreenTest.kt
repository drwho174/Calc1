package com.drwho174.calc1.ui.settings

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import com.drwho174.calc1.data.ThemeMode
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
    fun settings_systemMode_selectedByDefault() {
        compose.setContent {
            Calc1Theme(darkTheme = false) {
                SettingsScreen(themeMode = ThemeMode.SYSTEM, onThemeModeChange = {})
            }
        }

        compose.onNodeWithText("Как на устройстве").assertExists()
        compose.onNodeWithText("Светлая").assertExists()
        compose.onNodeWithText("Тёмная").assertExists()
        compose.onRoot().captureRoboImage("src/test/screenshots/settings_system.png")
    }

    @Test
    fun settings_lightMode() {
        compose.setContent {
            Calc1Theme(darkTheme = false) {
                SettingsScreen(themeMode = ThemeMode.LIGHT, onThemeModeChange = {})
            }
        }

        compose.onNodeWithText("Тема оформления").assertExists()
        compose.onRoot().captureRoboImage("src/test/screenshots/settings_light.png")
    }

    @Test
    fun settings_darkMode() {
        compose.setContent {
            Calc1Theme(darkTheme = true) {
                SettingsScreen(themeMode = ThemeMode.DARK, onThemeModeChange = {})
            }
        }

        compose.onNodeWithText("Тема оформления").assertExists()
        compose.onRoot().captureRoboImage("src/test/screenshots/settings_dark.png")
    }

    @Test
    fun tappingSystemOptionInvokesCallbackWithSystemMode() {
        var selected: ThemeMode? = null
        compose.setContent {
            Calc1Theme {
                SettingsScreen(
                    themeMode = ThemeMode.LIGHT,
                    onThemeModeChange = { selected = it },
                )
            }
        }

        compose.onNodeWithText("Как на устройстве").performClick()
        assertEquals(ThemeMode.SYSTEM, selected)
    }

    @Test
    fun tappingDarkOptionInvokesCallbackWithDarkMode() {
        var selected: ThemeMode? = null
        compose.setContent {
            Calc1Theme {
                SettingsScreen(
                    themeMode = ThemeMode.SYSTEM,
                    onThemeModeChange = { selected = it },
                )
            }
        }

        compose.onNodeWithText("Тёмная").performClick()
        assertEquals(ThemeMode.DARK, selected)
    }
}
