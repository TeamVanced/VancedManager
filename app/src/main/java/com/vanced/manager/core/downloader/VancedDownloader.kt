package com.vanced.manager.core.downloader

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.vanced.manager.R
import com.vanced.manager.utils.*
import com.vanced.manager.utils.AppUtils.log
import com.vanced.manager.utils.AppUtils.validateTheme
import com.vanced.manager.utils.AppUtils.vancedRootPkg
import com.vanced.manager.utils.DownloadHelper.download
import com.vanced.manager.utils.PackageHelper.downloadStockCheck
import com.vanced.manager.utils.PackageHelper.installSplitApkFiles
import com.vanced.manager.utils.PackageHelper.installVancedRoot
import java.io.File

object VancedDownloader {

    private lateinit var prefs: SharedPreferences
    private lateinit var defPrefs: SharedPreferences
    private lateinit var arch: String
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
    private var folderName: String? = null

    fun downloadVanced(context: Context, version: String?) {
        defPrefs = context.defPrefs
        prefs = context.installPrefs
        variant = defPrefs.managerVariant
        folderName = "vanced/$variant"
        downloadPath = context.getExternalFilesDir(folderName)?.path
        File(downloadPath.toString()).deleteRecursively()
        prefs.lang?.let {
            lang = it.split(", ").toMutableList()
        }
        theme = prefs.theme
        vancedVersion = version ?: defPrefs.vancedVersion?.getLatestAppVersion(
            vancedVersions.value?.value ?: listOf("")
        )
        themePath = "$baseInstallUrl/apks/v$vancedVersion/$variant/Theme"
        hashUrl = "apks/v$vancedVersion/$variant/Theme/hash.json"
        arch = getArch()
        count = 0

        vancedVersionCode = vanced.value?.int("versionCode") ?: 0
        try {
            downloadSplits(context)
        } catch (e: Exception) {
            log("VMDownloader", e.stackTraceToString())
            downloadingFile.postValue(context.getString(R.string.error_downloading, "Vanced"))
        }

    }

    private fun downloadSplits(context: Context, type: String = "theme") {
        val url = when (type) {
            "theme" -> "$themePath/$theme.apk"
            "arch" -> "$baseInstallUrl/apks/v$vancedVersion/$variant/Arch/split_config.$arch.apk"
            "stock" -> "$themePath/stock.apk"
            "dpi" -> "$themePath/dpi.apk"
            "lang" -> "$baseInstallUrl/apks/v$vancedVersion/$variant/Language/split_config.${lang[count]}.apk"
            else -> throw NotImplementedError("This type of APK is NOT valid. What the hell did you even do?")
        }

        download(
            url,
            "$baseInstallUrl/",
            folderName!!,
            getFileNameFromUrl(url),
            context,
            onDownloadComplete = {
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
                    "arch" -> if (variant == "root") downloadSplits(
                        context,
                        "stock"
                    ) else downloadSplits(context, "lang")
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
            },
            onError = {
                if (type == "lang") {
                    count++
                    when {
                        count < lang.size -> downloadSplits(context, "lang")
                        succesfulLangCount == 0 -> {
                            lang.add("en")
                            downloadSplits(context, "lang")
                        }
                        else -> startVancedInstall(context)
                    }

                } else {
                    downloadingFile.postValue(
                        context.getString(
                            R.string.error_downloading,
                            getFileNameFromUrl(url)
                        )
                    )
                }
            })
    }

    fun startVancedInstall(context: Context, variant: String? = this.variant) {
        installing.postValue(true)
        postReset()
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            variant?.let { param("vanced_variant", it) }
            theme?.let { param("vanced_theme", it) }
        }
        if (variant == "root")
            installVancedRoot(context)
        else
            installSplitApkFiles(context, "vanced")
    }
}