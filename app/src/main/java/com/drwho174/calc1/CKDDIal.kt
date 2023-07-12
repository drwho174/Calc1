package com.drwho174.calc1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import kotlin.math.pow

class CKDDial: DialogFragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { return inflater.inflate(R.layout.fragmentdialog_ckd, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val age : EditText = view.findViewById(R.id.ageField)
        val creatinine : EditText = view.findViewById(R.id.creatinineField)
        val sex: RadioGroup = view.findViewById(R.id.sex)
        val calculate : Button = view.findViewById(R.id.resultCKDButton)
        val dialisysCheckBox : CheckBox = view.findViewById(R.id.dialisysCheckBox)

        parentFragmentManager.setFragmentResultListener("age", this){ _, bundle ->
            val ageBundle = bundle.getString("ageBundle")

            age.setText(ageBundle)
        }
        parentFragmentManager.setFragmentResultListener("sex",this){
         _, bundle  ->
         val sexBundle = bundle.getString("sex")
            if (sexBundle == "male"){
                sex.check(R.id.male)
            } else{
                sex.check(R.id.female)
            }
        }

        fun sexCoefficient(): Double {
            val sexCoeficient = when(sex.checkedRadioButtonId){
                R.id.male -> 1.012
                R.id.female -> 1.0
                else -> 0.0
            }
            return sexCoeficient
        }

        fun calcCKD(): Double {
            val ageString = age.text.toString()
            val creatinineString = creatinine.text.toString()

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
        calculate.setOnClickListener {
            if(creatinine.text.isNotEmpty() &&
                age.text.isNotEmpty()) {
                val CKDres = calcCKD()
                parentFragmentManager.setFragmentResult("CKDres", bundleOf("CKDbundle" to CKDres))
            }
            if (dialisysCheckBox.isChecked){
                val dialsisys = true
                parentFragmentManager.setFragmentResult("dialisysBool", bundleOf("dialisysBundle" to dialsisys))
           }
//Закрывает Fragment
            val fragtran: FragmentTransaction = parentFragmentManager.beginTransaction()
            fragtran.remove(this)
            fragtran.commit()
        }
    }
    companion object {
        const val TAG = "CKDDialog"
    }

}