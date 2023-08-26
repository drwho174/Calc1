package com.drwho174.calc1.calcsfragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.drwho174.calc1.R
import com.drwho174.calc1.contract.CustomAction
import com.drwho174.calc1.contract.HasCustomAction
import com.drwho174.calc1.contract.HasCustomTitle
import com.drwho174.calc1.databinding.FragmentPerfusiologistCalcBinding
import com.drwho174.calc1.textandsettings.AboutPerfusiologistCalc
import kotlin.math.pow
import kotlin.math.roundToInt


class PerfusiologistCalculator : Fragment(), HasCustomTitle, HasCustomAction {

    private var _binding: FragmentPerfusiologistCalcBinding? = null
    private val binding
    get() = _binding?: throw java.lang.IllegalStateException("_binding for Perfusiologist must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfusiologistCalcBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

 with(binding) {
     //Body surface area calculator
     fun bsacalc(): Double {
         val height = heightField.text.toString()
         val weight = weightField.text.toString()

         val bsacoef = 0.007184

         // Du Bois formula of BSA
         return bsacoef * (height.toDouble().pow(0.725)) * (weight.toDouble().pow(0.425))
     }

     //Body mass index calculator
     fun bmi(): Double {
         val height = heightField.text.toString()
         val weight = weightField.text.toString()

         return weight.toDouble() / ((height.toDouble() / 100).pow(2))
     }

     //Slider for perfusion index
     fun perfindexslider(): Double {
         val v = perfindexslider.value

         return v * bsacalc()
     }

     //Slider for percentage
     fun perfprecentslider(): Double {
         val v = perfprecentslider.value

         return perfindexslider() * v / 100
     }

     //Slider for manual perfusion speed change and percentage change
     fun speedslider() {
         val v = perfspeedslider.value * 100 / (perfindexslider.value * bsacalc())
         val vv = (v / 5).roundToInt() * 5
         if (vv in 0..150) {
             perfprecentslider.value = vv.toFloat()
         }

     }

     val mTextWatcher = object : TextWatcher {
         override fun beforeTextChanged(et: CharSequence?, start: Int, count: Int, after: Int) {
         }

         override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
         }

         override fun afterTextChanged(et: Editable?) {
             if (heightField.text?.isNotEmpty() == true
                 && weightField.text?.isNotEmpty() == true
             ) {
                 bsaIndication.text = String.format("%.3f", bsacalc())
                 bmi.text = String.format("%.2f", bmi())
                 perfspeed.text = String.format("%.2f", perfindexslider())
             }
         }
     }

     heightField.addTextChangedListener(mTextWatcher)
     weightField.addTextChangedListener(mTextWatcher)

     perfindexslider.addOnChangeListener { _, _, _ ->
         if (heightField.text?.isNotEmpty() == true
             && weightField.text?.isNotEmpty() == true) {
             val round = (perfprecentslider() * 10).roundToInt() / 10.0
             perfspeedslider.value = round.toFloat()
             perfspeed.text = String.format("%.2f", perfprecentslider())
         }
     }
     perfprecentslider.addOnChangeListener { _, _, _ ->
         if (heightField.text?.isNotEmpty() == true
             && weightField.text?.isNotEmpty() == true
         ) {
             val round = (perfprecentslider() * 10).roundToInt() / 10.0
             perfspeedslider.value = round.toFloat()
             perfspeed.text = String.format("%.2f", perfprecentslider())
         }
     }
     perfspeedslider.addOnChangeListener { _, _, _ ->
         if (heightField.text?.isNotEmpty() == true
             && weightField.text?.isNotEmpty() == true
         ) {
             speedslider()
         }

     }
 }
    }

    override fun getTitleRes(): Int = R.string.name_perfusion_calculator

    override fun getCustomAction(): CustomAction {
        return CustomAction(
            iconRes = R.drawable.ic_info,
            onCustomAction = {
                launchFragment(AboutPerfusiologistCalc())
            }
        )
    }
    //start yours fragment
    fun launchFragment(fragment: Fragment){
        requireActivity().supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}



























