package com.drwho174.calc1.calcsfragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.drwho174.calc1.R
import com.drwho174.calc1.contract.CustomAction
import com.drwho174.calc1.contract.HasCustomAction
import com.drwho174.calc1.contract.HasCustomTitle
import com.drwho174.calc1.databinding.FragmentRespiratoryCalcBinding
import com.drwho174.calc1.textandsettings.AboutRespiratoryIndex

const val ATMOSPHERE_PRESSURE = 760.0
const val WATER_STEAM_PRESSURE = 47.0

class RespiratoryCalc : Fragment(), HasCustomTitle, HasCustomAction {

    private var _binding : FragmentRespiratoryCalcBinding? = null
    private val binding
    get() = _binding?: throw java.lang.IllegalStateException("_binding in FragmentRespiratoryIndex must be not null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRespiratoryCalcBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    with(binding) {
        fun gradient(): Double {


            val fio2 = etFio2.text.toString()
            val pao2 = etPao2.text.toString()
            val paco2 = etPaco2.text.toString()

            return (fio2.toDouble() / 100 * (ATMOSPHERE_PRESSURE - WATER_STEAM_PRESSURE) - (paco2.toDouble() / 0.8)) - pao2.toDouble()
        }

        fun estimatedGradient (): Double {
            val age = etAge.text.toString()
            return 4 + age.toDouble() / 4
        }

        fun ri(): Double {
            val po2 = etPao2.text.toString()
            return gradient() / po2.toDouble()
        }

        fun pfRatio(): Double {
            val fio2 = etFio2.text.toString()
            val pao2 = etPao2.text.toString()
            return pao2.toDouble() * 100 / fio2.toDouble()
        }

        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (etFio2.text?.isNotEmpty() == true &&
                    etPaco2.text?.isNotEmpty() == true &&
                    etPao2.text?.isNotEmpty() == true ){

                    twAAGradient.text = String.format("A-a градиент: %.1f мм.рт.ст.", gradient())
                    twRi.text = String.format("Респираторный индекс: %.1f", ri())
                    twPF.text = String.format("P/F %.0f", pfRatio())
                } else if(etFio2.text?.isNotEmpty() == true &&
                    etPao2.text?.isNotEmpty() == true){
                    twPF.text = String.format("P/F %.0f", pfRatio())
                }
                if (etAge.text?.isNotEmpty() == true){
                    twEstimatedAAGradient.text = String.format("Нормальный pO2(A-a) %.1f", estimatedGradient())
                }
            }
        }
        etFio2.addTextChangedListener(textWatcher)
        etPao2.addTextChangedListener(textWatcher)
        etPaco2.addTextChangedListener(textWatcher)
        etAge.addTextChangedListener(textWatcher)
    }
    }

    override fun getCustomAction(): CustomAction {
        return CustomAction(
            iconRes = R.drawable.ic_info,
            onCustomAction = {
                launchFragment(AboutRespiratoryIndex())
            }
        )
    }

    override fun getTitleRes(): Int = R.string.name_oxygenation_params

    private fun launchFragment(fragment: Fragment){
        requireActivity().supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}