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
import com.drwho174.calc1.databinding.FragmentPerfusiologistCalcBinding
import com.google.android.material.slider.Slider
import kotlin.math.pow
import kotlin.math.roundToInt


class PerfusiologistCalculator : Fragment() {

    private lateinit var binding: FragmentPerfusiologistCalcBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPerfusiologistCalcBinding.inflate(inflater, container, false)
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
        val perfspeedslider: Slider = binding.perfspeedslider

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
        fun perfindexslider(): Double {
            val v = perfindexslider.value

            return v * bsacalc()
        }
//Slider for percentage
        fun perfprecentslider(): Double {
            val v = perfprecentslider.value
            val perfspeedlocal = perfindexslider() * v / 100

            return perfspeedlocal
        }
//Slider for manual perfusion speed change and percentage change
        fun speedslider (){
           val v = perfspeedslider.value*100/(perfindexslider.value*bsacalc())
           val vv =  (v/5).roundToInt()*5
            if(vv in 0..150 ) {  perfprecentslider.value = vv.toFloat()}

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
                    perfspeed.text = String.format("%.2f", perfindexslider())
                }
            }
        }

        height.addTextChangedListener(mTextWatcher)
        weight.addTextChangedListener(mTextWatcher)

        perfindexslider.addOnChangeListener { _, _, _ ->
            if (height.text.isNotEmpty() && weight.text.isNotEmpty()) {
                val round = (perfprecentslider()*10).roundToInt()/10.0
                perfspeedslider.value = round.toFloat()
                perfspeed.text = String.format("%.2f", perfprecentslider())
            }
        }
        perfprecentslider.addOnChangeListener { _, _, _ ->
            if (height.text.isNotEmpty() && weight.text.isNotEmpty()) {
                val round = (perfprecentslider()*10).roundToInt()/10.0
                perfspeedslider.value = round.toFloat()
                perfspeed.text = String.format("%.2f", perfprecentslider())
            }
        }
        perfspeedslider.addOnChangeListener{_,_,_->
            if (height.text.isNotEmpty() && weight.text.isNotEmpty()) {
                speedslider()

            }

        }

    }
}



























