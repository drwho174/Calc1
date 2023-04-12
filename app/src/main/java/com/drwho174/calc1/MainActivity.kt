package com.drwho174.calc1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonEuroSCRButton = findViewById<Button>(R.id.EuroSCRButton)
        buttonEuroSCRButton.setOnClickListener {
val intent = Intent(this,EuroscoreAndCKD::class.java)
           startActivity(intent)
        }
        val buttonHbPerfButton = findViewById<Button>(R.id.HbPerfButton)
        buttonHbPerfButton.setOnClickListener {
            val intent = Intent(this,HbPerfusion::class.java)
            startActivity(intent)
        }
        val buttonOxyButton = findViewById<Button>(R.id.OxyButton)
        buttonOxyButton.setOnClickListener {
            val intent = Intent(this,Oxy::class.java)
            startActivity(intent)
        }
        val buttonBSAButton = findViewById<Button>(R.id.BSAButton)
        buttonBSAButton.setOnClickListener {
            val intent = Intent(this,BSA::class.java)
            startActivity(intent)
        }

    }
}

//todo добавить top app bar к каждому activity(настройки, краткая информация о каждом калькуляторе, название калькулятора)