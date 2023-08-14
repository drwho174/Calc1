package com.drwho174.calc1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.drwho174.calc1.databinding.FragmentSettingsOnMainMenuBinding

class SettingsOnMainMenu : Fragment() {
    private var _binding: FragmentSettingsOnMainMenuBinding? = null
    private val binding
    get() = _binding?: throw java.lang.IllegalStateException("_binding for SettingsOnMainMenu must not be null")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsOnMainMenuBinding.inflate(inflater, container, false)
     //   val themeselector = binding.themeselector
        binding.themeselector.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
        return binding.root
    }
}