package com.vanced.manager.core.downloader

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.vanced.manager.R
import com.vanced.manager.core.installer.RootSplitInstallerService
import com.vanced.manager.core.installer.SplitInstaller
import com.vanced.manager.ui.fragments.HomeFragment
import com.vanced.manager.utils.AppUtils.installing
import com.vanced.manager.utils.InternetTools.baseUrl
import com.vanced.manager.utils.InternetTools.getFileNameFromUrl
import com.vanced.manager.utils.InternetTools.getObjectFromJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class VancedDownloadService: Service() {

    //private var downloadId: Long = 0
    //private var apkType: String = "arch"
    private var count: Int = 0
    private val localBroadcastManager by lazy { LocalBroadcastManager.getInstance(this) }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        downloadSplits()
        stopSelf()
        return START_NOT_STICKY
    }

    private fun downloadSplits(
        type: String = "arch"
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            File(getExternalFilesDir("apk")?.path as String).deleteRecursively()
            val defPrefs = PreferenceManager.getDefaultSharedPreferences(this@VancedDownloadService)
            val installUrl = defPrefs.getString("install_url", baseUrl)
            val vancedVer = getObjectFromJson("$installUrl/vanced.json", "version")

            val prefs = getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
            val variant = defPrefs.getString("vanced_variant", "nonroot")
            val lang = prefs?.getString("lang", "en")?.split(", ")?.toTypedArray()
            val theme = prefs?.getString("theme", "dark")
            val arch =
                when {
                    Build.SUPPORTED_ABIS.contains("x86") -> "x86"
                    Build.SUPPORTED_ABIS.contains("arm64-v8a") -> "arm64_v8a"
                    else -> "armeabi_v7a"
                }
            val themePath = "$installUrl/apks/v$vancedVer/$variant/Theme/"
            val url =
                when (type) {
                    "arch" -> "$installUrl/apks/v$vancedVer/$variant/Arch/split_config.$arch.apk"
                    "theme" -> "$themePath$theme.apk"
                    "stock" ->  "$themePath/stock.apk"
                    "dpi" ->  "$themePath/dpi.apk"
                    "lang" -> "$installUrl/apks/v$vancedVer/$variant/Language/split_config.${lang?.get(count)}.apk"
                    else -> throw NotImplementedError("This type of APK is NOT valid. What the hell did you even do?")
                }

            //apkType = type
            //downloadId = download(url, "apks", getFileNameFromUrl(url), this@VancedDownloadService)

        PRDownloader
            .download(url, getExternalFilesDir("apks")?.path, getFileNameFromUrl(url))
            .build()
            .setOnStartOrResumeListener{ installing = true }
            .setOnProgressListener { progress ->
                val mProgress = progress.currentBytes * 100 / progress.totalBytes
                localBroadcastManager.sendBroadcast(Intent(HomeFragment.VANCED_DOWNLOADING).putExtra("progress", mProgress.toInt()).putExtra("file", getFileNameFromUrl(url)))
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    when (type) {
                        "arch" -> downloadSplits("theme")
                        "theme" -> downloadSplits("stock")
                        "stock" -> downloadSplits("dpi")
                        "dpi" -> downloadSplits("lang")
                        "lang" -> {
                            count++
                            if (count < lang?.count()!!)
                                downloadSplits("lang")
                            else
                                prepareInstall(variant!!)
                        }

                    }
                }

                override fun onError(error: Error?) {
                    installing = false
                    Toast.makeText(this@VancedDownloadService, getString(R.string.error_downloading, "Vanced"), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    /*
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val prefs = context?.getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
            val variant = PreferenceManager.getDefaultSharedPreferences(this@VancedDownloadService).getString("vanced_variant", "nonroot")
            val lang = prefs?.getString("lang", "en")
            if (intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) == downloadId) {
                when (apkType) {
                    "arch" -> downloadSplits("theme")
                    "theme" -> downloadSplits("lang")
                    "lang" -> {
                        if (lang == "en") {
                            prepareInstall(variant!!)
                            //cancelNotif(channel, this@VancedDownloadService)
                        } else {
                            downloadSplits("enlang")
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

    private fun prepareInstall(variant: String) {
        localBroadcastManager.sendBroadcast(Intent(HomeFragment.VANCED_INSTALLING))
        if (variant == "root")
            startService(Intent(this, RootSplitInstallerService::class.java))
        else
            startService(Intent(this, SplitInstaller::class.java))
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}