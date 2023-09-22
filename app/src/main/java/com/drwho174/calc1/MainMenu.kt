package com.drwho174.calc1

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import com.drwho174.calc1.contract.HasCustomTitle
import com.drwho174.calc1.contract.navigator
import com.drwho174.calc1.databinding.FragmentMainMenuBinding

class MainMenu : Fragment(), HasCustomTitle{
private lateinit var binding: FragmentMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefRepository = PreferenceRepository(context?.applicationContext ?: return)


        fun setuptheme(){
            when (prefRepository.getThemeSetting()){
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        setuptheme()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainMenuBinding.inflate(inflater, container, false)

        binding.EuroSCRButton.setOnClickListener { onEuroSCRButtonPressed() }
        binding.CKDButton.setOnClickListener { onCKDButtonPressed() }
        binding.HbPerfButton.setOnClickListener { onHbPerfusionButtonPressed() }
        binding.BSAButton.setOnClickListener { onPerfusiologistCalcButtonPressed() }
        binding.OxyButton.setOnClickListener { onOxyButtonPressed() }
        binding.idealbodymassbutton.setOnClickListener { onIdealBodyMassButtonPressed() }
        binding.btBsa.setOnClickListener { onBSAButtonPressed() }
        binding.btRespiratoryIndex.setOnClickListener { onRespiratoryIndexButtonPressed() }
        binding.btMehranScore.setOnClickListener { onMehranScoreButtonPressed() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.settings, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId ){
                    R.id.settings -> {onMainSettingsPressed();true}
                    R.id.about -> {onAboutPressed(); true}
                    else -> true
                }
            }

        },viewLifecycleOwner)
    }



    private fun onEuroSCRButtonPressed() {
        navigator().showEuroSCORE()
    }

    private fun onCKDButtonPressed() {
        navigator().showCKD()
    }

    private fun onHbPerfusionButtonPressed() {
        navigator().showHbPerfusion()
    }

    private fun onPerfusiologistCalcButtonPressed() {
        navigator().showPerfusiologistCalc()
    }

    private fun onOxyButtonPressed() {
        navigator().showOxy()
    }

    private fun onIdealBodyMassButtonPressed(){
        navigator().showIdealBodyMassCalc()
    }

    private fun onBSAButtonPressed(){
        navigator().showBSA()
    }

    private fun onRespiratoryIndexButtonPressed(){
        navigator().showResriratoryIndex()
    }

    private fun onMehranScoreButtonPressed(){
        navigator().showMehranScore()
    }
    private fun onMainSettingsPressed(){
        navigator().showMainSettings()
    }

    private fun onAboutPressed(){
        navigator().showAbout()
    }



    override fun getTitleRes(): Int = R.string.name_main_menu



}