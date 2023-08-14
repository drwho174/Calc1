package com.drwho174.calc1

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.drwho174.calc1.databinding.FragmentIdialBodyMassBinding

class IdealBodyMass : Fragment() {

    private lateinit var binding : FragmentIdialBodyMassBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentIdialBodyMassBinding.inflate(inflater, container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun sexcoef(): Double {
            val sexcoef = when(binding.SexRadioGroup.checkedRadioButtonId){
                R.id.male -> 50.0
                R.id.female -> 45.5
                else -> 0.0
            }
            return sexcoef
        }

        fun calculator() {
            val height = binding.heightField.text.toString()
            val idealbodymass =  sexcoef() + 0.91 * (height.toDouble() - 152.4)

            binding.idealbodymass.text = String.format("%.1f  кг", idealbodymass)
            println(idealbodymass)
        }

        val textwatcher : TextWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (binding.heightField.text?.isNotEmpty() == true){
                    calculator()
                }
            }

        }

        binding.heightField.addTextChangedListener(textwatcher)

        binding.SexRadioGroup.setOnCheckedChangeListener() { group: RadioGroup?, checkedId: Int ->
            if (binding.heightField.text?.isNotEmpty() == true) {calculator()}
        }
    }
}