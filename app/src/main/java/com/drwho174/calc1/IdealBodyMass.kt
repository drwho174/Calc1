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

    private var _binding : FragmentIdialBodyMassBinding? = null
    private val binding
    get() = _binding?: throw java.lang.IllegalStateException("_binding for IdealBodyMass must not be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentIdialBodyMassBinding.inflate(inflater, container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            fun sexcoef(): Double {
                val sexcoef = when (SexRadioGroup.checkedRadioButtonId) {
                    R.id.male -> 50.0
                    R.id.female -> 45.5
                    else -> 0.0
                }
                return sexcoef
            }

            fun calculator() {
                val height = heightField.text.toString()
                val idealbodymassformula = sexcoef() + 0.91 * (height.toDouble() - 152.4)

                idealbodymass.text = String.format("%.1f  кг", idealbodymassformula)
            }

            val textwatcher: TextWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (heightField.text?.isNotEmpty() == true) {
                        calculator()
                    }
                }

            }

            heightField.addTextChangedListener(textwatcher)

            SexRadioGroup.setOnCheckedChangeListener { _: RadioGroup?, _: Int ->
                if (heightField.text?.isNotEmpty() == true) {
                    calculator()
                }
            }
        }
    }
}