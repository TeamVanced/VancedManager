package com.vanced.manager.core.downloader

import android.content.Context
import com.vanced.manager.R
import com.vanced.manager.utils.*
import com.vanced.manager.utils.AppUtils.musicRootPkg
import com.vanced.manager.utils.AppUtils.validateTheme
import com.vanced.manager.utils.DownloadHelper.download
import com.vanced.manager.utils.PackageHelper.downloadStockCheck
import com.vanced.manager.utils.PackageHelper.install
import com.vanced.manager.utils.PackageHelper.installMusicRoot

object MusicDownloader {

    private var variant: String? = null
    private var musicVersion: String? = null
    private var versionCode: Int? = null
    private var baseurl = ""
    private var folderName: String? = null
    private var downloadPath: String? = null
    private var hashUrl: String? = null

    fun downloadMusic(context: Context, version: String? = null) {
        val prefs = context.defPrefs
        musicVersion = version ?: prefs.musicVersion?.getLatestAppVersion(
            musicVersions.value?.value ?: listOf("")
        )
        versionCode = music.value?.int("versionCode")
        variant = prefs.managerVariant
        baseurl = "$baseInstallUrl/music/v$musicVersion"
        folderName = "music/$variant"
        downloadPath = context.getExternalFilesDir(folderName)?.path
        hashUrl = "$baseurl/hash.json"

        downloadApk(context)
    }

    private fun downloadApk(context: Context, apk: String = "music") {
        val url = if (apk == "stock") "$baseurl/stock/${getArch()}.apk" else "$baseurl/$variant.apk"
        download(
            url,
            "$baseurl/",
            folderName!!,
            getFileNameFromUrl(url),
            context,
            onDownloadComplete = {
                if (variant == "root" && apk != "stock") {
                    downloadApk(context, "stock")
                    return@download
                }

                when (apk) {
                    "music" -> {
                        if (variant == "root") {
                            if (validateTheme(downloadPath!!, "root", hashUrl!!, context)) {
                                if (downloadStockCheck(musicRootPkg, versionCode!!, context))
                                    downloadApk(context, "stock")
                                else
                                    startMusicInstall(context)
                            } else {
                                downloadApk(context, apk)
                            }
                        } else
                            startMusicInstall(context)
                    }
                    "stock" -> startMusicInstall(context)
                }
            },
            onError = {
                downloadingFile.postValue(
                    context.getString(
                        R.string.error_downloading,
                        getFileNameFromUrl(url)
                    )
                )
            })
    }

    fun startMusicInstall(context: Context) {
        installing.postValue(true)
        postReset()
        if (variant == "root")
            installMusicRoot(context)
        else
            install("${context.getExternalFilesDir("music/nonroot")}/nonroot.apk", context)
    }
}
