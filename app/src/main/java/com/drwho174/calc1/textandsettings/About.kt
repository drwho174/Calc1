package com.drwho174.calc1.textandsettings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.drwho174.calc1.databinding.FragmentAboutBinding
import com.drwho174.calc1.BuildConfig
import com.drwho174.calc1.R
import com.drwho174.calc1.contract.HasCustomTitle

class About : Fragment() , HasCustomTitle{

    private var _binding : FragmentAboutBinding? = null
    private val binding
    get() = _binding?: throw java.lang.IllegalStateException("_binding for AboutFragment must not be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.twVersionNumber.text = BuildConfig.VERSION_NAME
        binding.twAuthorsEmail.setOnClickListener {
            val sendEmail = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto: " + getString(R.string.authors_email)))
            startActivity(sendEmail)
        }
        binding.twAuthorsTelegram.setOnClickListener {
            val openTelegram = Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=drwho174"))
            startActivity(openTelegram)
        }

    }

    override fun getTitleRes(): Int = R.string.name_about

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}