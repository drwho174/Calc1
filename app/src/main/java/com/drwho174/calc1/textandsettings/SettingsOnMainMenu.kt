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
//    private var _binding: PreferenceSett? = null
//    private val binding
//    get() = _binding?: throw java.lang.IllegalStateException("_binding for SettingsOnMainMenu must not be null")
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentSettingsOnMainMenuBinding.inflate(inflater, container, false)
//     //   val themeselector = binding.themeselector
//        binding.themeselector.setOnCheckedChangeListener{ _, isChecked ->
//            if (isChecked){
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//
//            }else{
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            }
//        }
//        return binding.root
//    }




    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.preference_settings,rootKey)
                val themeselector : SwitchPreferenceCompat? = findPreference("themeselector")

        val prefRepository = PreferenceRepository(context?.applicationContext ?: return)


        val nightModeFlags = context!!.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags){
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