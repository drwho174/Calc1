package com.drwho174.calc1.contract

import androidx.fragment.app.Fragment

fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}

interface Navigator {

    fun showEuroSCORE ()

    fun showCKD ()

    fun showOxy ()

    fun showPerfusiologistCalc ()

    fun showHbPerfusion ()

    fun showIdealBodyMassCalc ()

    fun showBSA()

    fun showMainSettings()

    fun showAbout()


}