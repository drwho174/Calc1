package com.drwho174.calc1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import com.drwho174.calc1.contract.HasCustomTitle
import com.drwho174.calc1.contract.navigator
import com.drwho174.calc1.ui.menu.MainMenuScreen
import com.drwho174.calc1.ui.theme.Calc1Theme

class MainMenu : Fragment(), HasCustomTitle {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Тема по-прежнему применяется через AppCompatDelegate: экраны на
        // фрагментах читают ночные ресурсы, а Calc1Theme следует uiMode.
        val prefRepository = PreferenceRepository(requireContext().applicationContext)
        AppCompatDelegate.setDefaultNightMode(
            if (prefRepository.getThemeSetting()) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            Calc1Theme {
                MainMenuScreen(
                    onEuroScore = { navigator().showEuroSCORE() },
                    onHbPerfusion = { navigator().showHbPerfusion() },
                    onPerfusiologist = { navigator().showPerfusiologistCalc() },
                    onOxygenDelivery = { navigator().showOxy() },
                    onGfr = { navigator().showCKD() },
                    onIdealBodyMass = { navigator().showIdealBodyMassCalc() },
                    onBsa = { navigator().showBSA() },
                    onRespiratory = { navigator().showResriratoryIndex() },
                    onMehranScore = { navigator().showMehranScore() },
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.settings, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.settings -> {
                        navigator().showMainSettings()
                        true
                    }

                    R.id.about -> {
                        navigator().showAbout()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }

    override fun getTitleRes(): Int = R.string.name_main_menu
}
