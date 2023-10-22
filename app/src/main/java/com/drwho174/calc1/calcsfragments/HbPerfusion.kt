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
import com.drwho174.calc1.databinding.FragmentHbPerfBinding
import com.drwho174.calc1.textandsettings.AboutHbPerfision
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlin.math.pow

class HbPerfusion : Fragment(), HasCustomTitle, HasCustomAction {

    private var _binding : FragmentHbPerfBinding? = null
    private val binding
    get() = _binding?: throw java.lang.IllegalStateException("_binding in HbPerfusionFragment must be not null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHbPerfBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val hbPerfSheet = BottomSheetBehavior.from(binding.bottomSheetHbResult)
        hbPerfSheet.state = BottomSheetBehavior.STATE_EXPANDED

        fun cbvfirst(): Double {
            val height = binding.etHeightField.text.toString()
            val weight = binding.etWeightField.text.toString()


            val circulatiobloodvol = when (binding.rgSexInHbCalc.checkedRadioButtonId) {
                R.id.maleRadioButton -> 0.417 * (height.toDouble() / 100).pow(3) + 0.045 * weight.toDouble() - 0.03
                R.id.femaleRadioButton -> 0.414 * (height.toDouble() / 100).pow(3) + 0.0328 * weight.toDouble() - 0.03

                else -> 0.0
            }
            return circulatiobloodvol
        }

        fun cbvsecond(): Double {
            val height = binding.etHeightField.text.toString()
            val weight = binding.etWeightField.text.toString()

            val circulatiobloodvol = when (binding.rgSexInHbCalc.checkedRadioButtonId) {
                R.id.maleRadioButton -> (367 * (height.toDouble() / 100).pow(3) + 32.2 * weight.toDouble() + 604) / 1000
                R.id.femaleRadioButton -> (356 * (height.toDouble() / 100).pow(3) + 33.1 * weight.toDouble() + 183) / 1000

                else -> { 0.0
                }
            }
            return circulatiobloodvol

        }

        fun hbexf(): Double {
            val hbinit = binding.etHbInitial.text.toString()
            val exfvol = binding.etExfusionVol.text.toString()

            return hbinit.toDouble() * exfvol.toDouble() / 1000

        }

        fun hbbefcpbfirst() {

            val hbinit = binding.etHbInitial.text.toString()
            val exfvol = binding.etExfusionVol.text.toString()
            val diur = binding.etDiuresis.text.toString()
            val infvol = binding.etInfusionVol.text.toString()

            val hbgeneral = hbinit.toDouble() * cbvfirst()

            val balance = (cbvfirst() - exfvol.toDouble() / 1000 - diur.toDouble() / 1000 + infvol.toDouble() / 1000)

            val hbbefcpb = (hbgeneral - hbexf()) / balance

            binding.twBeforeCpb1d.text = String.format("%.2f" , hbbefcpb)

            val cpbexfvol = binding.etCpbExfusionVol.text.toString()
            val primevol = binding.etPrimeVol.text.toString()

            val hbexfcpb = hbbefcpb * cpbexfvol.toDouble() / 1000

            val hboncpb = (hbgeneral - hbexfcpb - hbexf()) / (balance - cpbexfvol.toDouble() / 1000 + primevol.toDouble() / 1000)

            binding.twAfterCpb1d.text = String.format("%.2f" , hboncpb)


        }

        fun hbbefcpbsecond() {
            val hbinit = binding.etHbInitial.text.toString()
            val exfvol = binding.etExfusionVol.text.toString()
            val diur = binding.etDiuresis.text.toString()
            val infvol = binding.etInfusionVol.text.toString()

            val hbgeneral = hbinit.toDouble() * cbvsecond()

            val balance = (cbvsecond() - exfvol.toDouble() / 1000 - diur.toDouble() / 1000 + infvol.toDouble() / 1000)

            val hbbefcpb = (hbgeneral - hbexf()) / balance

            binding.twBeforeCpb2d.text = String.format("%.2f" , hbbefcpb)

            val cpbexfvol = binding.etCpbExfusionVol.text.toString()
            val primevol = binding.etPrimeVol.text.toString()

            val hbexfcpb = hbbefcpb * cpbexfvol.toDouble() / 1000

            val hboncpb = (hbgeneral - hbexfcpb - hbexf()) / (balance - cpbexfvol.toDouble() / 1000 + primevol.toDouble() / 1000)

            binding.twAfterCpb2d.text = String.format("%.2f" , hboncpb)
        }

        val generalTextWatcher: TextWatcher = object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (binding.etHeightField.text?.isNotEmpty() == true &&
                    binding.etWeightField.text?.isNotEmpty() == true &&
                    binding.etHbInitial.text?.isNotEmpty() == true  &&
                    binding.etInfusionVol.text?.isNotEmpty() == true&&
                    binding.etExfusionVol.text?.isNotEmpty() ==true  &&
                    binding.etDiuresis.text?.isNotEmpty() == true &&
                    binding.etPrimeVol.text?.isNotEmpty() == true &&
                    binding.etCpbExfusionVol.text?.isNotEmpty() == true
                ) {

                        hbbefcpbfirst()
                        hbbefcpbsecond()
                    }
                }
            }
        binding.rgSexInHbCalc.setOnCheckedChangeListener { _: RadioGroup?, _: Int ->
            if (binding.etHeightField.text?.isNotEmpty() == true &&
                binding.etWeightField.text?.isNotEmpty() == true &&
                binding.etHbInitial.text?.isNotEmpty() == true  &&
                binding.etInfusionVol.text?.isNotEmpty() == true&&
                binding.etExfusionVol.text?.isNotEmpty() ==true  &&
                binding.etDiuresis.text?.isNotEmpty() == true &&
                binding.etPrimeVol.text?.isNotEmpty() == true &&
                binding.etCpbExfusionVol.text?.isNotEmpty() == true
            ) {

                hbbefcpbfirst()
                hbbefcpbsecond()
            }
        }

        binding.etHeightField.addTextChangedListener(generalTextWatcher)
        binding.etWeightField.addTextChangedListener(generalTextWatcher)
        binding.etHbInitial.addTextChangedListener(generalTextWatcher)
        binding.etInfusionVol.addTextChangedListener(generalTextWatcher)
        binding.etExfusionVol.addTextChangedListener(generalTextWatcher)
        binding.etDiuresis.addTextChangedListener(generalTextWatcher)
        binding.etPrimeVol.addTextChangedListener(generalTextWatcher)
        binding.etCpbExfusionVol.addTextChangedListener(generalTextWatcher)
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


