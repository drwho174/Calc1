package com.drwho174.calc1.calcsfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.drwho174.calc1.R
import com.drwho174.calc1.contract.CustomAction
import com.drwho174.calc1.contract.HasCustomAction
import com.drwho174.calc1.contract.HasCustomTitle
import com.drwho174.calc1.ui.about.AboutFragment
import com.drwho174.calc1.ui.calc.mehran.MehranScoreScreen
import com.drwho174.calc1.ui.host.composeScreen

class MehranScore : Fragment(), HasCustomTitle, HasCustomAction {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = composeScreen { MehranScoreScreen() }

    override fun getTitleRes(): Int = R.string.name_mehran_score

    override fun getCustomAction(): CustomAction = CustomAction(
        iconRes = R.drawable.ic_info,
        onCustomAction = Runnable {
            launchFragment(AboutFragment.newInstance("MehraneScore.md"))
        },
    )

    private fun launchFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
