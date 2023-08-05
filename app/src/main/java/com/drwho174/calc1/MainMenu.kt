package com.drwho174.calc1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.drwho174.calc1.contract.navigator
import com.drwho174.calc1.databinding.FragmentMainMenuBinding

class MainMenu : Fragment(){
private lateinit var binding: FragmentMainMenuBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainMenuBinding.inflate(inflater, container, false)

        binding.EuroSCRButton.setOnClickListener { onEuroSCRButtonPressed() }
        binding.CKDButton.setOnClickListener { onCKDButtonPressed() }
        binding.HbPerfButton.setOnClickListener { onHbPerfusionButtonPressed() }
        binding.BSAButton.setOnClickListener { onBSAButtonPressed() }
        binding.OxyButton.setOnClickListener { onOxyButtonPressed() }

        binding.mainMenuToolbar.setOnMenuItemClickListener {
            when (it.itemId ){
                R.id.settings -> {onMainSettingsPressed();true}
                R.id.about -> {onAboutPressed(); true}
                else -> true
            }
        }



        return binding.root
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

    private fun onBSAButtonPressed() {
        navigator().showBSA()
    }

    private fun onOxyButtonPressed() {
        navigator().showOxy()
    }

    private fun onMainSettingsPressed(){
        navigator().showMainSettings()
    }

    private fun onAboutPressed(){
        navigator().showAbout()
    }

}