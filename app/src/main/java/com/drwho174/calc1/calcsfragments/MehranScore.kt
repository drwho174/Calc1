package com.drwho174.calc1.calcsfragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import com.drwho174.calc1.R
import com.drwho174.calc1.contract.CustomAction
import com.drwho174.calc1.contract.HasCustomAction
import com.drwho174.calc1.contract.HasCustomTitle
import com.drwho174.calc1.databinding.FragmentMehranScoreBinding
import com.drwho174.calc1.textandsettings.AboutMehraneScore
import kotlin.math.roundToInt


class MehranScore : Fragment(), HasCustomTitle, HasCustomAction {

    private var _binding : FragmentMehranScoreBinding? = null
    private val binding
    get() = _binding?: throw java.lang.IllegalStateException("_binding in MehranScoreFragment must be not null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentMehranScoreBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btGfrInMehranScore.setOnClickListener{
            val ckddialog = CKDDial()
            ckddialog.show(childFragmentManager, CKDDial.TAG)
        }

        childFragmentManager.setFragmentResultListener("CKDres", this) { _, bundle ->
            val gfrResult = bundle.getDouble("CKDbundle")
            binding.twGfrInMehraneScore.text = String.format("СКФ %.2f ml/min/1.73m2", gfrResult)
            if (gfrResult > 60.0) {
                binding.rgEGFR.check(R.id.rb_high_gfr)
            } else if (gfrResult in 40.0..60.0) {
                binding.rgEGFR.check(R.id.rb_middle_gfr)
            } else if (gfrResult in 20.0..40.0) {
                binding.rgEGFR.check(R.id.rb_low_gfr)
            } else if (gfrResult < 20.0){
                binding.rgEGFR.check(R.id.rb_lowest_gfr)
            }
        }

        fun heartFailure(): Int {
            return if (binding.cbHeartFalure.isChecked) {
                5
            } else {
                0
            }
        }

        fun frailty(): Int {
            return if (binding.cbFrailty.isChecked) {
                4
            } else {
                0
            }
        }

        fun iabp(): Int {
            return if (binding.cbIabp.isChecked) {
                5
            } else {
                0
            }
        }

        fun hypotension(): Int {
            return if (binding.cbHypotension.isChecked) {
                5
            } else {
                0
            }
        }

        fun anaemia(): Int {
            return if (binding.cbAnaemia.isChecked) {
                3
            } else {
                0
            }
        }

        fun diabetes(): Int {
            return if (binding.cbDiabetes.isChecked) {
                5
            } else {
                0
            }
        }

        fun contrastVol(): Int {
            val vol = binding.etContrastVolume.text.toString()
            val points = (vol.toDouble() / 100).roundToInt()
            return points
        }

        fun gfr(): Int {
            val points = when (binding.rgEGFR.checkedRadioButtonId) {
                R.id.rb_high_gfr -> 0
                R.id.rb_middle_gfr -> 2
                R.id.rb_low_gfr -> 4
                R.id.rb_lowest_gfr -> 6
                else -> {
                    0
                }
            }
            return points
        }

        fun risksumm(): Int {

            val pointsSumm =
                heartFailure() + frailty() + iabp() + hypotension() + anaemia() + diabetes() + contrastVol() + gfr()

            return pointsSumm
        }

        fun cinrisk() {
            val cinRisk = when {
                risksumm() <= 5 -> 7.5
                risksumm() in 6..10 -> 14.0
                risksumm() in 11..15 -> 26.1
                risksumm() >= 16 -> 57.3
                else -> 0
            }
            binding.twNephropatiaRisk.text = String.format("Риск Нефропатии: %.1f", cinRisk) + " %"
        }

        fun dialisysrisk() {
            val dialisysRisk = when {
                risksumm() <= 5 -> 0.04
                risksumm() in 6..10 -> 0.12
                risksumm() in 11..15 -> 1.09
                risksumm() >= 16 -> 12.6
                else -> 0
            }
            binding.twDialisysRisk.text = String.format("Риск Диализа: %.1f", dialisysRisk) + " %"
        }

        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {

                if (binding.etContrastVolume.text?.isNotEmpty() == true) {
                    cinrisk()
                    dialisysrisk()
                }
            }
        }

        val customChangeListener: CompoundButton.OnCheckedChangeListener =
            CompoundButton.OnCheckedChangeListener { _, _ ->
                if (binding.etContrastVolume.text?.isNotEmpty() == true){
                    cinrisk()
                    dialisysrisk()
                }
            }

        with(binding) {
            etContrastVolume.addTextChangedListener(textWatcher)

            cbAnaemia.setOnCheckedChangeListener(customChangeListener)
            cbDiabetes.setOnCheckedChangeListener(customChangeListener)
            cbHeartFalure.setOnCheckedChangeListener(customChangeListener)
            cbIabp.setOnCheckedChangeListener(customChangeListener)
            cbFrailty.setOnCheckedChangeListener(customChangeListener)
            cbHypotension.setOnCheckedChangeListener(customChangeListener)
            rgEGFR.setOnCheckedChangeListener {  _,_ ->
                if (binding.etContrastVolume.text?.isNotEmpty() == true) {
                    cinrisk()
                    dialisysrisk()
                }
            }

        }

    }

    override fun getCustomAction(): CustomAction {
        return CustomAction(
            iconRes = R.drawable.ic_info,
            onCustomAction = {
                launchFragment(AboutMehraneScore())
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

    override fun getTitleRes(): Int = R.string.name_mehran_score

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}