package com.drwho174.calc1.ui.components

import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import io.noties.markwon.Markwon
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin

/**
 * Markdown-текст справки.
 *
 * В assets лежит разметка с формулами LaTeX (`$$...$$`), таблицами и `<br>`,
 * поэтому используется Markwon с теми же плагинами, что и во фрагментах:
 * Compose-рендереры Markdown LaTeX не поддерживают.
 */
@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val baseTextSizePx = with(density) { 16.sp.toPx() }

    val markwon = remember(context, baseTextSizePx) {
        Markwon.builder(context)
            .usePlugin(MarkwonInlineParserPlugin.create())
            .usePlugin(HtmlPlugin.create())
            .usePlugin(TablePlugin.create(context))
            .usePlugin(
                JLatexMathPlugin.create(baseTextSizePx) { builder ->
                    builder.inlinesEnabled(true)
                }
            )
            .build()
    }

    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { ctx ->
            TextView(ctx).apply {
                setTextIsSelectable(true)
                setLineSpacing(0f, 1.15f)
            }
        },
        update = { textView ->
            markwon.setMarkdown(textView, markdown)
        },
    )
}
