package com.vanced.manager.core.base

import android.annotation.SuppressLint
import androidx.appcompat.app.AlertDialog
import com.vanced.manager.ui.core.ThemedActivity

@SuppressLint("Registered")
open class BaseActivity: ThemedActivity() {

    fun basicAlertBuilder(title: String, msg: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton("close") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

}