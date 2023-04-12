package com.drwho174.calc1.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.drwho174.calc1.CKD
import com.drwho174.calc1.EuroSCORE


class EuroscoreAdapter(fragmentActivity: FragmentActivity?) : FragmentStateAdapter(fragmentActivity!!) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment
        if (position == 0){
            fragment = CKD() }else{
            fragment = EuroSCORE()}

        return fragment
    }
}