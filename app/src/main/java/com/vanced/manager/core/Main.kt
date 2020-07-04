package com.vanced.manager.core

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.dezlum.codelabs.getjson.GetJson
import com.vanced.manager.R
import com.vanced.manager.ui.dialogs.DialogContainer.basicDialog
import com.vanced.manager.ui.dialogs.DialogContainer.showRootDialog
import com.vanced.manager.ui.dialogs.DialogContainer.showSecurityDialog
import com.vanced.manager.ui.dialogs.DialogContainer.statementFalse
import com.vanced.manager.ui.fragments.UpdateCheckFragment
import com.vanced.manager.utils.InternetTools.isUpdateAvailable
import com.vanced.manager.utils.PackageHelper.getPackageVersionName

// This activity will NOT be used in manifest
// since MainActivity will extend it
@SuppressLint("Registered")
open class Main: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val variant = prefs.getString("vanced_variant", "nonroot")
        val showRootDialog = prefs.getBoolean("show_root_dialog", true)

        when {
            prefs.getBoolean("firstStart", true) -> showSecurityDialog(this)
            !prefs.getBoolean("statement", true) -> statementFalse(this)
            variant == "root" -> {
                if (showRootDialog)
                    showRootDialog(this)

                if (getPackageVersionName("com.google.android.youtube", packageManager) == "14.21.54")
                    basicDialog(getString(R.string.hold_on), getString(R.string.magisk_vanced), this)
            }
        }

        checkUpdates()

    }

    override fun onPause() {
        getSharedPreferences("installPrefs", Context.MODE_PRIVATE).edit().putBoolean("isInstalling", false).apply()
        getSharedPreferences("installPrefs", Context.MODE_PRIVATE).edit().putBoolean("isVancedDownloading", false).apply()
        getSharedPreferences("installPrefs", Context.MODE_PRIVATE).edit().putBoolean("isMicrogDownloading", false).apply()
        super.onPause()
    }

    private fun checkUpdates() {
        if (GetJson().isConnected(this) && isUpdateAvailable()) {
            val fm = supportFragmentManager
            UpdateCheckFragment().show(fm, "UpdateCheck")
        }
    }

}