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
         val height = etHeightField.text.toString()
         val weight = etWeightField.text.toString()

         val bsacoef = 0.007184

         // Du Bois formula of BSA
         return bsacoef * (height.toDouble().pow(0.725)) * (weight.toDouble().pow(0.425))
     }

     //Body mass index calculator
//     fun bmi(): Double {
//         val height = etHeightField.text.toString()
//         val weight = etWeightField.text.toString()
//
//         return weight.toDouble() / ((height.toDouble() / 100).pow(2))
//     }

       fun perfusionperfomance () : Float {

           return slPerfPrecent.value
       }

     //Slider for perfusion index
     fun perfindex(): Double {
         val v = slPerfIndex.value
         return v * bsacalc()
     }

     //Slider for percentage
     fun perfspeed(): Double {
         val v = slPerfPrecent.value

         return perfindex() * v / 100

     }

     //Slider for manual perfusion speed change and percentage change
     fun perfomanceset() {
         val v = slPerfSpeed.value * 100 / (slPerfIndex.value * bsacalc())
         val vv = (v / 5).roundToInt() * 5
         if (vv in 0..200) {
             slPerfPrecent.value = vv.toFloat()
         }

         }
       fun speedset(){

                 val round = (perfspeed() * 10).roundToInt() / 10.0
           if (round.toFloat() in 0.0..8.0){
                 slPerfSpeed.value = round.toFloat()
         }

     }

     val mTextWatcher = object : TextWatcher {
         override fun beforeTextChanged(et: CharSequence?, start: Int, count: Int, after: Int) {
         }

         override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
         }

         override fun afterTextChanged(et: Editable?) {
             if (etHeightField.text?.isNotEmpty() == true
                 && etWeightField.text?.isNotEmpty() == true
             ) {
                 twBsa.text = String.format("%.3f", bsacalc())
                 twPerfusionPerfomance.text = perfusionperfomance().toString()
                 twPerfSpeed.text = String.format("%.2f", perfindex())
                 speedset()
                 perfomanceset()
             }
         }
     }

     etHeightField.addTextChangedListener(mTextWatcher)
     etWeightField.addTextChangedListener(mTextWatcher)

     slPerfIndex.addOnChangeListener { _, _, _ ->
         if (etHeightField.text?.isNotEmpty() == true
             && etWeightField.text?.isNotEmpty() == true) {
             speedset()
             twPerfSpeed.text = String.format("%.2f", perfspeed())
         }
     }
     slPerfPrecent.addOnChangeListener { _, _, _ ->
         if (etHeightField.text?.isNotEmpty() == true
             && etWeightField.text?.isNotEmpty() == true
         ) {
             speedset()
             twPerfSpeed.text = String.format("%.2f", perfspeed())
             twPerfusionPerfomance.text = perfusionperfomance().toString()
         }
     }
     slPerfSpeed.addOnChangeListener { _, _, _ ->
         if (etHeightField.text?.isNotEmpty() == true
             && etWeightField.text?.isNotEmpty() == true
         ) {
             perfomanceset()
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
    private fun launchFragment(fragment: Fragment){
        requireActivity().supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}



























