package com.drwho174.calc1

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.drwho174.calc1.contract.HasCustomTitle
import com.drwho174.calc1.contract.Navigator
import com.drwho174.calc1.databinding.ActivityMainBinding




class MainActivity : AppCompatActivity(), Navigator {


    private lateinit var binding: ActivityMainBinding

    private val currentFragment: Fragment
        get() = supportFragmentManager.findFragmentById(R.id.fragmentContainer)!!

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            updateUi()
        }
    } //эта штука и связанные с ней нужны для реализации изменений в appbar на каждом экране


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        setSupportActionBar(binding.mainToolbar)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, MainMenu())
                .commit()
        }

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)

    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun showEuroSCORE() {
        launchFragment(EuroSCORE())
    }

    override fun showCKD() {
        launchFragment(CreatinineClearance())
    }

    override fun showOxy() {
        launchFragment(OxygenDelivery())
    }

    override fun showPerfusiologistCalc() {
        launchFragment(PerfusiologistCalculator())
    }

    override fun showHbPerfusion() {
        launchFragment(HbPerfusion())
    }

    override fun showIdealBodyMassCalc() {
        launchFragment(IdealBodyMass())
    }

    override fun showBSA() {
        launchFragment(BSA())
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // we've called setSupportActionBar in onCreate,
        // that's why we need to override this method too
        updateUi()
        return true
    }

    private fun updateUi() {
        val fragment = currentFragment

        if (fragment is HasCustomTitle) {
            binding.mainToolbar.title = getString(fragment.getTitleRes())
        } else {
            binding.mainToolbar.title = getString(R.string.app_name)
        }

        if (supportFragmentManager.backStackEntryCount > 0) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setDisplayShowHomeEnabled(false)
        }

        binding.mainToolbar.menu.clear()

//        if (fragment is HasCustomAction) {
//            createCustomToolbarAction(fragment.getCustomAction())
//        } else {
//            binding.mainToolbar.menu.clear()
//        }
    }
}


