package com.drwho174.calc1.textandsettings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.drwho174.calc1.R
import com.drwho174.calc1.contract.HasCustomTitle
import com.drwho174.calc1.databinding.FragmentAboutBsaBinding
import io.noties.markwon.Markwon
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin

class AboutBSA : Fragment(), HasCustomTitle {

    private var _binding : FragmentAboutBsaBinding? = null
    private val binding
        get() = _binding?: throw java.lang.IllegalStateException("_binding for AboutBSAFragment must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBsaBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val string = requireContext().applicationContext
            .assets
            .open("about/Body_Surface_Area.md")
            .bufferedReader()
            .readText()

        val markdowntext = binding.twAboutBsa
        val markvon : Markwon = Markwon.builder(context?.applicationContext?: return)
            .usePlugin(MarkwonInlineParserPlugin.create())
            .usePlugin(JLatexMathPlugin.create(markdowntext.textSize) { builder ->
                builder.inlinesEnabled(true)
            })
            .usePlugin(HtmlPlugin.create())
            .usePlugin(TablePlugin.create(context?.applicationContext?: return))
            .build()
        markvon.setMarkdown(markdowntext,string)

    }

    override fun getTitleRes(): Int = R.string.about_calculator

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}