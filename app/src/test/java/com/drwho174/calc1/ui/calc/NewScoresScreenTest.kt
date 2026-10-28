package com.drwho174.calc1.ui.calc

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import com.drwho174.calc1.ui.calc.ariscat.AriscatScreen
import com.drwho174.calc1.ui.calc.charlson.CharlsonScreen
import com.drwho174.calc1.ui.calc.egri.ElGanzouriScreen
import com.drwho174.calc1.ui.calc.lee.LeeRiskScreen
import com.drwho174.calc1.ui.calc.sofa.SofaScreen
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
class NewScoresScreenTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun lee_defaultsToZeroPredictors() {
        compose.setContent { Calc1Theme { LeeRiskScreen() } }

        compose.onNodeWithText("0.4").assertExists()
        compose.onNodeWithText("Низкий").assertExists()

        compose.onAllNodes(hasSetTextAction()).assertCountEquals(0)
        compose.onRoot().captureRoboImage("src/test/screenshots/calc_lee.png")
    }

    @Test
    fun lee_predictorsRaiseRisk() {
        compose.setContent { Calc1Theme { LeeRiskScreen() } }

        compose.onNodeWithText("Высокорисковая операция").performClick()
        compose.onNodeWithText("Ишемическая болезнь сердца").performClick()
        compose.waitForIdle()

        // Два предиктора: 7 % и высокий риск.
        compose.onNodeWithText("7.0").assertExists()
        compose.onNodeWithText("Высокий").assertExists()
    }

    @Test
    fun explanationOpensOnInfoTap() {
        compose.setContent { Calc1Theme { LeeRiskScreen() } }

        compose.onAllNodesWithContentDescription("Показать пояснение")[0].performClick()
        compose.waitForIdle()

        compose.onNodeWithText(
            "Внутригрудная, внутрибрюшная или сосудистая операция выше паховой связки.",
        ).assertIsDisplayed()
    }

    @Test
    fun charlson_addsAgePointsToIndex() {
        compose.setContent { Calc1Theme { CharlsonScreen() } }

        compose.onAllNodes(hasSetTextAction())[0].performTextInput("70")
        compose.waitForIdle()

        // 70 лет — 3 балла, 10-летняя выживаемость 77 %.
        compose.onNodeWithText("77").assertExists()
        compose.onNodeWithText("Средний").assertExists()
        compose.onRoot().captureRoboImage("src/test/screenshots/calc_charlson.png")
    }

    @Test
    fun sofa_defaultsToZeroAndShowsMortalityRange() {
        compose.setContent { Calc1Theme { SofaScreen() } }

        compose.onNodeWithText("< 9").assertExists()
        compose.onNodeWithText("Низкий").assertExists()
        compose.onRoot().captureRoboImage("src/test/screenshots/calc_sofa.png")
    }

    @Test
    fun sofa_sumsSelectedCategories() {
        compose.setContent { Calc1Theme { SofaScreen() } }

        compose.onNodeWithText("< 200 при ИВЛ").performScrollTo().performClick()
        compose.waitForIdle()

        // Три балла за дыхание — диапазон летальности 9–16 %.
        compose.onNodeWithText("9–16").assertExists()
    }

    @Test
    fun ariscat_highRiskProfile() {
        compose.setContent { Calc1Theme { AriscatScreen() } }

        compose.onNodeWithText("> 80 лет").performScrollTo().performClick()
        compose.onNodeWithText("Торакотомия").performScrollTo().performClick()
        compose.waitForIdle()

        // 16 + 24 = 40 баллов — средний риск.
        compose.onNodeWithText("40").assertExists()
        compose.onNodeWithText("Средний").assertExists()
        compose.onRoot().captureRoboImage("src/test/screenshots/calc_ariscat.png")
    }

    @Test
    fun elGanzouri_highRiskProfile() {
        compose.setContent { Calc1Theme { ElGanzouriScreen() } }

        compose.onNodeWithText("IV").performScrollTo().performClick()
        compose.onNodeWithText("< 6 см").performScrollTo().performClick()
        compose.onNodeWithText("> 120 кг").performScrollTo().performClick()
        compose.waitForIdle()

        // 2 + 2 + 2 = 6 баллов — высокий класс риска.
        compose.onNodeWithText("6").assertExists()
        compose.onNodeWithText("Высокий").assertExists()
        compose.onRoot().captureRoboImage("src/test/screenshots/calc_egri.png")
    }
}
