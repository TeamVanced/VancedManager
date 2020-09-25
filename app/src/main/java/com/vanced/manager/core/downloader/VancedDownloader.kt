package com.vanced.manager.core.downloader

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.widget.Toast
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.vanced.manager.R
import com.vanced.manager.core.App
import com.vanced.manager.ui.viewmodels.HomeViewModel.Companion.vancedProgress
import com.vanced.manager.utils.AppUtils.mutableInstall
import com.vanced.manager.utils.InternetTools
import com.vanced.manager.utils.InternetTools.baseUrl
import com.vanced.manager.utils.InternetTools.getFileNameFromUrl
import com.vanced.manager.utils.InternetTools.getObjectFromJson
import com.vanced.manager.utils.PackageHelper.getPkgVerCode
import com.vanced.manager.utils.PackageHelper.installVanced
import com.vanced.manager.utils.PackageHelper.installVancedRoot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.IOException
import java.security.MessageDigest

object VancedDownloader {

    private var sha256Val: String? = null
    
    private lateinit var prefs: SharedPreferences
    private lateinit var defPrefs: SharedPreferences
    private lateinit var arch: String
    private var installUrl: String? = null
    private var variant: String? = null
    private var theme: String? = null
    private var lang: Array<String>? = null
    //private var newInstaller: Boolean? = null

    private lateinit var themePath: String
    
    //private var downloadId: Long = 0
    //private var apkType: String = "arch"
    private var count: Int = 0
    private var hashUrl = ""

    private const val yPkg = "com.google.android.youtube"
    private var vancedVersionCode = 0
    private val vancedVersion by lazy { runBlocking { getObjectFromJson("$installUrl/vanced.json", "version") }}

    fun downloadVanced(context: Context){
        //registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        File(context.getExternalFilesDir("apks")?.path as String).deleteRecursively()
        defPrefs = getDefaultSharedPreferences(context)
        installUrl = defPrefs.getString("install_url", baseUrl)
        prefs = context.getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
        variant = defPrefs.getString("vanced_variant", "nonroot")
        lang = prefs.getString("lang", "en")?.split(", ")?.toTypedArray()
        theme = prefs.getString("theme", "dark")
        themePath = "$installUrl/apks/v$vancedVersion/$variant/Theme"
        hashUrl = "apks/v$vancedVersion/$variant/Theme/hash.json"
        //newInstaller = defPrefs.getBoolean("new_installer", false)
        arch = 
            when {
                Build.SUPPORTED_ABIS.contains("x86") -> "x86"
                Build.SUPPORTED_ABIS.contains("arm64-v8a") -> "arm64_v8a"
                else -> "armeabi_v7a"
            }
        val app = context.applicationContext as App
        vancedVersionCode = app.vanced?.int("versionCode") ?: 0
        downloadSplits(context)
    }

