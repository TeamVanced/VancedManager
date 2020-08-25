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
import com.vanced.manager.utils.InternetTools
import com.vanced.manager.utils.InternetTools.baseUrl
import com.vanced.manager.utils.InternetTools.getFileNameFromUrl
import com.vanced.manager.utils.InternetTools.getObjectFromJson
import com.vanced.manager.utils.PackageHelper.getPkgVerCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.IOException
import java.security.MessageDigest

class VancedDownloadService: Service() {

    private var sha256Val: String? = null
    private var vancedVersionCode: Int? = null

    //private var downloadId: Long = 0
    //private var apkType: String = "arch"
    private var count: Int = 0
    private val localBroadcastManager by lazy { LocalBroadcastManager.getInstance(this) }
    private var hashUrl = ""

    private val yPkg = "com.google.android.youtube"

    suspend fun getVer()
    {
        vancedVersionCode = InternetTools.getJsonInt("vanced.json", "versionCode", application)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        runBlocking { getVer() }
        downloadSplits()
        stopSelf()
        return START_NOT_STICKY
    }

    private fun downloadSplits(
        type: String = "theme"
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
            val themePath = "$installUrl/apks/v$vancedVer/$variant/Theme"
            hashUrl = "apks/v$vancedVer/$variant/Theme/hash.json"
            val url =
                when (type) {
                    "arch" -> "$installUrl/apks/v$vancedVer/$variant/Arch/split_config.$arch.apk"
                    "theme" -> "$themePath/$theme.apk"
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
                        "theme" -> if(variant=="root") {
                            if(ValidateTheme()) {if(downloadStockCheck())downloadSplits("arch") else prepareInstall(variant)} else downloadSplits("theme")
                        } else downloadSplits("arch")
                        "arch" -> if(variant=="root") downloadSplits("stock") else downloadSplits("lang")
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

    private fun downloadStockCheck():Boolean
    {
        return getPkgVerCode(yPkg, packageManager) != vancedVersionCode
    }
    suspend fun getSha256(obj: String) {
        sha256Val = InternetTools.getJsonString(hashUrl,obj,applicationContext)
    }
    private fun ValidateTheme(): Boolean
    {
        val prefs = getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
        val theme = prefs?.getString("theme", "dark")
        val themeS = getExternalFilesDir("apks")?.path + "/${theme}.apk"
        val themeF = File(themeS)
        runBlocking { getSha256(theme!!) }
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