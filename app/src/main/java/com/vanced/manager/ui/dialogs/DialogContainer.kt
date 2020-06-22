package com.vanced.manager.ui.dialogs

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.preference.PreferenceManager
import com.vanced.manager.R
import com.vanced.manager.ui.MainActivity
import com.vanced.manager.utils.MiuiHelper

object DialogContainer {

    fun showSecurityDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle(context.resources.getString(R.string.welcome))
            .setMessage(context.resources.getString(R.string.security_context))
            .setPositiveButton(context.resources.getString(R.string.close)) { dialog, _ ->
                run {
                    dialog.dismiss()
                    if (MiuiHelper.isMiui()) {
                        showMiuiDialog(context)
                    }
                }
            }
            .create()
            .show()

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putBoolean("firstStart", false).apply()
    }

    private fun showMiuiDialog(context: Context) {
        basicAlertBuilder("Detected MiUI user!", "Hey! Looks like you're a MiUI user. in order to properly use Vanced Manager, you will have to disable MiUI optimisations in developer settings." +
                "If you can't find such setting, it means that you are using a new version of ROM which does not need fixing anything.", context)
    }

    fun secondMiuiDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("I'm gonna stop you right there!")
            .setMessage("I am once again asking you to disable MiUI optimisations if you have not already. K thx bai")
            .setPositiveButton("wut?") { dialog, _ ->
                run {
                    if (PreferenceManager.getDefaultSharedPreferences(context).getString("vanced_variant", "Nonroot") == "Root")
                        rootModeDetected(context)
                    else
                        dialog.dismiss()
                }
            }
            .create()
            .show()
    }

    fun rootModeDetected(context: Context) {
        basicAlertBuilder("Root mode detected!", "In order for app to work properly, please make sure you disabled signature verification.", context)
    }

    //Easter Egg
    fun statementFalse(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Wait what?")
            .setMessage("So this statement is false huh? I'll go with True!")
            .setPositiveButton("wut?") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putBoolean("statement", true).apply()
    }

    private fun basicAlertBuilder(title: String, msg: String, context: Context) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton("close") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    fun installAlertBuilder(msg: String, context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Error")
            .setMessage(msg)
            .setPositiveButton(context.getString(R.string.close)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    fun regularPackageInstalled(msg: String, activity: MainActivity) {
        AlertDialog.Builder(activity)
            .setTitle("Success")
            .setMessage(msg)
            .setPositiveButton(activity.getString(R.string.close)) { _, _ -> activity.restartActivity() }
            .create()
            .show()
    }

    fun launchVanced(context: Context) {
        val intent = Intent()
        intent.component =
            if (PreferenceManager.getDefaultSharedPreferences(context).getString("vanced_variant", "nonroot") == "root")
                ComponentName("com.google.android.youtube", "com.google.android.youtube.HomeActivity")
            else
                ComponentName("com.vanced.android.youtube", "com.google.android.youtube.HomeActivity")
        AlertDialog.Builder(context)
            .setTitle("Success!")
            .setMessage("Vanced has been successfully installed, do you want to launch it now?")
            .setPositiveButton("Launch") {
                    _, _ -> startActivity(context, intent, null)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                run {
                    dialog.dismiss()
                    MainActivity().restartActivity()
                }
            }
            .setCancelable(false)
            .create()
            .show()
    }

}