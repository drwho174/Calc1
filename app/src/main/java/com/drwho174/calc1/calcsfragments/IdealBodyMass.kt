package com.drwho174.calc1.calcsfragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.drwho174.calc1.R
import com.drwho174.calc1.contract.CustomAction
import com.drwho174.calc1.contract.HasCustomAction
import com.drwho174.calc1.contract.HasCustomTitle
import com.drwho174.calc1.databinding.FragmentIdealBodyMassBinding
import com.drwho174.calc1.textandsettings.AboutIdealBodyMass

class IdealBodyMass : Fragment(), HasCustomTitle, HasCustomAction {

    private var _binding : FragmentIdealBodyMassBinding? = null
    private val binding
    get() = _binding?: throw java.lang.IllegalStateException("_binding for IdealBodyMass must not be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentIdealBodyMassBinding.inflate(inflater, container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            fun sexcoef(): Double {
                val sexcoef = when (rgSexInIbmCalc.checkedRadioButtonId) {
                    R.id.male -> 50.0
                    R.id.female -> 45.5
                    else -> 0.0
                }
                return sexcoef
            }

            fun calculator() {
                val height = etHeightField.text.toString()
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
                    if (etHeightField.text?.isNotEmpty() == true) {
                        calculator()
                    }
                }

            }

            etHeightField.addTextChangedListener(textwatcher)

            rgSexInIbmCalc.setOnCheckedChangeListener { _: RadioGroup?, _: Int ->
                if (etHeightField.text?.isNotEmpty() == true) {
                    calculator()
                }
            }
        }
    }

    override fun getTitleRes(): Int = R.string.name_ideal_body_mass

    override fun getCustomAction(): CustomAction {
        return CustomAction(
            iconRes = R.drawable.ic_info,
            onCustomAction = {
                launchFragment(AboutIdealBodyMass())
            }
        )
    }
    //start yours fragment
    private fun launchFragment(fragment: Fragment){
        requireActivity().supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}