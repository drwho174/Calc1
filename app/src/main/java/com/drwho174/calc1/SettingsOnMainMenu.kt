package com.drwho174.calc1

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
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

        val settings : SharedPreferences = activity?.getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE) ?: return

        val nightModeFlags = context!!.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags){
            Configuration.UI_MODE_NIGHT_YES -> themeselector?.setDefaultValue(true)
            Configuration.UI_MODE_NIGHT_NO -> themeselector?.setDefaultValue(false)
        }

        themeselector?.setOnPreferenceChangeListener { _, newValue ->

            if (newValue == false){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                with(settings.edit()){
                    putBoolean("THEME_SET", false)
                    apply()
                }
                true

            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                with(settings.edit()){
                    putBoolean("THEME_SET", true)
                    apply()
                }
                true
            }
        }

    }

    override fun getTitleRes(): Int = R.string.name_settings
}