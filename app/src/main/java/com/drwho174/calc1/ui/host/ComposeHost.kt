package com.drwho174.calc1.ui.host

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.drwho174.calc1.ui.theme.Calc1Theme

/**
 * Временный мост между старым фрагментным каркасом и Compose.
 *
 * Нужен только на время перехода: на финальном этапе Activity станет
 * Compose-хостом с Navigation 3, а фрагменты исчезнут.
 */
fun Fragment.composeScreen(content: @Composable () -> Unit): ComposeView =
    ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            Calc1Theme {
                content()
            }
        }
    }
