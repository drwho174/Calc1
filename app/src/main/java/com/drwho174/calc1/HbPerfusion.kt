package com.drwho174.calc1

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.pow

class HbPerfusion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hb_perf)

        val height: EditText = findViewById(R.id.heightField)
        val weight: EditText = findViewById(R.id.weightFIeld)
        val hbinit: EditText = findViewById(R.id.hbInitial)
        val infvol: EditText = findViewById(R.id.infusionVol)
        val exfvol: EditText = findViewById(R.id.exfusionVol)
        val diur: EditText = findViewById(R.id.diuresis)
        val primevol: EditText = findViewById(R.id.primeVol)
        val cpbexf: EditText = findViewById(R.id.cpbExfusionVol)

        val generalTextWatcher: TextWatcher = object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (height.text.hashCode() === s.hashCode()) {
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
                } else if (weight.text.hashCode() === s.hashCode()) {
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
                } else if (hbinit.text.hashCode() === s.hashCode()) {
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
                } else if (infvol.text.hashCode() === s.hashCode()) {
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
                } else if (exfvol.text.hashCode() === s.hashCode()) {
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
                } else if (diur.text.hashCode() === s.hashCode()) {
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
                } else if (primevol.text.hashCode() === s.hashCode()) {
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
                } else if (cpbexf.text.hashCode() === s.hashCode()) {
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

    private fun cbvfirst(): Double {
        val sex: RadioGroup = findViewById(R.id.SexRadioGroup)

        val height: EditText = findViewById(R.id.heightField)
        val weight: EditText = findViewById(R.id.weightFIeld)


        val height1 = height.text.toString()
        val weight1 = weight.text.toString()

        val heightD = height1.toDouble()
        val weightD = weight1.toDouble()

        val circulatiobloodvol = when (sex.checkedRadioButtonId) {
                R.id.maleRadioButton -> 0.417 * (heightD / 100).pow(3) + 0.045 * weightD - 0.03
                R.id.femaleRadioButton -> 0.414 * (heightD / 100).pow(3) + 0.0328 * weightD - 0.03

            else -> { 0.0
            }
        }
        return circulatiobloodvol

    }

    private fun cbvsecond(): Double {
        val sex: RadioGroup = findViewById(R.id.SexRadioGroup)
        val height: EditText = findViewById(R.id.heightField)
        val weight: EditText = findViewById(R.id.weightFIeld)

        val height1 = height.text.toString()
        val weight1 = weight.text.toString()

        val heightD = height1.toDouble()
        val weightD = weight1.toDouble()

        val circulatiobloodvol = when (sex.checkedRadioButtonId) {
            R.id.maleRadioButton -> (367 * (heightD / 100).pow(3) + 32.2 * weightD + 604) / 1000
            R.id.femaleRadioButton -> (356 * (heightD / 100).pow(3) + 33.1 * weightD - 183) / 1000

            else -> { 0.0
            }
        }
        return circulatiobloodvol

    }

    private fun hbexf(): Double {
        val hbinit: EditText = findViewById(R.id.hbInitial)
        val exfvol: EditText = findViewById(R.id.exfusionVol)

        val hbinit1 = hbinit.text.toString()
        val exfvol1 = exfvol.text.toString()

        val hbinitd = hbinit1.toDouble()
        val exfvold = exfvol1.toDouble()

        return hbinitd * exfvold / 1000

    }

    fun hbbefcpbfirst() {
        val hbinit: EditText = findViewById(R.id.hbInitial)
        val exfvol: EditText = findViewById(R.id.exfusionVol)
        val diur: EditText = findViewById(R.id.diuresis)
        val infvol: EditText = findViewById(R.id.infusionVol)
        val res1: TextView = findViewById(R.id.beforecpb1d)
        val res3: TextView = findViewById(R.id.aftercpb1d)

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

            val cpbexf: EditText = findViewById(R.id.cpbExfusionVol)
            val primevol: EditText = findViewById(R.id.primeVol)

            val cpbexfvol = cpbexf.text.toString()
            val primevol1 = primevol.text.toString()

            val cpbexfvold = cpbexfvol.toDouble()
            val primevold = primevol1.toDouble()

            val hbexfcpb = hbbefcpb * cpbexfvold / 1000

            val hboncpb = (hbgeneral - hbexfcpb - hbexf()) / (balance - cpbexfvold / 1000 + primevold / 1000)

            res3.text = String.format("%.2f" , hboncpb)


        }

    fun hbbefcpbsecond() {

        val hbinit: EditText = findViewById(R.id.hbInitial)
        val exfvol: EditText = findViewById(R.id.exfusionVol)
        val diur: EditText = findViewById(R.id.diuresis)
        val infvol: EditText = findViewById(R.id.infusionVol)
        val res2: TextView = findViewById(R.id.beforecpb2d)
        val res4: TextView = findViewById(R.id.aftercpb2d)

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

            val cpbexf: EditText = findViewById(R.id.cpbExfusionVol)
            val primevol: EditText = findViewById(R.id.primeVol)

            val cpbexfvol = cpbexf.text.toString()
            val primevol1 = primevol.text.toString()

            val cpbexfvold = cpbexfvol.toDouble()
            val primevold = primevol1.toDouble()

            val hbexfcpb = hbbefcpb * cpbexfvold / 1000

            val hboncpb = (hbgeneral - hbexfcpb - hbexf()) / (balance - cpbexfvold / 1000 + primevold / 1000)

            res4.text = String.format("%.2f" , hboncpb)
    }
}


