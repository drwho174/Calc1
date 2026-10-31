package com.drwho174.calc1.ui

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.drwho174.calc1.BuildConfig
import com.drwho174.calc1.R
import com.drwho174.calc1.data.LanguageMode
import com.drwho174.calc1.data.ThemeMode
import com.drwho174.calc1.navigation.AboutRoute
import com.drwho174.calc1.navigation.AppAboutRoute
import com.drwho174.calc1.navigation.CalculatorId
import com.drwho174.calc1.navigation.CalculatorRoute
import com.drwho174.calc1.navigation.MainMenuRoute
import com.drwho174.calc1.navigation.SettingsRoute
import com.drwho174.calc1.navigation.calculatorDestinations
import com.drwho174.calc1.navigation.destination
import com.drwho174.calc1.ui.about.AboutAppScreen
import com.drwho174.calc1.ui.about.AboutScreen
import com.drwho174.calc1.ui.about.openAuthorEmail
import com.drwho174.calc1.ui.about.openAuthorTelegram
import com.drwho174.calc1.ui.calc.bsa.BsaScreen
import com.drwho174.calc1.ui.calc.ariscat.AriscatScreen
import com.drwho174.calc1.ui.calc.charlson.CharlsonScreen
import com.drwho174.calc1.ui.calc.egri.ElGanzouriScreen
import com.drwho174.calc1.ui.calc.euroscore.EuroScoreScreen
import com.drwho174.calc1.ui.calc.gfr.GfrScreen
import com.drwho174.calc1.ui.calc.hb.HbPerfusionScreen
import com.drwho174.calc1.ui.calc.ibm.IdealBodyMassScreen
import com.drwho174.calc1.ui.calc.lee.LeeRiskScreen
import com.drwho174.calc1.ui.calc.mehran.MehranScoreScreen
import com.drwho174.calc1.ui.calc.oxygen.OxygenDeliveryScreen
import com.drwho174.calc1.ui.calc.perfusiologist.PerfusiologistScreen
import com.drwho174.calc1.ui.calc.respiratory.RespiratoryScreen
import com.drwho174.calc1.ui.calc.sofa.SofaScreen
import com.drwho174.calc1.ui.components.CalcScaffold
import com.drwho174.calc1.ui.components.InfoAction
import com.drwho174.calc1.ui.menu.MainMenuScreen
import com.drwho174.calc1.ui.menu.MenuTab
import com.drwho174.calc1.ui.menu.defaultMenuTab
import com.drwho174.calc1.ui.settings.SettingsScreen

/**
 * Единственный Compose-хост приложения на Navigation 3.
 *
 * Стек навигации принадлежит приложению: экраны добавляют и снимают ключи
 * из [rememberNavBackStack], а [NavDisplay] отражает его в UI.
 */
@Composable
fun Calc1App(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    favoriteIds: Set<CalculatorId>?,
    onToggleFavorite: (CalculatorId) -> Unit,
    languageMode: LanguageMode = LanguageMode.SYSTEM,
    onLanguageModeChange: (LanguageMode) -> Unit = {},
) {
    val backStack = rememberNavBackStack(MainMenuRoute)
    val context = LocalContext.current
    val email = stringResource(R.string.authors_email)
    val telegram = stringResource(R.string.authors_telegram)

    // Вкладка выбирается один раз — когда впервые становится известно избранное
    // или когда пользователь нажал сегмент — и дальше сама не меняется. Иначе
    // отметка звездой переключала бы вкладку прямо под пальцем.
    var menuTab by rememberSaveable { mutableStateOf(MenuTab.ALL) }
    var menuTabResolved by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(favoriteIds) {
        if (!menuTabResolved && favoriteIds != null) {
            menuTab = defaultMenuTab(favoriteIds)
            menuTabResolved = true
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<MainMenuRoute> {
                CalcScaffold(
                    title = stringResource(R.string.name_main_menu),
                    actions = {
                        IconButton(onClick = { backStack.add(SettingsRoute) }) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = stringResource(R.string.name_settings),
                            )
                        }
                        InfoAction { backStack.add(AppAboutRoute) }
                    },
                ) { padding ->
                    MainMenuScreen(
                        calculators = calculatorDestinations,
                        favoriteIds = favoriteIds.orEmpty(),
                        selectedTab = menuTab,
                        onTabSelected = { tab ->
                            menuTab = tab
                            menuTabResolved = true
                        },
                        onToggleFavorite = onToggleFavorite,
                        onCalculatorClick = { id -> backStack.add(CalculatorRoute(id)) },
                        modifier = Modifier.padding(padding).imePadding(),
                    )
                }
            }

            entry<CalculatorRoute> { route ->
                val destination = route.id.destination()
                CalcScaffold(
                    title = stringResource(destination.titleRes),
                    onBack = { backStack.removeLastOrNull() },
                    actions = {
                        InfoAction { backStack.add(AboutRoute(destination.aboutAsset)) }
                    },
                ) { padding ->
                    val screenModifier = Modifier.padding(padding).imePadding()
                    when (route.id) {
                        CalculatorId.EUROSCORE -> EuroScoreScreen(screenModifier)
                        CalculatorId.HB_PERFUSION -> HbPerfusionScreen(screenModifier)
                        CalculatorId.PERFUSIOLOGIST -> PerfusiologistScreen(screenModifier)
                        CalculatorId.OXYGEN -> OxygenDeliveryScreen(screenModifier)
                        CalculatorId.GFR -> GfrScreen(screenModifier)
                        CalculatorId.IDEAL_BODY_MASS -> IdealBodyMassScreen(screenModifier)
                        CalculatorId.BSA -> BsaScreen(screenModifier)
                        CalculatorId.RESPIRATORY -> RespiratoryScreen(screenModifier)
                        CalculatorId.MEHRAN -> MehranScoreScreen(screenModifier)
                        CalculatorId.CHARLSON -> CharlsonScreen(screenModifier)
                        CalculatorId.SOFA -> SofaScreen(screenModifier)
                        CalculatorId.EL_GANZOURI -> ElGanzouriScreen(screenModifier)
                        CalculatorId.ARISCAT -> AriscatScreen(screenModifier)
                        CalculatorId.LEE_RCRI -> LeeRiskScreen(screenModifier)
                    }
                }
            }

            entry<AboutRoute> { route ->
                CalcScaffold(
                    title = stringResource(R.string.about_calculator),
                    onBack = { backStack.removeLastOrNull() },
                ) { padding ->
                    AboutScreen(
                        assetPath = route.assetName,
                        modifier = Modifier.padding(padding).imePadding(),
                    )
                }
            }

            entry<AppAboutRoute> {
                CalcScaffold(
                    title = stringResource(R.string.name_about),
                    onBack = { backStack.removeLastOrNull() },
                ) { padding ->
                    AboutAppScreen(
                        versionName = BuildConfig.VERSION_NAME,
                        email = email,
                        telegram = telegram,
                        onEmailClick = { context.openAuthorEmail(email) },
                        onTelegramClick = { context.openAuthorTelegram(telegram) },
                        modifier = Modifier.padding(padding).imePadding(),
                    )
                }
            }

            entry<SettingsRoute> {
                CalcScaffold(
                    title = stringResource(R.string.name_settings),
                    onBack = { backStack.removeLastOrNull() },
                ) { padding ->
                    SettingsScreen(
                        themeMode = themeMode,
                        onThemeModeChange = onThemeModeChange,
                        languageMode = languageMode,
                        onLanguageModeChange = onLanguageModeChange,
                        modifier = Modifier.padding(padding).imePadding(),
                    )
                }
            }
        },
    )
}
