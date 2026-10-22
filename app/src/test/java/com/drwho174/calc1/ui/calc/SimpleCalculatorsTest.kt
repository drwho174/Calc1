package com.drwho174.calc1.ui.calc

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.drwho174.calc1.ui.calc.bsa.BsaScreen
import com.drwho174.calc1.ui.calc.ibm.IdealBodyMassScreen
import com.drwho174.calc1.ui.calc.respiratory.RespiratoryScreen
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
class SimpleCalculatorsTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun bsa_170x70_showsAllThreeFormulas() {
        compose.setContent {
            Calc1Theme { BsaScreen() }
        }

        compose.onAllNodes(hasSetTextAction())[0].performTextInput("170")
        compose.onAllNodes(hasSetTextAction())[1].performTextInput("70")
        compose.waitForIdle()

        // 1.8217 / 1.8097 / 1.8257 — все три округляются до 1.8 м².
        compose.onAllNodesWithText("1.8 м²").assertCountEquals(3)
        compose.onRoot().captureRoboImage("src/test/screenshots/calc_bsa.png")
    }

    @Test
    fun idealBodyMass_female180_shows70_6() {
        compose.setContent {
            Calc1Theme { IdealBodyMassScreen() }
        }

        compose.onAllNodes(hasSetTextAction())[0].performTextInput("180")
        compose.onNodeWithText("Женщина").performClick()
        compose.waitForIdle()

        compose.onNodeWithText("70.6 кг").assertExists()
        compose.onRoot().captureRoboImage("src/test/screenshots/calc_ibm.png")
    }

    @Test
    fun respiratory_60x100x40x98_showsFourResults() {
        compose.setContent {
            Calc1Theme { RespiratoryScreen() }
        }

        // Порядок полей: PaO2, PaCO2, FiO2, SpO2.
        compose.onAllNodes(hasSetTextAction())[0].performTextInput("100")
        compose.onAllNodes(hasSetTextAction())[1].performTextInput("40")
        compose.onAllNodes(hasSetTextAction())[2].performTextInput("60")
        compose.onAllNodes(hasSetTextAction())[3].performTextInput("98")
        compose.waitForIdle()

        compose.onNodeWithText("167").assertExists()
        compose.onNodeWithText("163.3").assertExists()
        compose.onNodeWithText("277.8 мм рт.ст.").assertExists()
        compose.onNodeWithText("2.8").assertExists()
        compose.onRoot().captureRoboImage("src/test/screenshots/calc_respiratory.png")
    }
}
