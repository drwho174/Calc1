package com.drwho174.calc1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.drwho174.calc1.contract.Navigator
import kotlin.math.pow

class CKDDial: DialogFragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragmentdialog_ckd, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val age : EditText = view.findViewById(R.id.ageField)
        val creatinine : EditText = view.findViewById(R.id.creatinineField)
        val res : TextView = view.findViewById(R.id.CKDRate)
        val sex: RadioGroup = view.findViewById(R.id.sex)
        val calculate : Button = view.findViewById(R.id.resultCKDButton)
        val renalDisfunction : RadioGroup = view.findViewById(R.id.renalDisfunction)
        val dialisysCheckBox : CheckBox = view.findViewById(R.id.dialisysCheckBox)


        fun sexCoefivient(): Double {
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

            if (sexCoefivient() == 1.0) {
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
            return 142 * ((creatinineDouble / a).pow(b)) * (0.9938.pow(ageDouble)) * sexCoefivient()

        }

        calculate.setOnClickListener {

            res.text = String().format(calcCKD(), "% ml/min/1.73m2")


            if(calcCKD() > 85.0){
                renalDisfunction.check(R.id.noRenalDisfunction)
            }else if(calcCKD() in 51.0..85.0){
                renalDisfunction.check(R.id.mildRenalDisfunction)
            }else if(calcCKD() <= 50.0){
                renalDisfunction.check(R.id.severeRenalDisfunction)
            }else if (dialisysCheckBox.isChecked){
                renalDisfunction.check(R.id.dialisysRenalDisfunction)
            }
        }
    }


}