package com.drwho174.calc1.ui.calc

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.drwho174.calc1.ui.calc.euroscore.EuroScoreScreen
import com.drwho174.calc1.ui.calc.hb.HbPerfusionScreen
import com.drwho174.calc1.ui.calc.mehran.MehranScoreScreen
import com.drwho174.calc1.ui.calc.oxygen.OxygenDeliveryScreen
import com.drwho174.calc1.ui.calc.perfusiologist.PerfusiologistScreen
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
class AdvancedCalculatorsTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun oxygenDelivery_referenceScenario() {
        compose.setContent {
            Calc1Theme { OxygenDeliveryScreen() }
        }

        // BSA, сердечный выброс, Hb, SpO2, PaO2, EtCO2, поток свежего газа.
        listOf("1.8", "5", "120", "98", "100", "35", "2").forEachIndexed { index, value ->
            compose.onAllNodes(hasSetTextAction())[index].performTextInput(value)
        }
        compose.waitForIdle()

        compose.onNodeWithText("446.1").assertExists()
        compose.onNodeWithText("8.7").assertExists()
        compose.onRoot().captureRoboImage("src/test/screenshots/calc_oxygen.png")
    }

    @Test
    fun hbPerfusion_referenceScenario() {
        compose.setContent {
            Calc1Theme { HbPerfusionScreen() }
        }

        listOf("170", "70", "120", "1000", "500", "500", "300", "1500")
            .forEachIndexed { index, value ->
                compose.onAllNodes(hasSetTextAction())[index].performTextInput(value)
            }
        compose.waitForIdle()

        compose.onNodeWithText("108.39").assertExists()
        compose.onNodeWithText("82.86").assertExists()
        compose.onRoot().captureRoboImage("src/test/screenshots/calc_hb.png")
    }

    @Test
    fun perfusiologist_defaultsAfterHeightAndWeight() {
        compose.setContent {
            Calc1Theme { PerfusiologistScreen() }
        }

        compose.onAllNodes(hasSetTextAction())[0].performTextInput("170")
        compose.onAllNodes(hasSetTextAction())[1].performTextInput("70")
        compose.waitForIdle()

        compose.onNodeWithText("1.810").assertExists()
        // Скорость видна дважды: в карточке результата и в подписи слайдера.
        compose.onAllNodesWithText("4.34").assertCountEquals(2)
        compose.onRoot().captureRoboImage("src/test/screenshots/calc_perfusiologist.png")
    }

    @Test
    fun mehran_contrastHighAndMiddleEgfr() {
        compose.setContent {
            Calc1Theme { MehranScoreScreen() }
        }

        compose.onAllNodes(hasSetTextAction())[0].performTextInput("250")
        compose.onNodeWithText("Застойная сердечная недостаточность").performClick()
        compose.onNodeWithText("40–20").performClick()
        compose.waitForIdle()

        // 3 балла контраста + 5 за СН + 4 за СКФ = 12 → 26.1 % и 1.09 %.
        compose.onNodeWithText("26.1").assertExists()
        compose.onNodeWithText("1.1").assertExists()
        compose.onRoot().captureRoboImage("src/test/screenshots/calc_mehran.png")
    }

    @Test
    fun euroScore_70YearsMaleDefaultRisk() {
        compose.setContent {
            Calc1Theme { EuroScoreScreen() }
        }

        compose.onAllNodes(hasSetTextAction())[0].performTextInput("70")
        compose.waitForIdle()

        compose.onNodeWithText("0.66").assertExists()
        compose.onRoot().captureRoboImage("src/test/screenshots/calc_euroscore.png")
    }
}
