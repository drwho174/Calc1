package com.drwho174.calc1


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.drwho174.calc1.databinding.FragmentBsaBinding
import com.google.android.material.slider.Slider
import kotlin.math.pow


class BSA : Fragment() {

    private lateinit var binding: FragmentBsaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBsaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val height: EditText = binding.heightField
        val weight: EditText = binding.weightField
        val bsaindex: TextView = binding.bsaIndication
        val perfspeed: TextView = binding.PerfSpeed
        val bmi: TextView = binding.bmi
        val perfindexslider: Slider = binding.perfindexslider
        val perfprecentslider: Slider = binding.perfprecentslider

        //Body surface area calculator
        fun bsacalc(): Double {
            val height1 = height.text.toString()
            val weight1 = weight.text.toString()

            val heightD = height1.toDouble()
            val weightD = weight1.toDouble()

            val bsacoef = 0.007184

            // Du Bois formula of BSA
            return bsacoef * (heightD.pow(0.725)) * (weightD.pow(0.425))
        }

        //Body mass index calculator
        fun bmi(): Double {
            val height1 = height.text.toString()
            val weight1 = weight.text.toString()

            val heightD = height1.toDouble()
            val weightD = weight1.toDouble()
            return weightD / ((heightD / 100).pow(2))
        }

        //Slider for perfusion index
        fun perfspeedslider(): Double {
            val v = perfindexslider.value
            val bsa = bsaindex.text.toString()

            val bsad = (bsa.replace(",", ".")).toDouble()
            return v * bsad
        }

        fun perfprecentslider(): Double {
            val v = perfprecentslider.value
            return perfspeedslider() * v / 100
        }

        val mTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(et: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(et: Editable?) {
                if (height.text.isNotEmpty() && weight.text.isNotEmpty()) {

                    bsaindex.text = String.format("%.3f", bsacalc())
                    bmi.text = String.format("%.2f", bmi())
                    perfspeed.text = String.format("%.2f", perfspeedslider())
                }
            }
        }

        height.addTextChangedListener(mTextWatcher)
        weight.addTextChangedListener(mTextWatcher)

        perfindexslider.addOnChangeListener { _, _, _ ->
            if (height.text.isNotEmpty() && weight.text.isNotEmpty()) {
                perfspeed.text = String.format("%.2f", perfprecentslider())
            }
        }
        perfprecentslider.addOnChangeListener { _, _, _ ->
            if (height.text.isNotEmpty() && weight.text.isNotEmpty()) {
                perfspeed.text = String.format("%.2f", perfprecentslider())
            }
        }


    }
}



























