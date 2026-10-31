package com.drwho174.calc1.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
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
@Config(sdk = [35], qualifiers = "ru-w411dp-h891dp-xhdpi")
class ComponentsScreenshotTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun componentsGallery_light() {
        compose.setContent {
            Calc1Theme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    NumberField(
                        value = "170",
                        onValueChange = {},
                        label = "Рост",
                        suffix = "см",
                    )
                    NumberField(
                        value = "",
                        onValueChange = {},
                        label = "Креатинин",
                        suffix = "мкмоль/л",
                        isError = true,
                        errorText = "Введите значение",
                    )
                    ChoiceGroup(
                        title = "Пол",
                        options = listOf("Мужчина", "Женщина"),
                        selected = "Мужчина",
                        onSelect = {},
                        label = { it },
                    )
                    ResultCard(title = "СКФ", value = "86.16 мл/мин/1.73м²")
                    ResultCard(
                        title = "DO2i",
                        value = "446.1 мл/мин/м²",
                        tone = ResultTone.Positive,
                    )
                    ResultCard(
                        title = "DO2i/VCO2i",
                        value = "4.2",
                        tone = ResultTone.Negative,
                    )
                }
            }
        }

        compose.onRoot().captureRoboImage("src/test/screenshots/components_light.png")
    }

    @Test
    fun componentsGallery_dark() {
        compose.setContent {
            Calc1Theme(darkTheme = true) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    NumberField(
                        value = "170",
                        onValueChange = {},
                        label = "Рост",
                        suffix = "см",
                    )
                    ChoiceGroup(
                        title = "Пол",
                        options = listOf("Мужчина", "Женщина"),
                        selected = "Женщина",
                        onSelect = {},
                        label = { it },
                    )
                    ResultCard(title = "СКФ", value = "86.16 мл/мин/1.73м²")
                }
            }
        }

        compose.onRoot().captureRoboImage("src/test/screenshots/components_dark.png")
    }
}
