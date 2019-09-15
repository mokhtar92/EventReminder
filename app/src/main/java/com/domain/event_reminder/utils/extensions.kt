package com.domain.event_reminder.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.domain.event_reminder.R

fun <T> Context.openActivity(destination: Class<T>, bundleKey: String? = null, bundle: Bundle? = null) {
    val intent = Intent(this, destination)
    bundleKey?.let {
        bundle?.let {
            intent.putExtra(bundleKey, bundle)
        }
    }
    startActivity(intent)
}

fun Context.isNetworkConnected(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = cm.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
}


fun Context.showCustomDialog(
    title: String,
    message: String,
    positiveButtonTitle: String = "",
    positiveButtonClicked: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    val builder = AlertDialog.Builder(this)
        .setCancelable(false)
        .setTitle(title)
        .setMessage(message)
        .setNeutralButton(getString(R.string.app_general_cancel)) { dialogInterface, _ ->
            dialogInterface.cancel()
            onCancel.invoke()
        }

    if (positiveButtonTitle.isNotEmpty()) {
        builder.setPositiveButton(positiveButtonTitle) { _, _ -> positiveButtonClicked.invoke() }
    }

    builder.show()
}

fun View.gone() {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
}

fun View.visible() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}