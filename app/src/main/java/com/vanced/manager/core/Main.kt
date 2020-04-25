package com.vanced.manager.core

import android.annotation.SuppressLint
import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vanced.manager.R
import com.vanced.manager.ui.core.ThemeActivity

@SuppressLint("Registered")
open class Main: ThemeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val firstStart = prefs.getBoolean("firstStart", true)
        if (firstStart) {
            showSecurityDialog()
        }
    }

    private fun showSecurityDialog() {
        MaterialAlertDialogBuilder(this, R.style.DialogTheme)
            .setTitle("Welcome!")
            .setMessage("Please make sure you downloaded " +
                    "app from vanced.app, Vanced Discord server or GitHub")
            .setPositiveButton(
                "Close"
            ) { dialog, _ -> dialog.dismiss() }
            .show()

        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("firstStart", false)
        editor.apply()
    }
}