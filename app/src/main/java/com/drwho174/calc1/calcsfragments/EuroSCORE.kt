package com.drwho174.calc1.calcsfragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.drwho174.calc1.R
import com.drwho174.calc1.contract.CustomAction
import com.drwho174.calc1.contract.HasCustomAction
import com.drwho174.calc1.contract.HasCustomTitle
import com.drwho174.calc1.databinding.FragmentEuroscoreBinding
import com.drwho174.calc1.textandsettings.AboutEuroSCORE
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlin.math.E
import kotlin.math.pow

class EuroSCORE : Fragment(), HasCustomTitle, HasCustomAction {

    private var _binding : FragmentEuroscoreBinding? = null
    private val binding
    get() = _binding?:throw java.lang.IllegalStateException("_binding for BSAFragment must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEuroscoreBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val euroScoreSheet = BottomSheetBehavior.from(binding.bottomSheetEuroscoreResult)
        euroScoreSheet.state = BottomSheetBehavior.STATE_EXPANDED

//открывает диалог с калькулятором GFR и отправляет туда значения возраста и пола
        binding.btOpenGfrDialog.setOnClickListener{
            val ckddialog = CKDDial()
            ckddialog.show(childFragmentManager, CKDDial.TAG)
            val ageString = binding.etAgeField.text.toString()
            val sexId = binding.rgSex.checkedRadioButtonId

            childFragmentManager.setFragmentResult("age", bundleOf("ageBundle" to ageString))
            childFragmentManager.setFragmentResult("sex", bundleOf("sexBundle" to sexId))
        }

        //childFragmentManager'ы для прослушивания результатов из диалога GFR,
        // записывают результат и выставляют выбор согласно результату
        childFragmentManager.setFragmentResultListener("CKDres", this) { _, bundle ->
            val gfrResult = bundle.getDouble("CKDbundle")
            binding.twGfsRate.text = String.format("%.2f ml/min/1.73m2", gfrResult)
            if (gfrResult > 85.0) {
                binding.rgRenalDisfunction.check(R.id.noRenalDisfunction)
            } else if (gfrResult in 51.0..85.0) {
                binding.rgRenalDisfunction.check(R.id.mildRenalDisfunction)
            } else if (gfrResult <= 50.0) {
                binding.rgRenalDisfunction.check(R.id.severeRenalDisfunction)
            }
        }
        childFragmentManager.setFragmentResultListener("dialysisBool",this){ _, bundle->
               val dialysisBool = bundle.getBoolean("dialysisBundle")

                if (dialysisBool){
                    binding.rgRenalDisfunction.check(R.id.dialisysRenalDisfunction)
                }
            }

        //расчет коэфициента возраста
        fun ageFactor (): Double {
            val age = binding.etAgeField.text.toString()
            val ageFactor : Double = if (age.toDouble() <= 60.0){
                1.0
            }else{
                age.toDouble() - 60.0 + 1.0
            }

            return ageFactor
        }
// расчет суммы коэфициентов для факторов риска
        fun otherFactors(): Double {
    val factorAge = 0.0285181 * ageFactor()

        val factorSex = when (binding.rgSex.checkedRadioButtonId) {
            R.id.female -> 0.2196434
            else -> {
                0.0
            }
        }
        val factorDiabetes = when (binding.rgDiabetis.checkedRadioButtonId) {
            R.id.positiveDiabetis -> 0.3542749
            else -> {
                0.0
            }
        }
        val factorPulmonaryDisfunction =
            when (binding.rgPulmonaryDisfunction.checkedRadioButtonId) {
                R.id.positivePulmDisfunction -> 0.1886564
                else -> {
                    0.0
                }
            }
        val factorPoorMobility = when (binding.rgPoorMobility.checkedRadioButtonId) {
            R.id.positivePoorMobility -> 0.2407181
            else -> {
                0.0
            }
        }
        val factorRenalDisfunction = when (binding.rgRenalDisfunction.checkedRadioButtonId) {
            R.id.noRenalDisfunction -> 0.0
            R.id.mildRenalDisfunction -> 0.303553
            R.id.severeRenalDisfunction -> 0.8592256
            R.id.dialisysRenalDisfunction -> 0.6421508
            else -> {
                0.0
            }
        }
        val factorCriticalPreopState = when (binding.rgCriticalPreopState.checkedRadioButtonId) {
            R.id.positiveCriticalPreopState -> 1.086517
            else -> {
                0.0
            }
        }
        val factorNYHA = when (binding.rgNyhaClass.checkedRadioButtonId) {
            R.id.NYHA1 -> 0.0
            R.id.NYHA2 -> 0.1070545
            R.id.NYHA3 -> 0.2958358
            R.id.NYHA4 -> 0.5597929
            else -> {
                0.0
            }
        }
        val factorAnginaClass4 = when (binding.rgAnginaClass4.checkedRadioButtonId) {
            R.id.positiveAnginaClass4 -> 0.2226147
            else -> {
                0.0
            }
        }
        val factorExtracardiacArteriopathia =
            when (binding.rgExtracardiacArteriopathia.checkedRadioButtonId) {
                R.id.positiveExtracardiacArteriopathia -> 0.5360268
                else -> {
                    0.0
                }
            }
        val factorPreviousCardiacSurgery = when (binding.rgPreviousCardiacSurgery.checkedRadioButtonId) {
            R.id.positivePreviousCardiacSurgery -> 1.118599
            else -> {
                0.0
            }
        }
        val factorActiveEndocarditis = when (binding.rgActiveEndocarditis.checkedRadioButtonId) {
            R.id.positiveActiveEndocarditis -> 0.6194522
            else -> {
                0.0
            }
        }
        val factorEjectionFraction = when (binding.rgEjectionFraction.checkedRadioButtonId) {
            R.id.goodEjectionFraction -> 0.0
            R.id.moderateEjectionFraction -> 0.3150652
            R.id.poorEjectionFraction -> 0.8084096
            R.id.veryPoorEjectionFraction -> 0.9346919
            else -> {
                0.0
            }
        }
        val factorRecentMI = when (binding.rgRecentMi.checkedRadioButtonId) {
            R.id.positiveRecentMI -> 0.1528943
            else -> {
                0.0
            }
        }
        val factorPulmotaryArteryPressure = when (binding.rgPulmonaryArteryPressure.checkedRadioButtonId) {
            R.id.lowPulmonaryArteryPressure -> 0.0
            R.id.moderatePulmonaryArteryPressure -> 0.1788899
            R.id.highPulmonaryArteryPressure -> 0.3491475
            else -> {
                0.0
            }
        }
        val factorUrgency = when (binding.rgUrgency.checkedRadioButtonId) {
            R.id.elective -> 0.0
            R.id.urgent -> 0.3174673
            R.id.emergency -> 0.7039121
            R.id.salvage -> 1.362947
            else -> {
                0.0
            }
        }
        val factorProcedureWeight = when (binding.rgProcedureWeight.checkedRadioButtonId) {
            R.id.cabg -> 0.0
            R.id.nonCabg -> 0.0062118
            R.id.twoProcedures -> 0.5521478
            R.id.threeProcedures -> 0.9724533
            else -> {
                0.0
            }
        }
        val factorAortaSurgery = when (binding.rgAortaSurgery.checkedRadioButtonId) {
            R.id.positiveAortaSurgery -> 0.6527205
            else -> {
                0.0
            }
        }


val otherFactors = factorAge + factorSex + factorDiabetes + factorPulmonaryDisfunction +
        factorPoorMobility + factorRenalDisfunction + factorCriticalPreopState +
        factorNYHA + factorAnginaClass4 + factorExtracardiacArteriopathia +
        factorPreviousCardiacSurgery + factorActiveEndocarditis + factorEjectionFraction +
        factorRecentMI + factorPulmotaryArteryPressure + factorUrgency +
        factorProcedureWeight + factorAortaSurgery

    return otherFactors
        }

// расчет суммы коэфициентов факторов
        fun factorsSumm (): Double {
            return otherFactors() - 5.324537
        }

        //расчет вероятности смерти
        fun predictedMortality(): Double {
            return 100 * (E.pow(factorsSumm()) / (1.0 + E.pow(factorsSumm())))
        }

        //TextWatcher и ChangeListener для расчетов "на лету"
        val ageTextWatcher : TextWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (binding.etAgeField.text?.isNotEmpty() == true){
                    binding.twEuroscoreRate.text = String.format("%.2f%%" , predictedMortality())
//                    euroScoreSheet.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

        }
        binding.etAgeField.addTextChangedListener(ageTextWatcher)

        val customChangeListener =
            OnCheckedChangeListener { _, _ ->
                if (binding.etAgeField.text?.isNotEmpty() == true){
                    binding.twEuroscoreRate.text = String.format("%.2f%%" , predictedMortality())
                    //                    euroScoreSheet.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

        with(binding) {
            rgSex.setOnCheckedChangeListener(customChangeListener)
            rgDiabetis.setOnCheckedChangeListener(customChangeListener)
            rgPulmonaryDisfunction.setOnCheckedChangeListener(customChangeListener)
            rgPoorMobility.setOnCheckedChangeListener(customChangeListener)
            rgRenalDisfunction.setOnCheckedChangeListener(customChangeListener)
            rgCriticalPreopState.setOnCheckedChangeListener(customChangeListener)
            rgNyhaClass.setOnCheckedChangeListener(customChangeListener)
            rgAnginaClass4.setOnCheckedChangeListener(customChangeListener)
            rgExtracardiacArteriopathia.setOnCheckedChangeListener(customChangeListener)
            rgPreviousCardiacSurgery.setOnCheckedChangeListener(customChangeListener)
            rgActiveEndocarditis.setOnCheckedChangeListener(customChangeListener)
            rgEjectionFraction.setOnCheckedChangeListener(customChangeListener)
            rgRecentMi.setOnCheckedChangeListener(customChangeListener)
            rgPulmonaryArteryPressure.setOnCheckedChangeListener(customChangeListener)
            rgUrgency.setOnCheckedChangeListener(customChangeListener)
            rgProcedureWeight.setOnCheckedChangeListener(customChangeListener)
            rgAortaSurgery.setOnCheckedChangeListener(customChangeListener)
        }
    }

    override fun getTitleRes(): Int = R.string.name_euroscore_2
    override fun getCustomAction(): CustomAction {
        return CustomAction(
            iconRes = R.drawable.ic_info,
            onCustomAction = {
                launchFragment(AboutEuroSCORE())
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


