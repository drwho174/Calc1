package com.drwho174.calc1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.drwho174.calc1.contract.Navigator

class MainActivity : AppCompatActivity(), Navigator {

    /*private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            updateUi()
        }
    }*/ //эта штука и связанные с ней нужны для реализации изменений в appbar на каждом экране

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, MainMenu())
                .commit()
        }

      //  supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)

    }

    override fun showEuroSCORE() {
        launchFragment(EuroSCORE())
    }

    override fun showCKD() {
        launchFragment(CKD())
    }

    override fun showOxy() {
        launchFragment(Oxy())
    }

    override fun showBSA() {
        launchFragment(BSA())
    }

    override fun showHbPerfusion() {
        launchFragment(HbPerfusion())
    }

    override fun showMainSettings() {
        launchFragment(SettingsOnMainMenu())
    }

    override fun showAbout() {
        TODO("Not yet implemented")
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}

