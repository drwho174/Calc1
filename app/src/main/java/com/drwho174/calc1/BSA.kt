package com.drwho174.calc1


import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider
import kotlin.math.pow


class BSA : AppCompatActivity() {

    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bsa)


        val height: EditText = findViewById(R.id.heightField)
        val weight: EditText = findViewById(R.id.weightField)
        val bsaindex: TextView = findViewById(R.id.bsaIndication)
      //  val perfindex : NumberPicker = findViewById(R.id.perfusionIndexPicker)
        val perfspeed : TextView = findViewById(R.id.PerfSpeed)
        val bmi : TextView = findViewById(R.id.bmi)
        val slider : Slider = findViewById(R.id.perfindexslider)




/*
        //Number picker for perfusion index

        perfindex.minValue = 0
        perfindex.maxValue = 7
        perfindex.value = 4
        val values = arrayOf("2.0","2.1","2.2","2.3","2.4","2.5","2.6","2.7")
        perfindex.displayedValues = values
        perfindex.wrapSelectorWheel = true
        perfindex.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        perfindex.isFocusedByDefault



         perfindex.setOnValueChangedListener { picker, oldVal, newVal ->

             val v = values[perfindex.value].toDouble()
             var bsa = bsaindex.text.toString()
             if (bsa == ""){
                 bsa = "0.0"
             }else {
                 val bsad = (bsa.replace(",", ".")).toDouble()
                 val ps = v * bsad
                 perfspeed.text = String.format("%.2f", ps)
             }
         }
        */

        val mTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(et: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(et: Editable?) {
                when {
                    et === height.editableText -> {

                        val bsa : Double = bsacalc()
                        bsaindex.text = String.format("%.3f" , bsa  )

                        val bmid = bmi()
                        bmi.text = String.format("%.2f" , bmid  )

                        perfspeed.text = String.format("%.2f" , perfspeedslider())


                    }

                    et === weight.editableText -> {

                        val bsa : Double = bsacalc()
                        bsaindex.text = String.format("%.3f" , bsa  )//выведение ответа с тремя знаками после запятой

                        val bmid = bmi()
                        bmi.text = String.format("%.2f" , bmid  )

                        perfspeed.text = String.format("%.2f" , perfspeedslider())

                    }
                }

            }
        }

        height.addTextChangedListener(mTextWatcher)
        weight.addTextChangedListener(mTextWatcher)

        slider.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
            perfspeed.text = String.format("%.2f" , perfspeedslider())
        })



    }

    //Body surface area calculator
    fun bsacalc() : Double {
        val height: EditText = findViewById<EditText>(R.id.heightField)
        val weight: EditText = findViewById<EditText>(R.id.weightField)

        var height1 = height.text.toString()
        var weight1 = weight.text.toString()
        //null pointer exeption and String is empty protection
        if (height1 == "" || weight1 == "") {
            height1 = " "
            weight1 = " "
        } else {
            val heightD = height1.toDouble()
            val weightD = weight1.toDouble()

            val bsacoef = 0.007184
            val bsa = bsacoef * (heightD.pow(0.725)) * (weightD.pow(0.425))// Du Bois formula of BSA
            return bsa

        }
        return 0.0}

    //Body mass index calculator
   fun bmi () : Double{
       val height: EditText = findViewById<EditText>(R.id.heightField)
       val weight: EditText = findViewById<EditText>(R.id.weightField)

       var height1 = height.text.toString()
       var weight1 = weight.text.toString()
       //empty String protection
       if (height1 == "" || weight1 == "") {
           height1 = " "
           weight1 = " "
       } else {
           val heightD = height1.toDouble()
           val weightD = weight1.toDouble()

           val bmi = weightD / ((heightD / 100).pow(2))
           return  bmi
       }
       return 0.0
   }
    //Slider for perfusion index
    fun perfspeedslider (): Double{
        val slider : Slider = findViewById(R.id.perfindexslider)
        val bsaindex: TextView = findViewById(R.id.bsaIndication)

        val v = slider.value
        var bsa = bsaindex.text.toString()
        if (bsa == ""){
            bsa = "0.0"
        }else {
            val bsad = (bsa.replace(",", ".")).toDouble()
            val ps = v * bsad
            return ps
        }
        return 0.0
    }

}





























