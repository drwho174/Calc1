package com.drwho174.calc1.ui.calc

import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.drwho174.calc1.ui.calc.gfr.GfrScreen
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
class GfrScreenTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun male60_years_creatinine88_shows86_16() {
        compose.setContent {
            Calc1Theme { GfrScreen() }
        }

        // Порядок полей: возраст, креатинин.
        compose.onAllNodes(hasSetTextAction())[0].performTextInput("60")
        compose.onAllNodes(hasSetTextAction())[1].performTextInput("88.4055")
        compose.waitForIdle()

        compose.onNodeWithText("86.16").assertExists()
        compose.onRoot().captureRoboImage("src/test/screenshots/calc_gfr.png")
    }

    @Test
    fun female70_years_creatinine70_shows80_20() {
        compose.setContent {
            Calc1Theme { GfrScreen() }
        }

        compose.onNodeWithText("Женщина").performClick()
        compose.onAllNodes(hasSetTextAction())[0].performTextInput("70")
        compose.onAllNodes(hasSetTextAction())[1].performTextInput("70")
        compose.waitForIdle()

        compose.onNodeWithText("80.20").assertExists()
    }
}
