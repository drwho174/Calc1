package com.drwho174.calc1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.drwho174.calc1.contract.Navigator
import com.google.android.material.bottomsheet.BottomSheetBehavior

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
    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}

//todo добавить top app bar к каждому activity(настройки, краткая информация о каждом калькуляторе, название калькулятора)