package com.vanced.manager.core

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.dezlum.codelabs.getjson.GetJson
import com.vanced.manager.ui.dialogs.DialogContainer.showSecurityDialog
import com.vanced.manager.ui.dialogs.DialogContainer.statementFalse
import com.vanced.manager.ui.fragments.UpdateCheckFragment
import com.vanced.manager.utils.InternetTools
import com.vanced.manager.R
import com.vanced.manager.ui.dialogs.DialogContainer.secondMiuiDialog
import com.vanced.manager.ui.dialogs.DialogContainer.showRootDialog
import com.vanced.manager.utils.MiuiHelper.isMiui
import com.vanced.manager.utils.MiuiHelper.isMiuiOptimisationsDisabled

// This activity will NOT be used in manifest
// since MainActivity will extend it
@SuppressLint("Registered")
open class Main: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkUpdates()

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val firstStart = prefs.getBoolean("firstStart", true)
        val isUpgrading = prefs.getBoolean("isUpgrading", false)
        val variant = prefs.getString("vanced_variant", "nonroot")
        val shouldShowRootDialog = prefs.getBoolean("show_root_dialog", true)

        val falseStatement = prefs.getBoolean("statement", true)

        when {
            firstStart -> showSecurityDialog(this)
            !falseStatement -> statementFalse(this)
            isUpgrading -> prefs.edit().putBoolean("isUpgrading", false).apply()
            variant == "root" && shouldShowRootDialog -> showRootDialog(this)
            !firstStart && !isMiuiOptimisationsDisabled() -> secondMiuiDialog(this)
        }

    }

    override fun onPause() {
        getSharedPreferences("installPrefs", Context.MODE_PRIVATE).edit().putBoolean("isInstalling", false).apply()
        getSharedPreferences("installPrefs", Context.MODE_PRIVATE).edit().putBoolean("isVancedDownloading", false).apply()
        getSharedPreferences("installPrefs", Context.MODE_PRIVATE).edit().putBoolean("isMicrogDownloading", false).apply()
        super.onPause()
    }

    private fun checkUpdates() {
        val checkPrefs = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("auto_check_update", false)
        if (checkPrefs) {
            if (GetJson().isConnected(this) && InternetTools.isUpdateAvailable()) {
                UpdateCheckFragment().show(supportFragmentManager, "Update")
            } else Toast.makeText(this, getString(R.string.update_notfound), Toast.LENGTH_SHORT).show()
        }
    }

}