    private fun downloadSplits(
        context: Context,
        type: String = "theme"
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val url =
                when (type) {
                    "theme" -> "$themePath/$theme.apk"
                    "arch" -> "$installUrl/apks/v$vancedVersion/$variant/Arch/split_config.$arch.apk"
                    "stock" ->  "$themePath/stock.apk"
                    "dpi" ->  "$themePath/dpi.apk"
                    "lang" -> "$installUrl/apks/v$vancedVersion/$variant/Language/split_config.${lang?.get(count)}.apk"
                    else -> throw NotImplementedError("This type of APK is NOT valid. What the hell did you even do?")
                }

            //apkType = type
            //downloadId = download(url, "apks", getFileNameFromUrl(url), this@VancedDownloadService)

            vancedProgress.get()?.currentDownload = PRDownloader
                .download(url, context.getExternalFilesDir("apks")?.path, getFileNameFromUrl(url))
                .build()
                .setOnStartOrResumeListener { 
                    mutableInstall.value = true
                    vancedProgress.get()?.downloadingFile?.set(context.getString(R.string.downloading_file, getFileNameFromUrl(url)))
                    vancedProgress.get()?.showDownloadBar?.set(true)
                }
                .setOnProgressListener { progress ->
                    vancedProgress.get()?.downloadProgress?.set((progress.currentBytes * 100 / progress.totalBytes).toInt())
                }
                .setOnCancelListener {
                    mutableInstall.value = false
                    vancedProgress.get()?.showDownloadBar?.set(false)
                }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        when (type) {
                            "theme" -> 
                                if (variant == "root") {
                                    if (validateTheme(context)) {
                                        if(downloadStockCheck(context))
                                            downloadSplits(context, "arch") 
                                        else 
                                            prepareInstall(variant!!, context)
                                    } else 
                                        downloadSplits(context, "theme")
                                } else 
                                    downloadSplits(context, "arch")
                            "arch" -> if (variant == "root") downloadSplits(context, "stock") else downloadSplits(context, "lang")
                            "stock" -> downloadSplits(context, "dpi")
                            "dpi" -> downloadSplits(context, "lang")
                            "lang" -> {
                                count++
                                if (count < lang?.count()!!)
                                    downloadSplits(context, "lang")
                                else
                                    prepareInstall(variant!!, context)
                            }

                        }
                    }
                    override fun onError(error: Error?) {
                        if (type == "lang") {
                            count++
                            if (count < lang?.count()!!)
                                downloadSplits(context, "lang")
                            else
                                prepareInstall(variant!!, context)
                        } else {
                            mutableInstall.value = false
                            vancedProgress.get()?.showDownloadBar?.set(false)
                            Toast.makeText(context, context.getString(R.string.error_downloading, "Vanced"), Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        }
    }

    private fun downloadStockCheck(context: Context) :Boolean {
        return try {
            getPkgVerCode(yPkg,context.packageManager) != vancedVersionCode
        } catch (e: Exception) {
            true
        }
    }
    
    private suspend fun getSha256(obj: String, context: Context) {
        sha256Val = InternetTools.getJsonString(hashUrl, obj, context)
    }
    
    private fun validateTheme(context: Context): Boolean {
        val themeS = context.getExternalFilesDir("apks")?.path + "/${theme}.apk"
        val themeF = File(themeS)
        runBlocking { getSha256(theme!!, context) }
        return checkSHA256(sha256Val!!,themeF)
    }

    /*
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val prefs = context?.getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
            val variant = PreferenceManager.getDefaultSharedPreferences(this@VancedDownloadService).getString("vanced_variant", "nonroot")
            val lang = prefs?.getString("lang", "en")
            if (intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) == downloadId) {
                when (apkType) {
                    "arch" -> downloadSplits(context, "theme")
                    "theme" -> downloadSplits(context, "lang")
                    "lang" -> {
                        if (lang == "en") {
                            prepareInstall(variant!!)
                            //cancelNotif(channel, this@VancedDownloadService)
                        } else {
                            downloadSplits(context, "enlang")
                        }
                    }
                    "enlang" -> {
                        prepareInstall(variant!!)
                        //cancelNotif(channel, this@VancedDownloadService)
                    }
                }
            }
        }
    }
     */

    private fun prepareInstall(variant: String, context: Context) {
        vancedProgress.get()?.showDownloadBar?.set(false)
        vancedProgress.get()?.showInstallCircle?.set(true)
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            param("Vanced Variant", variant)
            theme?.let { param("Vanced Theme", it) }
        }
        if (variant == "root")
            installVancedRoot(context)
        else
            installVanced(context)
    }

    private fun checkSHA256(sha256: String, updateFile: File?): Boolean {
        return try {
            val dataBuffer = updateFile!!.readBytes()
            // Generate the checksum
            val sum = generateChecksum(dataBuffer)

            sum == sha256
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    @Throws(IOException::class)
    private fun generateChecksum(data: ByteArray): String {
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
        for (aMessageDigest:Byte in data) {
            var h: String = Integer.toHexString(0xFF and aMessageDigest.toInt())
            while (h.length < 2)
                h = "0$h"
            hexString.append(h)
        }
        return hexString.toString()
    }


}
