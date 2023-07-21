package com.drwho174.calc1

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlin.math.E
import kotlin.math.pow

class EuroSCORE : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {return inflater.inflate(R.layout.fragment_euroscore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //определяем переменные
        val age : EditText = view.findViewById(R.id.ageField)
        val euroscoreRate : TextView = view.findViewById(R.id.euroscoreRate)
        val sex: RadioGroup = view.findViewById(R.id.sex)
        val diabetes: RadioGroup = view.findViewById(R.id.diabetis)
        val pulmonaryDisfunction: RadioGroup = view.findViewById(R.id.pulmonaryDisfunction)
        val poorMobility: RadioGroup = view.findViewById(R.id.poorMobility)
        val renalDisfunction: RadioGroup = view.findViewById(R.id.renalDisfunction)
        val criticalPreopState: RadioGroup = view.findViewById(R.id.criticalPreopState)
        val NYHAclass: RadioGroup = view.findViewById(R.id.NYHAclass)
        val anginaClass4: RadioGroup = view.findViewById(R.id.anginaClass4)
        val extracardiacArteriopathia: RadioGroup = view.findViewById(R.id.extracardiacArteriopathia)
        val previousCardiacSurgery: RadioGroup = view.findViewById(R.id.previousCardiacSurgery)
        val activeEndocarditis: RadioGroup = view.findViewById(R.id.activeEndocarditis)
        val ejectionFraction: RadioGroup = view.findViewById(R.id.ejectionFraction)
        val recentMI: RadioGroup = view.findViewById(R.id.recentMI)
        val pulmonaryArteryPressure: RadioGroup = view.findViewById(R.id.pulmonaryArteryPressure)
        val urgency: RadioGroup = view.findViewById(R.id.urgency)
        val procedureWeight: RadioGroup = view.findViewById(R.id.procedureWeight)
        val aortaSurgery: RadioGroup = view.findViewById(R.id.aortaSurgery)
        val CKDRate : TextView = view.findViewById(R.id.CKDRate)

        val euroScoreSheet = BottomSheetBehavior.from(view.findViewById(R.id.bottom_sheet_euroscore_result))
        euroScoreSheet.state = BottomSheetBehavior.STATE_EXPANDED

//открывает диалог с калькулятором CKD и отправляет туда значения возраста и пола
        val opCKD : Button = view.findViewById(R.id.CKDDialog)
        opCKD.setOnClickListener{
            val ckddialog = CKDDial()
            ckddialog.show(childFragmentManager, CKDDial.TAG)
            val agetext = age.text.toString()
            val sextext = sex.checkedRadioButtonId

            childFragmentManager.setFragmentResult("age", bundleOf("ageBundle" to agetext))
            childFragmentManager.setFragmentResult("sex", bundleOf("sexBundle" to sextext))
        }

        //childFragmentManager'ы для прослушивания результатов из диалога CKD,
        // записывают результат и выставляют выбор согласно результату
        childFragmentManager.setFragmentResultListener("CKDres", this) { _, bundle ->
            val CKDres = bundle.getDouble("CKDbundle")
            CKDRate.text = String.format("%.2f ml/min/1.73m2", CKDres)
            if (CKDres > 85.0) {
                renalDisfunction.check(R.id.noRenalDisfunction)
            } else if (CKDres in 51.0..85.0) {
                renalDisfunction.check(R.id.mildRenalDisfunction)
            } else if (CKDres <= 50.0) {
                renalDisfunction.check(R.id.severeRenalDisfunction)
            }
        }
        childFragmentManager.setFragmentResultListener("dialysisBool",this){requestKey, bundle->
               val dialysisBool = bundle.getBoolean("dialysisBundle")

                if (dialysisBool){
                    renalDisfunction.check(R.id.dialisysRenalDisfunction)
                }
            }

        //расчет коэфициента возраста
        fun ageFactor (): Double {
            val ageText = age.text.toString()
            val ageDouble = ageText.toDouble()
            val ageFactor : Double = if (ageDouble <= 60.0){
                1.0
            }else{
                ageDouble - 60.0 + 1.0
            }

            return ageFactor
        }
// расчет суммы коэфициентов для факторов риска
        fun otherFactors(): Double {
    val factorAge = 0.0285181 * ageFactor()
    val factorSex = when (sex.checkedRadioButtonId) {
                R.id.female -> 0.2196434
                else -> { 0.0
                }
            }
    val factorDiabetes = when (diabetes.checkedRadioButtonId) {
                R.id.positiveDiabetis -> 0.3542749
                else -> { 0.0
                }
            }
    val factorPulmonaryDisfunction = when (pulmonaryDisfunction.checkedRadioButtonId) {
                R.id.positivePulmDisfunction -> 0.1886564
                else -> { 0.0
                }
            }
    val factorPoorMobility = when (poorMobility.checkedRadioButtonId) {
                R.id.positivePoorMobility -> 0.2407181
                else -> { 0.0
                }
            }
    val factorRenalDisfunction = when (renalDisfunction.checkedRadioButtonId) {
         R.id.noRenalDisfunction -> 0.0
         R.id.mildRenalDisfunction -> 0.303553
         R.id.severeRenalDisfunction -> 0.8592256
         R.id.dialisysRenalDisfunction -> 0.6421508
                else -> { 0.0
                }
            }
    val factorCriticalPreopState = when (criticalPreopState.checkedRadioButtonId) {
                R.id.positiveCriticalPreopState -> 1.086517
                else -> { 0.0
                }
            }
    val factorNYHA = when (NYHAclass.checkedRadioButtonId) {
                R.id.NYHA1 -> 0.0
                R.id.NYHA2 -> 0.1070545
                R.id.NYHA3 -> 0.2958358
                R.id.NYHA4 -> 0.5597929
                else -> { 0.0
                }
            }
    val factorAnginaClass4 = when (anginaClass4.checkedRadioButtonId) {
                R.id.positiveAnginaClass4 -> 0.2226147
                else -> { 0.0
                }
            }
    val factorExtracardiacArteriopathia = when (extracardiacArteriopathia.checkedRadioButtonId) {
                R.id.positiveExtracardiacArteriopathia -> 0.5360268
                else -> { 0.0
                }
            }
    val factorPreviousCardiacSurgery = when (previousCardiacSurgery.checkedRadioButtonId) {
                R.id.positivePreviousCardiacSurgery -> 1.118599
                else -> { 0.0
                }
            }
    val factorActiveEndocarditis = when (activeEndocarditis.checkedRadioButtonId) {
                R.id.positiveActiveEndocarditis -> 0.6194522
                else -> { 0.0
                }
            }
    val factorEjectionFraction = when (ejectionFraction.checkedRadioButtonId) {
                R.id.goodEjectionFraction -> 0.0
                R.id.moderateEjectionFraction -> 0.3150652
                R.id.poorEjectionFraction -> 0.8084096
                R.id.veryPoorEjectionFraction -> 0.9346919
                else -> { 0.0
                }
            }
    val factorRecentMI = when (recentMI.checkedRadioButtonId) {
                R.id.positiveRecentMI -> 0.1528943
                else -> { 0.0
                }
            }
    val factorPulmotaryArteryPressure = when (pulmonaryArteryPressure.checkedRadioButtonId) {
                R.id.lowPulmonaryArteryPressure -> 0.0
                R.id.moderatePulmonaryArteryPressure -> 0.1788899
                R.id.highPulmonaryArteryPressure -> 0.3491475
                else -> { 0.0
                }
            }
    val factorUrgency = when (urgency.checkedRadioButtonId) {
                R.id.elective -> 0.0
                R.id.urgent -> 0.3174673
                R.id.emergency -> 0.7039121
                R.id.salvage -> 1.362947
                else -> { 0.0
                }
            }
    val factorProcedureWeight = when (procedureWeight.checkedRadioButtonId) {
                R.id.cabg -> 0.0
                R.id.nonCabg -> 0.0062118
                R.id.twoProcedures -> 0.5521478
                R.id.threeProcedures -> 0.9724533
                else -> { 0.0
                }
            }
    val factorAortaSurgery = when (aortaSurgery.checkedRadioButtonId) {
                R.id.positiveAortaSurgery -> 0.6527205
                else -> { 0.0
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
            val predmort =100 * (E.pow(factorsSumm()) / (1.0 + E.pow(factorsSumm())) )
            return predmort
        }

        //TextWatcher и ChangeListener для расчетов "на лету"
        val ageTextWatcher : TextWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (age.text.isNotEmpty()){
                    euroscoreRate.text = String.format("%.2f%%" , predictedMortality())
//                    euroScoreSheet.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

        }
        age.addTextChangedListener(ageTextWatcher)

        val customChangeListener =
            OnCheckedChangeListener { group, checkedId ->
                if (age.text.isNotEmpty()){
                    euroscoreRate.text = String.format("%.2f%%" , predictedMortality())
                    //                    euroScoreSheet.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

        sex.setOnCheckedChangeListener(customChangeListener)
        diabetes.setOnCheckedChangeListener(customChangeListener)
        pulmonaryDisfunction.setOnCheckedChangeListener(customChangeListener)
        poorMobility.setOnCheckedChangeListener(customChangeListener)
        renalDisfunction.setOnCheckedChangeListener(customChangeListener)
        criticalPreopState.setOnCheckedChangeListener(customChangeListener)
        NYHAclass.setOnCheckedChangeListener(customChangeListener)
        anginaClass4.setOnCheckedChangeListener(customChangeListener)
        extracardiacArteriopathia.setOnCheckedChangeListener(customChangeListener)
        previousCardiacSurgery.setOnCheckedChangeListener(customChangeListener)
        activeEndocarditis.setOnCheckedChangeListener(customChangeListener)
        ejectionFraction.setOnCheckedChangeListener(customChangeListener)
        recentMI.setOnCheckedChangeListener(customChangeListener)
        pulmonaryArteryPressure.setOnCheckedChangeListener(customChangeListener)
        urgency.setOnCheckedChangeListener(customChangeListener)
        procedureWeight.setOnCheckedChangeListener(customChangeListener)
        aortaSurgery.setOnCheckedChangeListener(customChangeListener)
    }

}


