package com.drwho174.calc1

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlin.math.pow

class CKD : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ckd, container, false)


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val age : EditText = view.findViewById(R.id.ageField)
        val creatinine : EditText = view.findViewById(R.id.creatinineField)
        val res : TextView = view.findViewById(R.id.resultCKD)
        val ckdsheet = BottomSheetBehavior.from(view.findViewById(R.id.bottom_sheet_ckd_result))

        ckdsheet.state = BottomSheetBehavior.STATE_EXPANDED


        fun sexCoefivient(): Double {
            val sex: RadioGroup = view.findViewById(R.id.sex)
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

        val generalTextWatcher: TextWatcher = object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                    if (age.text.isNotEmpty() &&
                        creatinine.text.isNotEmpty()){
                           res.text = String.format("%.2f ml/min/1.73m2", calcCKD())
                    }
            }
        }

        age.addTextChangedListener(generalTextWatcher)
        creatinine.addTextChangedListener(generalTextWatcher)



    }
}

