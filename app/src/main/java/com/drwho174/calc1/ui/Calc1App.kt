package com.drwho174.calc1.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.drwho174.calc1.BuildConfig
import com.drwho174.calc1.R
import com.drwho174.calc1.data.ThemeMode
import com.drwho174.calc1.navigation.AboutRoute
import com.drwho174.calc1.navigation.AppAboutRoute
import com.drwho174.calc1.navigation.CalculatorId
import com.drwho174.calc1.navigation.CalculatorRoute
import com.drwho174.calc1.navigation.MainMenuRoute
import com.drwho174.calc1.navigation.SettingsRoute
import com.drwho174.calc1.navigation.destination
import com.drwho174.calc1.ui.about.AboutAppScreen
import com.drwho174.calc1.ui.about.AboutScreen
import com.drwho174.calc1.ui.about.openAuthorEmail
import com.drwho174.calc1.ui.about.openAuthorTelegram
import com.drwho174.calc1.ui.calc.bsa.BsaScreen
import com.drwho174.calc1.ui.calc.euroscore.EuroScoreScreen
import com.drwho174.calc1.ui.calc.gfr.GfrScreen
import com.drwho174.calc1.ui.calc.hb.HbPerfusionScreen
import com.drwho174.calc1.ui.calc.ibm.IdealBodyMassScreen
import com.drwho174.calc1.ui.calc.mehran.MehranScoreScreen
import com.drwho174.calc1.ui.calc.oxygen.OxygenDeliveryScreen
import com.drwho174.calc1.ui.calc.perfusiologist.PerfusiologistScreen
import com.drwho174.calc1.ui.calc.respiratory.RespiratoryScreen
import com.drwho174.calc1.ui.components.CalcScaffold
import com.drwho174.calc1.ui.components.InfoAction
import com.drwho174.calc1.ui.menu.MainMenuScreen
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
) {
    val backStack = rememberNavBackStack(MainMenuRoute)
    val context = LocalContext.current
    val email = stringResource(R.string.authors_email)
    val telegram = stringResource(R.string.authors_telegram)

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
                        onEuroScore = {
                            backStack.add(CalculatorRoute(CalculatorId.EUROSCORE))
                        },
                        onHbPerfusion = {
                            backStack.add(CalculatorRoute(CalculatorId.HB_PERFUSION))
                        },
                        onPerfusiologist = {
                            backStack.add(CalculatorRoute(CalculatorId.PERFUSIOLOGIST))
                        },
                        onOxygenDelivery = {
                            backStack.add(CalculatorRoute(CalculatorId.OXYGEN))
                        },
                        onGfr = {
                            backStack.add(CalculatorRoute(CalculatorId.GFR))
                        },
                        onIdealBodyMass = {
                            backStack.add(CalculatorRoute(CalculatorId.IDEAL_BODY_MASS))
                        },
                        onBsa = {
                            backStack.add(CalculatorRoute(CalculatorId.BSA))
                        },
                        onRespiratory = {
                            backStack.add(CalculatorRoute(CalculatorId.RESPIRATORY))
                        },
                        onMehranScore = {
                            backStack.add(CalculatorRoute(CalculatorId.MEHRAN))
                        },
                        modifier = Modifier.padding(padding),
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
                    val screenModifier = Modifier.padding(padding)
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
                        modifier = Modifier.padding(padding),
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
                        modifier = Modifier.padding(padding),
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
                        modifier = Modifier.padding(padding),
                    )
                }
            }
        },
    )
}
