package com.drwho174.calc1.ui.about

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onRoot
import com.drwho174.calc1.ui.theme.Calc1Theme
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Справка рендерится Markwon: проверяем, что markdown с формулами LaTeX
 * и таблицами доезжает до экрана без потерь.
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = "w411dp-h891dp-xhdpi")
class AboutScreensTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun gfrAbout_light_showsLatexFormula() {
        compose.setContent {
            Calc1Theme(darkTheme = false) {
                AboutScreen(assetPath = "GFR_about.md")
            }
        }

        compose.waitForIdle()
        compose.onRoot().captureRoboImage("src/test/screenshots/about_gfr_light.png")
    }

    @Test
    fun mehranAbout_light_showsScoreTable() {
        compose.setContent {
            Calc1Theme(darkTheme = false) {
                AboutScreen(assetPath = "MehraneScore.md")
            }
        }

        compose.waitForIdle()
        compose.onRoot().captureRoboImage("src/test/screenshots/about_mehran_light.png")
    }

    @Test
    fun gfrAbout_dark() {
        compose.setContent {
            Calc1Theme(darkTheme = true) {
                AboutScreen(assetPath = "GFR_about.md")
            }
        }

        compose.waitForIdle()
        compose.onRoot().captureRoboImage("src/test/screenshots/about_gfr_dark.png")
    }

    @Test
    fun appAbout_showsVersionAndContacts() {
        compose.setContent {
            Calc1Theme(darkTheme = false) {
                AboutAppScreen(
                    versionName = "1.4",
                    email = "filipphome@mail.ru",
                    telegram = "drwho174",
                    onEmailClick = {},
                    onTelegramClick = {},
                )
            }
        }

        compose.waitForIdle()
        compose.onRoot().captureRoboImage("src/test/screenshots/about_app_light.png")
    }
}
