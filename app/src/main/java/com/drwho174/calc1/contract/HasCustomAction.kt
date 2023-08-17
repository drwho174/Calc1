package com.drwho174.calc1.contract

import androidx.core.view.MenuHost

interface HasCustomAction {
    /* @return a custom action specification, see [CustomAction] class for details
    */
    fun getCustomAction(): CustomAction

}

class CustomAction (
menuHost: MenuHost,
    val onCustomAction: Runnable
)
