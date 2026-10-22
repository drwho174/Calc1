package com.drwho174.calc1.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.drwho174.calc1.R
import com.drwho174.calc1.contract.HasCustomTitle
import com.drwho174.calc1.ui.theme.Calc1Theme

/**
 * Универсальный хост справки: разметка берётся из `assets/about/<assetPath>`.
 *
 * Временный слой: на финальном этапе фрагменты заменяются на Navigation 3
 * destinations, а composable-содержимое переиспользуется без изменений.
 */
class AboutFragment : Fragment(), HasCustomTitle {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val assetPath = requireArguments().getString(ARG_ASSET).orEmpty()

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Calc1Theme {
                    AboutScreen(assetPath = assetPath)
                }
            }
        }
    }

    override fun getTitleRes(): Int =
        requireArguments().getInt(ARG_TITLE, R.string.about_calculator)

    companion object {
        private const val ARG_ASSET = "assetPath"
        private const val ARG_TITLE = "titleRes"

        fun newInstance(
            assetPath: String,
            titleRes: Int = R.string.about_calculator,
        ): AboutFragment = AboutFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_ASSET, assetPath)
                putInt(ARG_TITLE, titleRes)
            }
        }
    }
}
