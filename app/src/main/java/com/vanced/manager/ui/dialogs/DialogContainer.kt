package com.vanced.manager.ui.dialogs

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.preference.PreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vanced.manager.R
import com.vanced.manager.core.downloader.VancedDownloader.downloadVanced
import com.vanced.manager.utils.InternetTools.openUrl
import com.vanced.manager.utils.MiuiHelper
import com.vanced.manager.utils.PackageHelper.installVanced
import com.vanced.manager.utils.PackageHelper.uninstallApk

object DialogContainer {

    fun showSecurityDialog(context: Context) {
        MaterialAlertDialogBuilder(context).apply {
            setTitle(R.string.welcome)
            setMessage(R.string.security_context)
            setPositiveButton(R.string.close) { dialog, _ ->
                dialog.dismiss()
            }
            setOnDismissListener {
                if (MiuiHelper.isMiui()) {
                    showMiuiDialog(context)
                }
            }
            setOnCancelListener {
                if (MiuiHelper.isMiui()) {
                    showMiuiDialog(context)
                }
            }
            create()
            show()
        }
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putBoolean("firstStart", false).apply()
    }

    private fun showMiuiDialog(context: Context) {
        MaterialAlertDialogBuilder(context).apply {
            setTitle(R.string.miui_one_title)
            setMessage(R.string.miui_one)
            setNeutralButton(R.string.close) { dialog, _ -> dialog.dismiss() }
            setPositiveButton(R.string.guide) { _, _ ->
                openUrl(
                    "https://telegra.ph/How-to-install-v15-on-MIUI-02-11",
                    R.color.Telegram,
                    context
                )
            }
            setCancelable(false)
            create()
            show()
        }
    }

    fun showUnofficialAppInstalledDialog(app: String, appPkg: String, context: Context) {
        MaterialAlertDialogBuilder(context).apply {
            setTitle(R.string.warning)
            setMessage(context.getString(R.string.unofficial_app_installed, app))
            setPositiveButton(context.getString(R.string.uninstall)) { _, _ ->
                uninstallApk(appPkg, context)
            }
            setNeutralButton(context.getString(R.string.close)) { dialog, _ ->
                dialog.dismiss()
            }
            setCancelable(false)
            create()
            show()
        }
    }

    //TODO
    fun installOrDownload(context: Context) {
        MaterialAlertDialogBuilder(context).apply {
            setTitle("")
            setMessage("")
            setNegativeButton("") { dialog, _ ->
                downloadVanced(context)
                dialog.dismiss()
            }
            setPositiveButton(context.getString(R.string.button_reinstall)) { dialog, _ ->
                installVanced(context)
                dialog.dismiss()
            }
        }
    }

    //Easter Egg
    fun statementFalse(context: Context) {
        MaterialAlertDialogBuilder(context).apply {
            setTitle("Wait what?")
            setMessage("So this statement is false huh? I'll go with True!")
            setPositiveButton("wut?") { dialog, _ -> dialog.dismiss() }
            create()
            show()
        }

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putBoolean("statement", true).apply()
    }

    fun installAlertBuilder(msg: String, context: Context) {
        MaterialAlertDialogBuilder(context).apply {
            setTitle(context.getString(R.string.error))
            setMessage(msg)
            when (msg) {
                context.getString(R.string.installation_signature) -> {
                    setPositiveButton(context.getString(R.string.guide)) { _, _ ->
                        openUrl("https://lmgtfy.com/?q=andnixsh+apk+verification+disable", R.color.Twitter, context)
                    }
                    setNeutralButton(context.getString(R.string.close)) { dialog, _ -> dialog.dismiss() }
                }
                context.getString(R.string.installation_miui) -> {
                    setPositiveButton(context.getString(R.string.guide)) { _, _ ->
                        openUrl("https://telegra.ph/How-to-install-v15-on-MIUI-02-11", R.color.Telegram, context)
                    }
                    setNeutralButton(context.getString(R.string.close)) { dialog, _ -> dialog.dismiss() }
                }
                else -> setPositiveButton(context.getString(R.string.close)) { dialog, _ -> dialog.dismiss() }
            }
            create()
            show()
        }
    }

    fun basicDialog(title: String, msg: String, activity: Activity) {
        MaterialAlertDialogBuilder(activity).apply {
            setTitle(title)
            setMessage(msg)
            setPositiveButton(activity.getString(R.string.close)) { dialog, _ -> dialog.dismiss() }
            create()
            show()
        }
    }

    fun launchVanced(context: Context) {
        val intent = Intent()
        intent.component =
            if (PreferenceManager.getDefaultSharedPreferences(context).getString("vanced_variant", "nonroot") == "root")
                ComponentName("com.google.android.youtube", "com.google.android.youtube.HomeActivity")
            else
                ComponentName("com.vanced.android.youtube", "com.google.android.youtube.HomeActivity")
                
        MaterialAlertDialogBuilder(context).apply {
            setTitle(context.getString(R.string.success))
            setMessage(context.getString(R.string.vanced_installed))
            setPositiveButton(context.getString(R.string.launch)) { _, _ ->
                startActivity(context, intent, null)
            }
            setNegativeButton(context.getString(R.string.close)) { dialog, _ -> dialog.dismiss() }
            create()
            show()
        }
    }
    
    fun launchMusic(activity: Activity) {
        val intent = Intent()
        intent.component = ComponentName("com.vanced.android.youtube.music", "com.vanced.android.youtube.music.MusicActivity")
        MaterialAlertDialogBuilder(activity).apply {
            setTitle(activity.getString(R.string.success))
            setMessage(activity.getString(R.string.music_installed))
            setPositiveButton(activity.getString(R.string.launch)) { _, _ ->
                startActivity(activity, intent, null)
            }
            setNegativeButton(activity.getString(R.string.close)) { dialog, _ -> dialog.dismiss() }
            create()
            show()
        }
    }

}
