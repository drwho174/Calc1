package com.drwho174.calc1

import android.content.Context
import android.content.SharedPreferences

class PreferenceRepository(context: Context) {

    val APP_PREFERENCES = "settings"
    val THEME_SET = true

     var settings : SharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)



        fun getThemeSetting(): Boolean{
            return settings.getBoolean("THEME_SET", true)
        }

        fun setThemeSetting(value : Boolean) {
            with(settings.edit()) {
                putBoolean("THEME_SET", value)
                apply()
            }
        }


}