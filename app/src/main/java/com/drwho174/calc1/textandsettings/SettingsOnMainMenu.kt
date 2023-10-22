package com.drwho174.calc1.textandsettings

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.drwho174.calc1.PreferenceRepository
import com.drwho174.calc1.R
import com.drwho174.calc1.contract.HasCustomTitle


class SettingsOnMainMenu : PreferenceFragmentCompat() , HasCustomTitle{

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.preference_settings,rootKey)
                val themeselector : SwitchPreferenceCompat? = findPreference("themeselector")

        val prefRepository = PreferenceRepository(context?.applicationContext ?: return)


        when (requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK){
            Configuration.UI_MODE_NIGHT_YES -> themeselector?.setDefaultValue(true)
            Configuration.UI_MODE_NIGHT_NO -> themeselector?.setDefaultValue(false)
        }

        themeselector?.setOnPreferenceChangeListener { _, newValue ->

            if (newValue == false){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                prefRepository.setThemeSetting(false)

                true

            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                prefRepository.setThemeSetting(true)

                true
            }
        }

    }

    override fun getTitleRes(): Int = R.string.name_settings

}