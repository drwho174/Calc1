package com.drwho174.calc1

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.drwho174.calc1.data.SettingsRepository
import com.drwho174.calc1.navigation.CalculatorId
import com.drwho174.calc1.navigation.storageKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Дымовые тесты на реальном устройстве/эмуляторе: запуск Activity,
 * навигация по меню, расчёт и возврат назад.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {

    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun mainMenuIsShown() {
        compose.onNodeWithText("Главное меню").assertIsDisplayed()
        compose.onNodeWithText("Площадь поверхности тела").assertIsDisplayed()
        compose.onNodeWithText("EuroSCORE 2").assertIsDisplayed()
    }

    @Test
    fun bsaCalculatesAndBackReturnsToMenu() {
        compose.onNodeWithText("Площадь поверхности тела").performClick()
        compose.waitForIdle()

        compose.onAllNodes(hasSetTextAction())[0].performTextInput("170")
        compose.onAllNodes(hasSetTextAction())[1].performTextInput("70")
        compose.waitForIdle()

        // Все три формулы посчитаны — заглушек с прочерком не осталось.
        compose.onAllNodesWithText("—").assertCountEquals(0)
        compose.onNodeWithText("По Мостлеру, м²").assertIsDisplayed()

        compose.onNodeWithContentDescription("Назад").performClick()
        compose.waitForIdle()

        compose.onNodeWithText("Mehran Score").assertIsDisplayed()
    }

    @Test
    fun euroScoreShowsPinnedResultBar() {
        compose.onNodeWithText("EuroSCORE 2").performClick()
        compose.waitForIdle()

        compose.onAllNodes(hasSetTextAction())[0].performTextInput("70")
        compose.waitForIdle()

        compose.onNodeWithText("0.66").assertIsDisplayed()
    }

    /**
     * Звезда должна реально доезжать до DataStore: именно по этому значению
     * следующий запуск приложения выбирает стартовую вкладку.
     *
     * Activity пересоздавать нельзя: `scenario.recreate()` возвращает
     * сохранённое состояние (вкладка восстанавливается), а не холодный старт.
     */
    @Test
    fun favoriteIsPersistedInDataStore() {
        val repository = SettingsRepository(ApplicationProvider.getApplicationContext())
        val key = CalculatorId.OXYGEN.storageKey

        runBlocking {
            repository.toggleFavorite(key)
            val stored = withTimeout(5_000) {
                repository.favoriteCalculators.first { key in it }
            }
            assertTrue("избранное не сохранилось: $stored", key in stored)

            // Возвращаем состояние, чтобы не влиять на остальные тесты.
            repository.toggleFavorite(key)
            val cleared = withTimeout(5_000) {
                repository.favoriteCalculators.first { key !in it }
            }
            assertFalse("избранное не очистилось: $cleared", key in cleared)
        }
    }
}
