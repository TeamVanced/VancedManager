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
import com.vanced.manager.utils.AppUtils.validateTheme
import com.vanced.manager.utils.AppUtils.vancedRootPkg
import com.vanced.manager.utils.DeviceUtils.getArch
import com.vanced.manager.utils.DownloadHelper.downloadProgress
import com.vanced.manager.utils.Extensions.getInstallUrl
import com.vanced.manager.utils.Extensions.getLatestAppVersion
import com.vanced.manager.utils.InternetTools.backupUrl
import com.vanced.manager.utils.InternetTools.getFileNameFromUrl
import com.vanced.manager.utils.InternetTools.vanced
import com.vanced.manager.utils.InternetTools.vancedVersions
import com.vanced.manager.utils.LanguageHelper.getDefaultVancedLanguages
import com.vanced.manager.utils.PackageHelper.downloadStockCheck
import com.vanced.manager.utils.PackageHelper.installVanced
import com.vanced.manager.utils.PackageHelper.installVancedRoot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

object VancedDownloader: CoroutineScope by CoroutineScope(Dispatchers.IO) {
    
    private lateinit var prefs: SharedPreferences
    private lateinit var defPrefs: SharedPreferences
    private lateinit var arch: String
    private var installUrl: String? = null
    private var variant: String? = null
    private var theme: String? = null
    private var lang = mutableListOf<String>()

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
        installUrl = defPrefs.getInstallUrl()
        prefs.getString("lang", getDefaultVancedLanguages())?.let {
            lang = it.split(", ").toMutableList()
        }
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
        launch {
            val url =
                when (type) {
                    "theme" -> "$themePath/$theme.apk"
                    "arch" -> "$installUrl/apks/v$vancedVersion/$variant/Arch/split_config.$arch.apk"
                    "stock" -> "$themePath/stock.apk"
                    "dpi" ->  "$themePath/dpi.apk"
                    "lang" -> "$installUrl/apks/v$vancedVersion/$variant/Language/split_config.${lang?.get(count)}.apk"
                    else -> throw NotImplementedError("This type of APK is NOT valid. What the hell did you even do?")
                }

            downloadProgress.value?.currentDownload = PRDownloader.download(url, downloadPath, getFileNameFromUrl(url))
                .build()
                .setOnStartOrResumeListener {
                    downloadProgress.value?.downloadingFile?.value = context.getString(R.string.downloading_file, getFileNameFromUrl(url))
                }
                .setOnProgressListener { progress ->
                    downloadProgress.value?.downloadProgress?.value = (progress.currentBytes * 100 / progress.totalBytes).toInt()
                }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        when (type) {
                            "theme" -> 
                                if (variant == "root") {
                                    if (validateTheme(downloadPath!!, theme!!, hashUrl, context)) {
                                        if (downloadStockCheck(vancedRootPkg, vancedVersionCode, context))
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
                                if (count < lang.size)
                                    downloadSplits(context, "lang")
                                else
                                    startVancedInstall(context)
                            }

                        }
                    }
                    override fun onError(error: Error?) {
                        if (installUrl != backupUrl) {
                            installUrl = backupUrl
                            themePath = "$installUrl/apks/v$vancedVersion/$variant/Theme"

                            downloadSplits(context, type)
                            return
                        }

                        if (type == "lang") {
                            count++
                            when {
                                count < lang.size       -> downloadSplits(context, "lang")
                                succesfulLangCount == 0 -> {
                                    lang.add("en")
                                    downloadSplits(context, "lang")
                                }
                                else -> startVancedInstall(context)
                            }

                        } else {
                            downloadProgress.value?.downloadingFile?.value = context.getString(R.string.error_downloading, getFileNameFromUrl(url))
                        }
                    }
                })
        }
    }

    fun startVancedInstall(context: Context, variant: String? = this.variant) {
        downloadProgress.value?.installing?.value = true
        downloadProgress.value?.reset()
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            variant?.let { param("vanced_variant", it) }
            theme?.let { param("vanced_theme", it) }
        }
        if (variant == "root")
            installVancedRoot(context)
        else
            installVanced(context)
    }
}