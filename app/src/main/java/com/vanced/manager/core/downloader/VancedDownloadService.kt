package com.vanced.manager.core.downloader

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import com.dezlum.codelabs.getjson.GetJson
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.OnStartOrResumeListener
import com.downloader.PRDownloader
import com.vanced.manager.R
import com.vanced.manager.core.installer.RootSplitInstallerService
import com.vanced.manager.core.installer.SplitInstaller
import com.vanced.manager.ui.fragments.HomeFragment
import com.vanced.manager.utils.InternetTools.baseUrl
import com.vanced.manager.utils.InternetTools.getFileNameFromUrl
import com.vanced.manager.utils.NotificationHelper.cancelNotif
import com.vanced.manager.utils.NotificationHelper.createBasicNotif
import com.vanced.manager.utils.NotificationHelper.displayDownloadNotif
import java.lang.IllegalStateException
import java.util.concurrent.ExecutionException

class VancedDownloadService: Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            downloadSplits()
        } catch (e: Exception) {
            Toast.makeText(this, "Unable to download Vanced", Toast.LENGTH_SHORT).show()
        }
        stopSelf()
        return START_NOT_STICKY
    }

    private fun downloadSplits(
        type: String = "arch"
    ) {
        val baseUrl = PreferenceManager.getDefaultSharedPreferences(this).getString("install_url", baseUrl)
        val vancedVer =
            if(GetJson().isConnected(this))
                try {
                    GetJson().AsJSONObject("https://vanced.app/api/v1/vanced.json").get("vanced").asString
                } catch (e: Exception) {
                    GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/vanced.json").get("vanced").asString
                }
            else
                ""
        val prefs = getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
        val variant = PreferenceManager.getDefaultSharedPreferences(this).getString("vanced_variant", "nonroot")
        val lang = prefs?.getString("lang", "en")
        val theme = prefs?.getString("theme", "dark")
        val arch =
            when {
                Build.SUPPORTED_ABIS.contains("x86") -> "x86"
                Build.SUPPORTED_ABIS.contains("arm64-v8a") -> "arm64_v8a"
                else -> "armeabi_v7a"
            }
        val url =
            when (type) {
                "arch" -> "$baseUrl/apks/v$vancedVer/$variant/Config/config.$arch.apk"
                "theme" -> "$baseUrl/apks/v$vancedVer/$variant/Theme/$theme.apk"
                "lang" -> "$baseUrl/apks/v$vancedVer/$variant/Language/split_config.$lang.apk"
                "enlang" -> "$baseUrl/apks/v$vancedVer/$variant/Language/split_config.en.apk"
                else -> throw NotImplementedError("This type of APK is NOT valid. What the hell did you even do?")
            }

        val channel = 69
        PRDownloader
            .download(url, cacheDir.path, getFileNameFromUrl(url))
            .build()
            .setOnStartOrResumeListener { OnStartOrResumeListener { prefs?.edit()?.putBoolean("isVancedDownloading", true)?.apply() } }
            .setOnProgressListener { progress ->
                val mProgress = progress.currentBytes * 100 / progress.totalBytes
                displayDownloadNotif(channel, mProgress.toInt(), getFileNameFromUrl(url), this)
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    when (type) {
                        "arch" -> downloadSplits("theme")
                        "theme" -> downloadSplits("lang")
                        "lang" -> {
                            if (lang == "en") {
                                prepareInstall(variant!!)
                                cancelNotif(channel, this@VancedDownloadService)
                            } else {
                                downloadSplits("enlang")
                            }
                        }
                        "enlang" -> {
                            prepareInstall(variant!!)
                            cancelNotif(channel, this@VancedDownloadService)
                        }
                    }
                }

                override fun onError(error: Error) {
                    createBasicNotif(getString(R.string.error_downloading, "Vanced"), channel, this@VancedDownloadService)
                }
            })
    }

    private fun prepareInstall(variant: String) {
        val intent = Intent(HomeFragment.VANCED_DOWNLOADED)
        intent.action = HomeFragment.VANCED_DOWNLOADED
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        if (variant == "root")
            startService(Intent(this, RootSplitInstallerService::class.java))
        else
            startService(Intent(this, SplitInstaller::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelNotif(69, this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}