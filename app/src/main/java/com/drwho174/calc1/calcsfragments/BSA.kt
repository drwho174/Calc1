package com.drwho174.calc1.calcsfragments


import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.drwho174.calc1.R
import com.drwho174.calc1.contract.CustomAction
import com.drwho174.calc1.contract.HasCustomAction
import com.drwho174.calc1.contract.HasCustomTitle
import com.drwho174.calc1.databinding.FragmentBsaBinding
import com.drwho174.calc1.textandsettings.AboutBSA
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlin.math.pow
import android.content.Context as Context


class BSA : Fragment(), HasCustomTitle, HasCustomAction {

    private var _binding : FragmentBsaBinding? = null
    private val binding
    get() = _binding?: throw java.lang.IllegalStateException("_binding for BSAFragment must not be null")

        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBsaBinding.inflate(inflater,container, false)
            return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheetBehaviorResult = BottomSheetBehavior.from(binding.bshBsa)
        bottomSheetBehaviorResult.state = BottomSheetBehavior.STATE_HIDDEN

        fun mostlerCalc():Double{
            val height = binding.etHeight.text.toString()
            val weight = binding.etWeight.text.toString()

            return 0.0167 * height.toDouble().pow(0.5) * weight.toDouble().pow(0.5)
        }

        fun duboisCalc(): Double {
            val height = binding.etHeight.text.toString()
            val weight = binding.etWeight.text.toString()

            val bsacoef = 0.007184

            // Du Bois formula of BSA
            return bsacoef * (height.toDouble().pow(0.725)) * (weight.toDouble().pow(0.425))
        }

        fun hicockCalc(): Double{
            val height = binding.etHeight.text.toString()
            val weight = binding.etWeight.text.toString()

            return 0.024265 * height.toDouble().pow(0.3964) * weight.toDouble().pow(0.5378)

        }



        binding.btCalculate.setOnClickListener {
            hideKeyboard()

            if (binding.etHeight.text?.isNotEmpty() == true && binding.etWeight.text?.isNotEmpty()== true){
                binding.twMoustler.text = String.format("%.1f м2", mostlerCalc())
                binding.twDubois.text = String.format("%.1f м2", duboisCalc())
                binding.twHicock.text = String.format("%.1f м2", hicockCalc())

                bottomSheetBehaviorResult.state = BottomSheetBehavior.STATE_EXPANDED
                binding.layoutHeight.error = null
                binding.layoutWeight.error = null


            }else{
                binding.layoutHeight.error = "Введите рост"
                binding.layoutWeight.error = "Введите вес"

            }
        }

    }

    private fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun getTitleRes(): Int = R.string.name_BSA
    override fun getCustomAction(): CustomAction {
        return CustomAction(
            iconRes = R.drawable.ic_info,
            onCustomAction = {
                launchFragment(AboutBSA())
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