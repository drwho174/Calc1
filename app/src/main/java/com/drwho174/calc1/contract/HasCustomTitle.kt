package com.drwho174.calc1.contract

import androidx.annotation.StringRes

interface HasCustomTitle {
    /**
     * @return the string resource which should be displayed instead of default title
     */
    @StringRes
    fun getTitleRes(): Int

}