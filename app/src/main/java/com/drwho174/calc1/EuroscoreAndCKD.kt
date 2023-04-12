package com.drwho174.calc1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.drwho174.calc1.databinding.EuroscoreAndCkdBinding
import com.drwho174.calc1.ui.main.EuroscoreAdapter
import com.google.android.material.tabs.TabLayout

class EuroscoreAndCKD : AppCompatActivity() {

    private lateinit var binding: EuroscoreAndCkdBinding
    private lateinit var adapter: EuroscoreAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       binding = EuroscoreAndCkdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = findViewById(R.id.view_pager)


        viewPager.adapter = EuroscoreAdapter(this)
        val tabs: TabLayout = binding.tabs


    }
}