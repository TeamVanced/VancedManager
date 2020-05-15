package com.vanced.manager.core

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.vanced.manager.R
import com.vanced.manager.core.base.BaseActivity
import com.vanced.manager.ui.core.ThemedActivity

// This activity will NOT be used in manifest
// since MainActivity will extend it
@SuppressLint("Registered")
open class Main: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val firstStart = prefs.getBoolean("firstStart", true)

        //Easter Egg
        val falseStatement = prefs.getBoolean("statement", true)

        if (firstStart) showSecurityDialog()


        if (!falseStatement) {
            statementFalse()
        }
    }

    private fun showSecurityDialog() {
        AlertDialog.Builder(this)
            .setTitle(resources.getString(R.string.welcome))
            .setMessage(resources.getString(R.string.security_context))
            .setPositiveButton(resources.getString(R.string.close)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()

        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("firstStart", false)
        editor.apply()
    }

    //Easter Egg
    private fun statementFalse() {
        AlertDialog.Builder(this)
            .setTitle("Wait what?")
            .setMessage("So this statement is false huh? I'll go with True!")
            .setPositiveButton("wut?") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()

        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("statement", true)
        editor.apply()
    }

}