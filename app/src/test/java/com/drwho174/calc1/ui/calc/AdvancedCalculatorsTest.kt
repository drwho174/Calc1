package com.drwho174.calc1.ui.calc

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
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
    fun oxygenDelivery_commaDecimals_produceResults() {
        compose.setContent {
            Calc1Theme { OxygenDeliveryScreen() }
        }

        // Значения из реального сценария: ППТ введена с запятой.
        listOf("1,97", "5", "100", "100", "200", "50", "5").forEachIndexed { index, value ->
            compose.onAllNodes(hasSetTextAction())[index].performTextInput(value)
        }
        compose.waitForIdle()

        compose.onNodeWithText("14.0").assertExists()
        compose.onNodeWithText("355.3").assertExists()
        compose.onNodeWithText("2.1").assertExists()
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
    fun oxygenBsaDialogFillsBsaField() {
        compose.setContent {
            Calc1Theme { OxygenDeliveryScreen() }
        }

        compose.onNodeWithContentDescription("Площадь поверхности тела").performClick()
        compose.waitForIdle()
        compose.mainClock.advanceTimeBy(1_000)
        compose.waitForIdle()

        // Два поля диалога идут после семи полей экрана.
        compose.onAllNodes(hasSetTextAction())[7].performTextInput("170")
        compose.onAllNodes(hasSetTextAction())[8].performTextInput("70")
        compose.waitForIdle()

        compose.onNodeWithText("1.81").assertExists()
        compose.onNodeWithText("Применить").performClick()
        compose.waitForIdle()
        compose.mainClock.advanceTimeBy(1_000)
        compose.waitForIdle()

        // Значение подставлено в поле «ППТ».
        compose.onNodeWithText("1.81").assertExists()
    }

    @Test
    fun euroScore_70YearsMaleDefaultRisk() {
        compose.setContent {
            Calc1Theme(darkTheme = false) { EuroScoreScreen() }
        }

        compose.onAllNodes(hasSetTextAction())[0].performTextInput("70")
        compose.waitForIdle()

        compose.onNodeWithText("0.66").assertExists()
        compose.onRoot().captureRoboImage("src/test/screenshots/calc_euroscore.png")
    }

    @Test
    fun euroScore_darkTheme_pinnedResultBar() {
        compose.setContent {
            Calc1Theme(darkTheme = true) { EuroScoreScreen() }
        }

        compose.onAllNodes(hasSetTextAction())[0].performTextInput("70")
        compose.waitForIdle()

        compose.onNodeWithText("0.66").assertExists()
        compose.onRoot().captureRoboImage("src/test/screenshots/calc_euroscore_dark.png")
    }

    @Test
    fun euroScore_scrolled_panelStaysPinned() {
        compose.setContent {
            Calc1Theme(darkTheme = false) { EuroScoreScreen() }
        }

        compose.onAllNodes(hasSetTextAction())[0].performTextInput("70")
        compose.waitForIdle()

        // Прокручиваем к последнему фактору: панель результата должна остаться на месте.
        compose.onNodeWithText("Вмешательство на грудной аорте").performScrollTo()
        compose.waitForIdle()

        compose.onNodeWithText("0.66").assertExists()
        compose.onRoot().captureRoboImage("src/test/screenshots/calc_euroscore_scrolled.png")
    }
}
