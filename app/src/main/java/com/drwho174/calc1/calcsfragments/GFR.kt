package com.drwho174.calc1.calcsfragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.drwho174.calc1.R
import com.drwho174.calc1.contract.CustomAction
import com.drwho174.calc1.contract.HasCustomAction
import com.drwho174.calc1.contract.HasCustomTitle
import com.drwho174.calc1.databinding.FragmentGfrBinding
import com.drwho174.calc1.textandsettings.AboutCreatinineClearence
import kotlin.math.pow

class GFR : Fragment(), HasCustomTitle, HasCustomAction {

    private var _binding: FragmentGfrBinding? = null
    private val binding
    get() = _binding?: throw java.lang.IllegalStateException("_binding in GFRFragment must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGfrBinding.inflate(inflater,container,false)
        return binding.root


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        fun sexCoefivient(): Double {
            val sexCoeficient = when(binding.rgSex.checkedRadioButtonId){
                R.id.male -> 1.012
                R.id.female -> 1.0
                else -> 0.0
            }
            return sexCoeficient
        }

        fun calcCKD(): Double {
            val age = binding.etAgeField.text.toString()
            val creatinine = binding.etCreatinineFieldGfrFragment.text.toString()

            val creatinineDouble = creatinine.toDouble() / 88.4055
            val a: Double
            val b: Double

            if (sexCoefivient() == 1.0) {
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
            return 142 * ((creatinineDouble / a).pow(b)) * (0.9938.pow(age.toDouble())) * sexCoefivient()
        }

        val generalTextWatcher: TextWatcher = object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                    if (binding.etAgeField.text?.isNotEmpty() == true
                        && binding.etCreatinineFieldGfrFragment.text?.isNotEmpty() == true){

                        binding.twResultGfr.text = String.format("%.2f ml/min/1.73m2", calcCKD())
                    }
            }
        }

        binding.etAgeField.addTextChangedListener(generalTextWatcher)
        binding.etCreatinineFieldGfrFragment.addTextChangedListener(generalTextWatcher)

    }

    override fun getTitleRes(): Int = R.string.name_glomerular_filtration_rate
    override fun getCustomAction(): CustomAction {
        return CustomAction(
            iconRes = R.drawable.ic_info,
            onCustomAction = {
                launchFragment(AboutCreatinineClearence())
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

