package com.vanced.manager.utils

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.vanced.manager.BuildConfig
import com.vanced.manager.BuildConfig.APPLICATION_ID
import com.vanced.manager.R
import com.vanced.manager.ui.dialogs.AppDownloadDialog
import com.vanced.manager.ui.fragments.HomeFragment
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import java.security.MessageDigest
import java.util.*

object AppUtils : CoroutineScope by CoroutineScope(Dispatchers.IO) {

    const val vancedPkg = "com.vanced.android.youtube"
    const val vancedRootPkg = "com.google.android.youtube"
    const val musicPkg = "com.vanced.android.apps.youtube.music"
    const val musicRootPkg = "com.google.android.apps.youtube.music"
    const val microgPkg = "com.mgoogle.android.gms"
    const val faqpkg = "com.vanced.faq"
    const val managerPkg = APPLICATION_ID
    const val playStorePkg = "com.android.vending"

    val logs = mutableListOf<Spannable>()

    var currentLocale: Locale? = null

    fun log(tag: String, message: String) {
        logs.add(
            SpannableString("$tag: $message\n").apply {
                setSpan(ForegroundColorSpan(Color.parseColor("#2e73ff")), 0, tag.length + 1, 0)
                setSpan(StyleSpan(Typeface.BOLD), 0, tag.length + 1, 0)
                setSpan(
                    ForegroundColorSpan(Color.MAGENTA),
                    tag.length + 2,
                    tag.length + message.length + 2,
                    0
                )
            }
        )
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        }
    }

    fun sendRefresh(context: Context): Job {
        return launch {
            delay(700)
            LocalBroadcastManager.getInstance(context)
                .sendBroadcast(Intent(HomeFragment.REFRESH_HOME))
        }
    }

    fun sendCloseDialog(context: Context): Job {
        return launch {
            delay(700)
            installing.postValue(false)
            LocalBroadcastManager.getInstance(context)
                .sendBroadcast(Intent(AppDownloadDialog.CLOSE_DIALOG))
        }
    }

    fun sendFailure(error: List<String>, context: Context) {
        sendFailure(error.joinToString(" "), context)
    }

    fun sendFailure(error: String, context: Context): Job {
        //Delay error broadcast until activity (and fragment) get back to the screen
        return launch {
            delay(700)
            val intent = Intent(HomeFragment.INSTALL_FAILED)
            intent.putExtra("errorMsg", getErrorMessage(error, context))
            intent.putExtra("fullErrorMsg", error)
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }
    }

    @Throws(IOException::class)
    fun generateChecksum(data: ByteArray): String {
        try {
            val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
            val hash: ByteArray = digest.digest(data)
            return printableHexString(hash)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    private fun printableHexString(data: ByteArray): String {
        // Create Hex String
        val hexString: StringBuilder = StringBuilder()
        for (aMessageDigest: Byte in data) {
            var h: String = Integer.toHexString(0xFF and aMessageDigest.toInt())
            while (h.length < 2)
                h = "0$h"
            hexString.append(h)
        }
        return hexString.toString()
    }

    fun validateTheme(
        downloadPath: String,
        apk: String,
        hashUrl: String,
        context: Context,
    ): Boolean {
        val themeF = File(downloadPath, "$apk.apk")
        return runBlocking { checkSHA256(getSha256(hashUrl, apk, context), themeF) }
    }

    private fun getErrorMessage(status: String, context: Context): String {
        log("VMInstall", status)
        return when {
            status.contains("INSTALL_FAILED_ABORTED") -> context.getString(R.string.installation_aborted)
            status.contains("INSTALL_FAILED_ALREADY_EXISTS") -> context.getString(R.string.installation_conflict)
            status.contains("INSTALL_FAILED_CPU_ABI_INCOMPATIBLE") -> context.getString(R.string.installation_incompatible)
            status.contains("INSTALL_FAILED_INSUFFICIENT_STORAGE") -> context.getString(R.string.installation_storage)
            status.contains("INSTALL_FAILED_INVALID_APK") -> context.getString(R.string.installation_invalid)
            status.contains("INSTALL_FAILED_VERSION_DOWNGRADE") -> context.getString(R.string.installation_downgrade)
            status.contains("INSTALL_PARSE_FAILED_NO_CERTIFICATES") -> context.getString(R.string.installation_signature)
            status.contains("Failed_Uninstall") -> context.getString(R.string.failed_uninstall)
            status.contains("Chown_Fail") -> context.getString(R.string.chown_fail)
            status.contains("IFile_Missing") -> context.getString(R.string.ifile_missing)
            status.contains("ModApk_Missing") -> context.getString(R.string.modapk_missing)
            status.contains("Files_Missing_VA") -> context.getString(R.string.files_missing_va)
            status.contains("Path_Missing") -> context.getString(R.string.path_missing)
            status.contains("INSTALL_FAILED_INTERNAL_ERROR: Permission Denied") -> context.getString(
                R.string.installation_miui
            )
            else -> context.getString(R.string.installation_failed)
        }
    }
}