package com.drwho174.calc1.contract

import androidx.annotation.DrawableRes

interface HasCustomAction {
    /* @return a custom action specification, see [CustomAction] class for details
    */
    fun getCustomAction(): CustomAction

}

class CustomAction (
    @DrawableRes val iconRes: Int,
    val onCustomAction: Runnable
)
