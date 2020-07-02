package com.vanced.manager.core

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.vanced.manager.ui.dialogs.DialogContainer.showRootDialog
import com.vanced.manager.ui.dialogs.DialogContainer.showSecurityDialog
import com.vanced.manager.ui.dialogs.DialogContainer.statementFalse

// This activity will NOT be used in manifest
// since MainActivity will extend it
@SuppressLint("Registered")
open class Main: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val firstStart = prefs.getBoolean("firstStart", true)
        val falseStatement = prefs.getBoolean("statement", true)
        val variant = prefs.getString("vanced_variant", "nonroot")
        val showRootDialog = prefs.getBoolean("show_root_dialog", true)

        when {
            firstStart -> showSecurityDialog(this)
            !falseStatement -> statementFalse(this)
            variant == "root" && showRootDialog -> showRootDialog(this)
        }

    }

    override fun onPause() {
        getSharedPreferences("installPrefs", Context.MODE_PRIVATE).edit().putBoolean("isInstalling", false).apply()
        getSharedPreferences("installPrefs", Context.MODE_PRIVATE).edit().putBoolean("isVancedDownloading", false).apply()
        getSharedPreferences("installPrefs", Context.MODE_PRIVATE).edit().putBoolean("isMicrogDownloading", false).apply()
        super.onPause()
    }

}