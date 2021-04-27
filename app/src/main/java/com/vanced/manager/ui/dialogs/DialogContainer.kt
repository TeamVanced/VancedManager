package com.vanced.manager.ui.dialogs

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vanced.manager.R
import com.vanced.manager.utils.isMiuiOptimizationsEnabled
import com.vanced.manager.utils.openUrl
import com.vanced.manager.utils.showWithAccent

object DialogContainer {

    fun showSecurityDialog(context: Context) {
        MaterialAlertDialogBuilder(context).apply {
            setTitle(context.resources.getString(R.string.welcome))
            setMessage(context.resources.getString(R.string.security_context))
            setPositiveButton(context.resources.getString(R.string.close)) { dialog, _ ->
                dialog.cancel()
            }
            setOnCancelListener {
                if (context.isMiuiOptimizationsEnabled) {
                    miuiDialog(context)
                }
            }
            create()
            showWithAccent()
        }
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit { putBoolean("firstLaunch", false) }
    }

    fun miuiDialog(context: Context) {
        MaterialAlertDialogBuilder(context).apply {
            setTitle(context.getString(R.string.miui_one_title))
            setMessage(context.getString(R.string.miui_one))
            setNeutralButton(context.getString(R.string.close)) { dialog, _ -> dialog.dismiss() }
            setPositiveButton(context.getString(R.string.guide)) { _, _ ->
                openUrl(
                    "https://telegra.ph/How-to-install-v15-on-MIUI-02-11",
                    R.color.Telegram,
                    context
                )
            }
            setCancelable(false)
            create()
            showWithAccent()
        }
    }

    fun statementFalse(context: Context) {
        MaterialAlertDialogBuilder(context).apply {
            setTitle("Wait what?")
            setMessage("So this statement is false huh? I'll go with True!")
            setPositiveButton("wut?") { dialog, _ -> dialog.dismiss() }
            create()
            showWithAccent()
        }

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit { putBoolean("statement", true) }
    }

    fun installAlertBuilder(msg: String, fullMsg: String?, context: Context) {
        MaterialAlertDialogBuilder(context).apply {
            setTitle(context.getString(R.string.error))
            setMessage(msg)
            when (msg) {
                context.getString(R.string.installation_signature) -> {
                    setPositiveButton(context.getString(R.string.guide)) { _, _ ->
                        openUrl(
                            "https://lmgtfy.com/?q=andnixsh+apk+verification+disable",
                            R.color.Twitter,
                            context
                        )
                    }
                    setNeutralButton(context.getString(R.string.close)) { dialog, _ -> dialog.dismiss() }
                    if (fullMsg != null)
                        setNegativeButton(context.getString(R.string.advanced)) { _, _ ->
                            basicDialog(
                                context.getString(R.string.advanced),
                                fullMsg,
                                context
                            )
                        }
                }
                context.getString(R.string.installation_miui) -> {
                    setPositiveButton(context.getString(R.string.guide)) { _, _ ->
                        openUrl(
                            "https://telegra.ph/How-to-install-v15-on-MIUI-02-11",
                            R.color.Telegram,
                            context
                        )
                    }
                    setNeutralButton(context.getString(R.string.close)) { dialog, _ -> dialog.dismiss() }
                    if (fullMsg != null)
                        setNegativeButton(context.getString(R.string.advanced)) { _, _ ->
                            basicDialog(
                                context.getString(R.string.advanced),
                                fullMsg,
                                context
                            )
                        }
                }
                else -> {
                    setPositiveButton(context.getString(R.string.close)) { dialog, _ -> dialog.dismiss() }
                    if (fullMsg != null)
                        setNegativeButton(context.getString(R.string.advanced)) { _, _ ->
                            basicDialog(
                                context.getString(R.string.advanced),
                                fullMsg,
                                context
                            )
                        }
                }
            }
            create()
            showWithAccent()
        }
    }

    fun basicDialog(title: String, msg: String, context: Context) {
        MaterialAlertDialogBuilder(context).apply {
            setTitle(title)
            setMessage(msg)
            setPositiveButton(context.getString(R.string.close)) { dialog, _ -> dialog.dismiss() }
            create()
            showWithAccent()
        }
    }

}
