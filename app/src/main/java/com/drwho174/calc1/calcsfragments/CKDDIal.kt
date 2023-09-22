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

                etAgeField.setText(ageBundle)
            }
            parentFragmentManager.setFragmentResultListener("sex",viewLifecycleOwner){
                    _, bundle  ->
                val sexBundle = bundle.getInt("sexBundle")
                if (sexBundle == R.id.male){
                    rgSex.check(R.id.male)
                }else{
                    rgSex.check(R.id.female)
                }
            }

            fun sexCoefficient(): Double {
                val sexCoefficient = when(rgSex.checkedRadioButtonId){
                    R.id.male -> 1.012
                    R.id.female -> 1.0
                    else -> 0.0
                }
                return sexCoefficient
            }

            fun calcCKD(): Double {
                val ageString = etAgeField.text.toString()
                val creatinineString = etCreatinineField.text.toString()

                val ageDouble = ageString.toDouble()
                val creatinineDouble = creatinineString.toDouble() / 88.4055
                val a: Double
                val b: Double

                if (sexCoefficient() == 1.0) {
                    a = 0.7
                    b = if (creatinineDouble <= 0.7) {
                        -0.241
                    } else {
                        -1.2
                    }
                } else {
                    a = 0.9
                    b = if (creatinineDouble <= 0.9) {
                        -0.302
                    } else {
                        -1.2
                    }
                }
                return 142 * ((creatinineDouble / a).pow(b)) * (0.9938.pow(ageDouble)) * sexCoefficient()
            }
//результат по нажатию на кнопку отправляется в parentFragmentManager
            btResultGfr.setOnClickListener {
                if(etCreatinineField.text?.isNotEmpty() == true &&
                    etAgeField.text?.isNotEmpty()== true) {
                    val gfrRes = calcCKD()
                    parentFragmentManager.setFragmentResult("CKDres", bundleOf("CKDbundle" to gfrRes))
                }
                if (cbDialisys.isChecked){
                    val dialysis = true
                    parentFragmentManager.setFragmentResult("dialysisBool", bundleOf("dialysisBundle" to dialysis))
                }
//Закрывает Fragment
                val fragtran: FragmentTransaction = parentFragmentManager.beginTransaction()
                fragtran.remove(this@CKDDial)
                fragtran.commit()
            }
        }
    }
    companion object {
        const val TAG = "CKDDialog"
    }

}