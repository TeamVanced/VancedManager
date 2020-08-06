package com.vanced.manager.ui.dialogs

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.preference.PreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vanced.manager.R
import com.vanced.manager.utils.InternetTools.openUrl
import com.vanced.manager.utils.MiuiHelper

object DialogContainer {

    fun showSecurityDialog(context: Context) {
        MaterialAlertDialogBuilder(context)
            .setTitle(context.resources.getString(R.string.welcome))
            .setMessage(context.resources.getString(R.string.security_context))
            .setPositiveButton(context.resources.getString(R.string.close)) { dialog, _ ->
                dialog.dismiss()
            }
            .setOnDismissListener {
                if (MiuiHelper.isMiui()) {
                    showMiuiDialog(context)
                }
            }
            .setOnCancelListener {
                if (MiuiHelper.isMiui()) {
                    showMiuiDialog(context)
                }
            }
            .create()
            .show()

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putBoolean("firstStart", false).apply()
    }

    private fun showMiuiDialog(context: Context) {
        MaterialAlertDialogBuilder(context)
            .setTitle(context.getString(R.string.miui_one_title))
            .setMessage(context.getString(R.string.miui_one))
            .setNeutralButton(context.getString(R.string.close)) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(context.getString(R.string.guide)) { _, _ ->
                openUrl("https://telegra.ph/How-to-install-v15-on-MIUI-02-11", R.color.Telegram, context)
            }
            .setCancelable(false)
            .create()
            .show()
    }

    fun showRootDialog(activity: Activity) {
        MaterialAlertDialogBuilder(activity)
            .setTitle(activity.getString(R.string.hold_on))
            .setMessage(activity.getString(R.string.disable_signature))
            .setNeutralButton(activity.getString(R.string.button_dismiss)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(activity.getString(R.string.guide)) { _, _ ->
                openUrl("https://lmgtfy.com/?q=andnixsh+apk+verification+disable", R.color.Twitter, activity)
            }
            .setOnDismissListener { PreferenceManager.getDefaultSharedPreferences(activity).edit().putBoolean("show_root_dialog", false).apply() }
            .setOnCancelListener { PreferenceManager.getDefaultSharedPreferences(activity).edit().putBoolean("show_root_dialog", false).apply() }
            .create()
            .show()
    }

    //Easter Egg
    fun statementFalse(context: Context) {
        MaterialAlertDialogBuilder(context)
            .setTitle("Wait what?")
            .setMessage("So this statement is false huh? I'll go with True!")
            .setPositiveButton("wut?") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putBoolean("statement", true).apply()
    }

    fun installAlertBuilder(msg: String, context: Context) {
        MaterialAlertDialogBuilder(context).apply {
            setTitle(context.getString(R.string.error))
            setMessage(msg)
            setPositiveButton(context.getString(R.string.close)) { dialog, _ -> dialog.dismiss() }
            when (msg) {
                context.getString(R.string.installation_signature) -> {
                    setNeutralButton(context.getString(R.string.guide)) { _, _ ->
                        openUrl("https://lmgtfy.com/?q=andnixsh+apk+verification+disable", R.color.Twitter, context)
                    }
                }
                context.getString(R.string.installation_miui) -> {
                    setNeutralButton(context.getString(R.string.guide)) { _, _ ->
                        openUrl("https://telegra.ph/How-to-install-v15-on-MIUI-02-11", R.color.Telegram, context)
                    }
                }
            }
            create()
            show()
        }
    }

    fun basicDialog(title: String, msg: String, activity: Activity) {
        MaterialAlertDialogBuilder(activity)
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton(activity.getString(R.string.close)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    fun launchVanced(activity: Activity) {
        val intent = Intent()
        intent.component =
            if (PreferenceManager.getDefaultSharedPreferences(activity).getString("vanced_variant", "nonroot") == "root")
                ComponentName("com.google.android.youtube", "com.google.android.youtube.HomeActivity")
            else
                ComponentName("com.vanced.android.youtube", "com.google.android.youtube.HomeActivity")
        MaterialAlertDialogBuilder(activity)
            .setTitle(activity.getString(R.string.success))
            .setMessage(activity.getString(R.string.vanced_installed))
            .setPositiveButton(activity.getString(R.string.launch)) { _, _ ->
                run {
                    startActivity(activity, intent, null)
                    activity.finish()
                }
            }
            .setNegativeButton(activity.getString(R.string.close)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

}