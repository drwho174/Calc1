package com.drwho174.calc1.textandsettings

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
        val themeSelector : SwitchPreferenceCompat? = findPreference("THEME_SET")

        val prefRepository = PreferenceRepository(context?.applicationContext ?: return)

        // SwitchPreferenceCompat automatically shows the persisted value,
        // so there is no need to call setDefaultValue() here.

        themeSelector?.setOnPreferenceChangeListener { _, newValue ->
            val darkTheme = newValue == true

            if (darkTheme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            prefRepository.setThemeSetting(darkTheme)

            true
        }

    }

    override fun getTitleRes(): Int = R.string.name_settings

}