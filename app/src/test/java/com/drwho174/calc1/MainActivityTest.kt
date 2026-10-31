package com.drwho174.calc1

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Проверяет, что Activity на платформенной теме (без AppCompat) запускается
 * и показывает Compose-навигацию.
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = "ru-w411dp-h891dp-xhdpi")
class MainActivityTest {

    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun launchesOnMainMenu() {
        compose.waitForIdle()
        compose.onNodeWithText("EuroSCORE 2").assertIsDisplayed()
    }

    @Test
    fun navigatesToCalculator() {
        compose.waitForIdle()
        compose.onNodeWithText("СКФ").performClick()
        compose.waitForIdle()
        compose.onNodeWithText("Креатинин").assertIsDisplayed()
    }
}
