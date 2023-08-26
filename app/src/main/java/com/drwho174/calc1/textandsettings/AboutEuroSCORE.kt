package com.drwho174.calc1.textandsettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.drwho174.calc1.R
import com.drwho174.calc1.contract.HasCustomTitle
import com.drwho174.calc1.databinding.FragmentAboutEuroscoreBinding
import io.noties.markwon.Markwon
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin

class AboutEuroSCORE : Fragment(), HasCustomTitle {

    private var _binding : FragmentAboutEuroscoreBinding? = null
    private val binding
        get() = _binding?: throw java.lang.IllegalStateException("_binding for AboutEUROSCOREFragment must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutEuroscoreBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val string = context!!.applicationContext
            .assets
            .open("about/EUROSCORE_2.md")
            .bufferedReader()
            .readText()

        val markdowntext = binding.twAboutEuroscore
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

}