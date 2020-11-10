package com.vanced.manager.core.downloader

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.vanced.manager.R
import com.vanced.manager.utils.AppUtils.vancedRootPkg
import com.vanced.manager.utils.DeviceUtils.getArch
import com.vanced.manager.utils.DownloadHelper.downloadProgress
import com.vanced.manager.utils.Extensions.getLatestAppVersion
import com.vanced.manager.utils.InternetTools
import com.vanced.manager.utils.InternetTools.baseUrl
import com.vanced.manager.utils.InternetTools.getFileNameFromUrl
import com.vanced.manager.utils.InternetTools.vanced
import com.vanced.manager.utils.InternetTools.vancedVersions
import com.vanced.manager.utils.LanguageHelper.getDefaultVancedLanguages
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
import java.util.*

object VancedDownloader {

    private var sha256Val: String? = null
    
    private lateinit var prefs: SharedPreferences
    private lateinit var defPrefs: SharedPreferences
    private lateinit var arch: String
    private var installUrl: String? = null
    private var variant: String? = null
    private var theme: String? = null
    private var lang: MutableList<String>? = null

    private lateinit var themePath: String

    private var count: Int = 0
    private var succesfulLangCount: Int = 0
    private var hashUrl = ""

    private var vancedVersionCode = 0
    private var vancedVersion: String? = null

    private var downloadPath: String? = null

    fun downloadVanced(context: Context) {
        defPrefs = getDefaultSharedPreferences(context)
        prefs = context.getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
        variant = defPrefs.getString("vanced_variant", "nonroot")
        downloadPath = context.getExternalFilesDir("vanced/$variant")?.path
        File(downloadPath.toString()).deleteRecursively()
        installUrl = defPrefs.getString("install_url", baseUrl)
        lang = prefs.getString("lang", getDefaultVancedLanguages())?.split(", ")?.toMutableList()
        theme = prefs.getString("theme", "dark")
        vancedVersion = defPrefs.getString("vanced_version", "latest")?.getLatestAppVersion(vancedVersions.get()?.value ?: listOf(""))
        themePath = "$installUrl/apks/v$vancedVersion/$variant/Theme"
        hashUrl = "apks/v$vancedVersion/$variant/Theme/hash.json"
        //newInstaller = defPrefs.getBoolean("new_installer", false)
        arch = getArch()
        count = 0

        vancedVersionCode = vanced.get()?.int("versionCode") ?: 0
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
                    "stock" -> "$themePath/stock.apk"
                    "dpi" ->  "$themePath/dpi.apk"
                    "lang" -> "$installUrl/apks/v$vancedVersion/$variant/Language/split_config.${lang?.get(count)}.apk"
                    else -> throw NotImplementedError("This type of APK is NOT valid. What the hell did you even do?")
                }

            downloadProgress.get()?.currentDownload = PRDownloader.download(url, downloadPath, getFileNameFromUrl(url))
                .build()
                .setOnStartOrResumeListener {
                    downloadProgress.get()?.downloadingFile?.set(context.getString(R.string.downloading_file, getFileNameFromUrl(url)))
                }
                .setOnProgressListener { progress ->
                    downloadProgress.get()?.downloadProgress?.set((progress.currentBytes * 100 / progress.totalBytes).toInt())
                }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        when (type) {
                            "theme" -> 
                                if (variant == "root") {
                                    if (validateTheme(context)) {
                                        if (downloadStockCheck(context))
                                            downloadSplits(context, "arch") 
                                        else 
                                            startVancedInstall(context)
                                    } else 
                                        downloadSplits(context, "theme")
                                } else 
                                    downloadSplits(context, "arch")
                            "arch" -> if (variant == "root") downloadSplits(context, "stock") else downloadSplits(context, "lang")
                            "stock" -> downloadSplits(context, "dpi")
                            "dpi" -> downloadSplits(context, "lang")
                            "lang" -> {
                                count++
                                succesfulLangCount++
                                if (count < lang?.size!!)
                                    downloadSplits(context, "lang")
                                else
                                    startVancedInstall(context)
                            }

                        }
                    }
                    override fun onError(error: Error?) {
                        if (type == "lang") {
                            count++
                            when {
                                count < lang?.size!! -> downloadSplits(context, "lang")
                                succesfulLangCount == 0 -> {
                                    lang?.add("en")
                                    downloadSplits(context, "lang")
                                }
                                else -> startVancedInstall(context)
                            }

                        } else {
                            downloadProgress.get()?.downloadingFile?.set(context.getString(R.string.error_downloading, getFileNameFromUrl(url)))
                        }
                    }
                })
        }
    }

    fun downloadStockCheck(context: Context): Boolean {
        return try {
            getPkgVerCode(vancedRootPkg, context.packageManager) != vancedVersionCode
        } catch (e: Exception) {
            true
        }
    }
    
    private suspend fun getSha256(obj: String, context: Context) {
        sha256Val = InternetTools.getJsonString(hashUrl, obj, context)
    }
    
    private fun validateTheme(context: Context): Boolean {
        val themeF = File(downloadPath, "${theme}.apk")
        runBlocking { getSha256(theme!!, context) }
        return checkSHA256(sha256Val!!,themeF)
    }

    fun startVancedInstall(context: Context, variant: String? = this.variant) {
        downloadProgress.get()?.installing?.set(true)
        downloadProgress.get()?.reset()
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            variant?.let { param("vanced_variant", it) }
            theme?.let { param("vanced_theme", it) }
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

            sum.toLowerCase(Locale.ENGLISH) == sha256.toLowerCase(Locale.ENGLISH)
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
