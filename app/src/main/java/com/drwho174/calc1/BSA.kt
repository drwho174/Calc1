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
import com.google.android.material.slider.Slider
import kotlin.math.pow


class BSA : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {return inflater.inflate(R.layout.fragment_bsa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val height: EditText = view.findViewById(R.id.heightField)
        val weight: EditText = view.findViewById(R.id.weightField)
        val bsaindex: TextView = view.findViewById(R.id.bsaIndication)
        val perfspeed: TextView = view.findViewById(R.id.PerfSpeed)
        val bmi: TextView = view.findViewById(R.id.bmi)
        val slider: Slider = view.findViewById(R.id.perfindexslider)

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
            val v = slider.value
            val bsa = bsaindex.text.toString()

            val bsad = (bsa.replace(",", ".")).toDouble()
            val ps = v * bsad
            return ps

        }

        val mTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(et: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(et: Editable?) {
                if(height.text.isNotEmpty() && weight.text.isNotEmpty()){

                    bsaindex.text = String.format("%.3f", bsacalc())
                    bmi.text = String.format("%.2f", bmi())
                    perfspeed.text = String.format("%.2f", perfspeedslider())
                }
            }
        }

        height.addTextChangedListener(mTextWatcher)
        weight.addTextChangedListener(mTextWatcher)

        slider.addOnChangeListener { _, _, _ ->
            perfspeed.text = String.format("%.2f", perfspeedslider())
        }
    }
}




























