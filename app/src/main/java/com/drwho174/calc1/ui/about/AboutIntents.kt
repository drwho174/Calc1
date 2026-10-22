package com.drwho174.calc1.ui.about

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.drwho174.calc1.R

/** Открывает почтовый клиент; при отсутствии обработчика показывает Toast. */
fun Context.openAuthorEmail(email: String) {
    try {
        startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email")))
    } catch (e: ActivityNotFoundException) {
        showNoHandlerToast()
    }
}

/** Открывает Telegram по схеме `tg://`. */
fun Context.openAuthorTelegram(user: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=$user")))
    } catch (e: ActivityNotFoundException) {
        showNoHandlerToast()
    }
}

private fun Context.showNoHandlerToast() {
    Toast.makeText(this, R.string.no_app_to_handle_action, Toast.LENGTH_SHORT).show()
}
