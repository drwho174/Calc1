package com.drwho174.calc1.ui.about

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.drwho174.calc1.BuildConfig
import com.drwho174.calc1.R
import com.drwho174.calc1.contract.HasCustomTitle
import com.drwho174.calc1.ui.theme.Calc1Theme

class AppAboutFragment : Fragment(), HasCustomTitle {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            Calc1Theme {
                AboutAppScreen(
                    versionName = BuildConfig.VERSION_NAME,
                    email = getString(R.string.authors_email),
                    telegram = getString(R.string.authors_telegram),
                    onEmailClick = ::openEmail,
                    onTelegramClick = ::openTelegram,
                )
            }
        }
    }

    private fun openEmail() {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_SENDTO,
                    Uri.parse("mailto:" + getString(R.string.authors_email)),
                )
            )
        } catch (e: ActivityNotFoundException) {
            showNoHandlerToast()
        }
    }

    private fun openTelegram() {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("tg://resolve?domain=" + getString(R.string.authors_telegram)),
                )
            )
        } catch (e: ActivityNotFoundException) {
            showNoHandlerToast()
        }
    }

    private fun showNoHandlerToast() {
        Toast.makeText(requireContext(), R.string.no_app_to_handle_action, Toast.LENGTH_SHORT).show()
    }

    override fun getTitleRes(): Int = R.string.name_about
}
