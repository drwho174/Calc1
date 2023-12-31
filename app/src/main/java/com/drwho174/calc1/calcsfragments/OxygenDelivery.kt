package com.drwho174.calc1.calcsfragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.drwho174.calc1.R
import com.drwho174.calc1.contract.CustomAction
import com.drwho174.calc1.contract.HasCustomAction
import com.drwho174.calc1.contract.HasCustomTitle
import com.drwho174.calc1.databinding.FragmentOxyBinding
import com.drwho174.calc1.textandsettings.AboutOxygenDelivery

class OxygenDelivery : Fragment(), HasCustomTitle, HasCustomAction {

    private var _binding: FragmentOxyBinding? = null
    private val binding
    get() = _binding?: throw java.lang.IllegalStateException("_binding in OxygenDeliveryFragment must be not null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOxyBinding.inflate(inflater , container ,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bsa : EditText = binding.bsa
        val cardiacoutput: EditText = binding.cardiacoutput
        val hb: EditText = binding.hb
        val spo2: EditText = binding.spo2
        val po2: EditText = binding.po2
        val etco2: EditText = binding.etco2
        val freshgasflow: EditText = binding.freshgasflow
        val arterialcontentO2 : TextView = binding.arterialcontentO2
        val do2 : TextView = binding.do2
        val do2i : TextView = binding.do2i
        val vco2i : TextView = binding.vco2i
        val do2vco2i : TextView = binding.twDo2ivco2i



        //arterial oxygen content calc
        fun oxygenarterialcontent (): Double {
            val hbtext = hb.text.toString()
            val spo2text = spo2.text.toString()
            val po2text = po2.text.toString()
            val hbmgdl = hbtext.toDouble()/10
            return (hbmgdl * 1.34 * spo2text.toDouble()/100) + (po2text.toDouble() * 0.003)
        }

        //oxygen delivery calc

        fun oxygendelivery(): Double {
            val cardiacoutputtext = cardiacoutput.text.toString()
            return cardiacoutputtext.toDouble() * oxygenarterialcontent() * 10
        }

        fun oxygendeliveryindex(): Double{
            val bsatext = bsa.text.toString()
            return oxygendelivery()/ bsatext.toDouble()
        }

        fun co2productionindex(): Double{
            val etco2text = etco2.text.toString()
            val freshgasflowtext = freshgasflow.text.toString()
            val bsatext = bsa.text.toString()
            return (etco2text.toDouble() * freshgasflowtext.toDouble() * 1000) /
                    (bsatext.toDouble() * 760)
        }

        fun do2indexco2index(): Double{
            return oxygendeliveryindex() / co2productionindex()
        }

        fun colorSignalOfIndex (){
            val v = do2indexco2index()

            if(v >= 5){
                binding.cvDo2Vco2.setCardBackgroundColor(ContextCompat.getColor(requireContext().applicationContext, R.color.green ))
            }else{
                binding.cvDo2Vco2.setCardBackgroundColor(ContextCompat.getColor(requireContext().applicationContext, R.color.red ))
            }
        }

        fun colorSignalOfOxygenDelivery () {
            val v = oxygendeliveryindex()

            if(v > 270.0){
                binding.cvIndexedOxygenDelivery.setCardBackgroundColor(ContextCompat.getColor(requireContext().applicationContext, R.color.green ))
            }else{
                binding.cvIndexedOxygenDelivery.setCardBackgroundColor(ContextCompat.getColor(requireContext().applicationContext, R.color.red ))
            }
        }

        val textWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {

                if (bsa.text.isNotEmpty() &&
                    cardiacoutput.text.isNotEmpty() &&
                    hb.text.isNotEmpty()&&
                    spo2.text.isNotEmpty()&&
                    etco2.text.isNotEmpty()&&
                    po2.text.isNotEmpty()&&
                    freshgasflow.text.isNotEmpty()){

                    colorSignalOfIndex()
                    colorSignalOfOxygenDelivery()

                    arterialcontentO2.text = String.format("%.1f мл/дл",oxygenarterialcontent())
                    do2.text = String.format("%.1f мл/мин" ,oxygendelivery())
                    do2i.text = String.format("%.1f мл/мин/м2",oxygendeliveryindex())
                    vco2i.text = String.format("%.1f мл/мин",co2productionindex())
                    do2vco2i.text = String.format("%.1f", do2indexco2index())

                }
            }
        }

        bsa.addTextChangedListener(textWatcher)
        cardiacoutput.addTextChangedListener(textWatcher)
        hb.addTextChangedListener(textWatcher)
        spo2.addTextChangedListener(textWatcher)
        etco2.addTextChangedListener(textWatcher)
        po2.addTextChangedListener(textWatcher)
        freshgasflow.addTextChangedListener(textWatcher)

    }

    override fun getTitleRes(): Int = R.string.name_oxygen_delivery

    override fun getCustomAction(): CustomAction {
        return CustomAction(
            iconRes = R.drawable.ic_info,
            onCustomAction = {
                launchFragment(AboutOxygenDelivery())
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