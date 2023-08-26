package com.drwho174.calc1.calcsfragments

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
import com.drwho174.calc1.R
import com.drwho174.calc1.contract.CustomAction
import com.drwho174.calc1.contract.HasCustomAction
import com.drwho174.calc1.contract.HasCustomTitle
import com.drwho174.calc1.textandsettings.AboutHbPerfision
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlin.math.pow

class HbPerfusion : Fragment(), HasCustomTitle, HasCustomAction {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_hb_perf, container, false)
    }
//TODO viewBinding needed
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val height: EditText = view.findViewById(R.id.heightField)
        val weight: EditText = view.findViewById(R.id.weightFIeld)
        val hbinit: EditText = view.findViewById(R.id.hbInitial)
        val infvol: EditText = view.findViewById(R.id.infusionVol)
        val exfvol: EditText = view.findViewById(R.id.exfusionVol)
        val diur: EditText = view.findViewById(R.id.diuresis)
        val primevol: EditText = view.findViewById(R.id.primeVol)
        val cpbexf: EditText = view.findViewById(R.id.cpbExfusionVol)
        val sex: RadioGroup = view.findViewById(R.id.SexRadioGroup)

        val hbPerfSheet = BottomSheetBehavior.from(view.findViewById(R.id.bottom_sheet_hb_result))
        hbPerfSheet.state = BottomSheetBehavior.STATE_EXPANDED

        fun cbvfirst(): Double {
            val height1 = height.text.toString()
            val weight1 = weight.text.toString()

            val heightD = height1.toDouble()
            val weightD = weight1.toDouble()

            val circulatiobloodvol = when (sex.checkedRadioButtonId) {
                R.id.maleRadioButton -> 0.417 * (heightD / 100).pow(3) + 0.045 * weightD - 0.03
                R.id.femaleRadioButton -> 0.414 * (heightD / 100).pow(3) + 0.0328 * weightD - 0.03

                else -> 0.0
            }
            return circulatiobloodvol
        }

        fun cbvsecond(): Double {
            val height1 = height.text.toString()
            val weight1 = weight.text.toString()

            val heightD = height1.toDouble()
            val weightD = weight1.toDouble()

            val circulatiobloodvol = when (sex.checkedRadioButtonId) {
                R.id.maleRadioButton -> (367 * (heightD / 100).pow(3) + 32.2 * weightD + 604) / 1000
                R.id.femaleRadioButton -> (356 * (heightD / 100).pow(3) + 33.1 * weightD + 183) / 1000

                else -> { 0.0
                }
            }
            return circulatiobloodvol

        }

        fun hbexf(): Double {
            val hbinit1 = hbinit.text.toString()
            val exfvol1 = exfvol.text.toString()

            val hbinitd = hbinit1.toDouble()
            val exfvold = exfvol1.toDouble()

            return hbinitd * exfvold / 1000

        }

        fun hbbefcpbfirst() {
            val res1: TextView = view.findViewById(R.id.beforecpb1d)
            val res3: TextView = view.findViewById(R.id.aftercpb1d)

            val hbinit1 = hbinit.text.toString()
            val exfvol1 = exfvol.text.toString()
            val diur1 = diur.text.toString()
            val infvol1 = infvol.text.toString()

            val hbinitd = hbinit1.toDouble()
            val exfvold = exfvol1.toDouble()
            val diurd = diur1.toDouble()
            val infvold = infvol1.toDouble()

            val hbgeneral = hbinitd * cbvfirst()

            val balance = (cbvfirst() - exfvold / 1000 - diurd / 1000 + infvold / 1000)

            val hbbefcpb = (hbgeneral - hbexf()) / balance

            res1.text = String.format("%.2f" , hbbefcpb)

            val cpbexfvol = cpbexf.text.toString()
            val primevol1 = primevol.text.toString()

            val cpbexfvold = cpbexfvol.toDouble()
            val primevold = primevol1.toDouble()

            val hbexfcpb = hbbefcpb * cpbexfvold / 1000

            val hboncpb = (hbgeneral - hbexfcpb - hbexf()) / (balance - cpbexfvold / 1000 + primevold / 1000)

            res3.text = String.format("%.2f" , hboncpb)


        }

        fun hbbefcpbsecond() {
            val res2: TextView = view.findViewById(R.id.beforecpb2d)
            val res4: TextView = view.findViewById(R.id.aftercpb2d)

            val hbinit1 = hbinit.text.toString()
            val exfvol1 = exfvol.text.toString()
            val diur1 = diur.text.toString()
            val infvol1 = infvol.text.toString()


            val hbinitd = hbinit1.toDouble()
            val exfvold = exfvol1.toDouble()
            val diurd = diur1.toDouble()
            val infvold = infvol1.toDouble()

            val hbgeneral = hbinitd * cbvsecond()

            val balance = (cbvsecond() - exfvold / 1000 - diurd / 1000 + infvold / 1000)

            val hbbefcpb = (hbgeneral - hbexf()) / balance

            res2.text = String.format("%.2f" , hbbefcpb)

            val cpbexfvol = cpbexf.text.toString()
            val primevol1 = primevol.text.toString()

            val cpbexfvold = cpbexfvol.toDouble()
            val primevold = primevol1.toDouble()

            val hbexfcpb = hbbefcpb * cpbexfvold / 1000

            val hboncpb = (hbgeneral - hbexfcpb - hbexf()) / (balance - cpbexfvold / 1000 + primevold / 1000)

            res4.text = String.format("%.2f" , hboncpb)
        }

        val generalTextWatcher: TextWatcher = object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (height.text.isNotEmpty() &&
                        weight.text.isNotEmpty() &&
                        hbinit.text.isNotEmpty() &&
                        infvol.text.isNotEmpty() &&
                        exfvol.text.isNotEmpty() &&
                        diur.text.isNotEmpty() &&
                        primevol.text.isNotEmpty() &&
                        cpbexf.text.isNotEmpty()) {

                        hbbefcpbfirst()
                        hbbefcpbsecond()
                    }
                }
            }
        sex.setOnCheckedChangeListener { _: RadioGroup?, _: Int ->
            if (height.text.isNotEmpty() &&
                weight.text.isNotEmpty() &&
                hbinit.text.isNotEmpty() &&
                infvol.text.isNotEmpty() &&
                exfvol.text.isNotEmpty() &&
                diur.text.isNotEmpty() &&
                primevol.text.isNotEmpty() &&
                cpbexf.text.isNotEmpty()) {

                hbbefcpbfirst()
                hbbefcpbsecond()
            }
        }

        height.addTextChangedListener(generalTextWatcher)
        weight.addTextChangedListener(generalTextWatcher)
        hbinit.addTextChangedListener(generalTextWatcher)
        infvol.addTextChangedListener(generalTextWatcher)
        exfvol.addTextChangedListener(generalTextWatcher)
        diur.addTextChangedListener(generalTextWatcher)
        primevol.addTextChangedListener(generalTextWatcher)
        cpbexf.addTextChangedListener(generalTextWatcher)
    }

    override fun getTitleRes(): Int = R.string.name_hb_on_perfusion
    override fun getCustomAction(): CustomAction {
        return CustomAction(
            iconRes = R.drawable.ic_info,
            onCustomAction = {
                launchFragment(AboutHbPerfision())
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


