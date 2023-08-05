package com.drwho174.calc1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.drwho174.calc1.databinding.FragmentSettingsOnMainMenuBinding

class SettingsOnMainMenu : Fragment() {
private lateinit var binding: FragmentSettingsOnMainMenuBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsOnMainMenuBinding.inflate(inflater, container, false)
        val themeselector = binding.themeselector
        themeselector.setOnCheckedChangeListener{buttonView, isChecked ->
            if (isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            }else{AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)


            }

        }
        return binding.root
    }
}