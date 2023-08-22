package com.drwho174.calc1.calcsfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import com.drwho174.calc1.R
import com.drwho174.calc1.databinding.FragmentdialogCkdBinding
import kotlin.math.pow

class CKDDial: DialogFragment(){
    private var _binding : FragmentdialogCkdBinding? = null
    private val binding
    get() = _binding?: throw java.lang.IllegalStateException("_binding for BSAFragment must not be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentdialogCkdBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            parentFragmentManager.setFragmentResultListener("age", viewLifecycleOwner){ _, bundle ->
                val ageBundle = bundle.getString("ageBundle")

                ageField.setText(ageBundle)
            }
            parentFragmentManager.setFragmentResultListener("sex",viewLifecycleOwner){
                    _, bundle  ->
                val sexBundle = bundle.getInt("sexBundle")
                if (sexBundle == R.id.male){
                    sex.check(R.id.male)
                }else{
                    sex.check(R.id.female)
                }
            }

            fun sexCoefficient(): Double {
                val sexCoefficient = when(sex.checkedRadioButtonId){
                    R.id.male -> 1.012
                    R.id.female -> 1.0
                    else -> 0.0
                }
                return sexCoefficient
            }

            fun calcCKD(): Double {
                val ageString = ageField.text.toString()
                val creatinineString = creatinineField.text.toString()

                val ageDouble = ageString.toDouble()
                val creatinineDouble = creatinineString.toDouble() / 88.4055
                val a: Double
                val b: Double

                if (sexCoefficient() == 1.0) {
                    a = 0.7
                    if (creatinineDouble <= 0.7) {
                        b = -0.241
                    } else {
                        b = -1.2
                    }
                } else {
                    a = 0.9
                    if (creatinineDouble <= 0.9) {
                        b = -0.302
                    } else {
                        b = -1.2
                    }
                }
                return 142 * ((creatinineDouble / a).pow(b)) * (0.9938.pow(ageDouble)) * sexCoefficient()
            }
//результат по нажатию на кнопку отправляется в parentFragmentManager
            resultCKDButton.setOnClickListener {
                if(creatinineField.text?.isNotEmpty() == true &&
                    ageField.text?.isNotEmpty()== true) {
                    val CKDres = calcCKD()
                    parentFragmentManager.setFragmentResult("CKDres", bundleOf("CKDbundle" to CKDres))
                }
                if (dialisysCheckBox.isChecked){
                    val dialysis = true
                    parentFragmentManager.setFragmentResult("dialysisBool", bundleOf("dialysisBundle" to dialysis))
                }
//Закрывает Fragment
                val fragtran: FragmentTransaction = parentFragmentManager.beginTransaction()
                fragtran.remove(this@CKDDial)
                fragtran.commit()
            }
        }
//        val age : EditText = view.findViewById(R.id.ageField)
//        val creatinine : EditText = view.findViewById(R.id.creatinineField)
//        val sex: RadioGroup = view.findViewById(R.id.sex)
//        val calculate : Button = view.findViewById(R.id.resultCKDButton)
//        val dialisysCheckBox : CheckBox = view.findViewById(R.id.dialisysCheckBox)


    }
    companion object {
        const val TAG = "CKDDialog"
    }

}