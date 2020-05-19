package com.vanced.manager.core

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager
import com.vanced.manager.R
import com.vanced.manager.core.base.BaseActivity

// This activity will NOT be used in manifest
// since MainActivity will extend it
@SuppressLint("Registered")
open class Main: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val firstStart = prefs.getBoolean("firstStart", true)

        //Easter Egg
        val falseStatement = prefs.getBoolean("statement", true)

        when {
            firstStart -> showSecurityDialog()
            !falseStatement -> statementFalse()
        }

    }

    private fun showSecurityDialog() {
        AlertDialog.Builder(this)
            .setTitle(resources.getString(R.string.welcome))
            .setMessage(resources.getString(R.string.security_context))
            .setPositiveButton(resources.getString(R.string.close)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.edit().putBoolean("firstStart", false).apply()
    }

    //Easter Egg
    private fun statementFalse() {
        AlertDialog.Builder(this)
            .setTitle("Wait what?")
            .setMessage("So this statement is false huh? I'll go with True!")
            .setPositiveButton("wut?") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.edit().putBoolean("statement", true).apply()
    }

}