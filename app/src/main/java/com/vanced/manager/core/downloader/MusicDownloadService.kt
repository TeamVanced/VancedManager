package com.vanced.manager.core.downloader

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.vanced.manager.R
import com.vanced.manager.core.installer.AppInstaller
import com.vanced.manager.ui.fragments.HomeFragment
import com.vanced.manager.utils.AppUtils.installing
import com.vanced.manager.utils.InternetTools.baseUrl
import com.vanced.manager.utils.InternetTools.getFileNameFromUrl
import com.vanced.manager.utils.InternetTools.getJsonString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MusicDownloadService: Service() {

    //private var downloadId: Long = 0
    private val localBroadcastManager by lazy { LocalBroadcastManager.getInstance(this) }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        downloadMusic()
        stopSelf()
        return START_NOT_STICKY
    }

    private fun downloadMusic() {
        CoroutineScope(Dispatchers.IO).launch {
            val version = getJsonString("music.json", "version", this@MusicDownloadService)
            val url = "https://vanced.app/api/v1/music/v$version.apk"

            //downloadId = download(url, "apk", "music.apk", this@MusicDownloadService)

            PRDownloader.download(url, getExternalFilesDir("apk")?.path, "music.apk")
                .build()
                .setOnStartOrResumeListener { installing = true }
                .setOnProgressListener { progress ->
                    val mProgress = progress.currentBytes * 100 / progress.totalBytes
                    localBroadcastManager.sendBroadcast(Intent(HomeFragment.MUSIC_DOWNLOADING).putExtra("progress", mProgress.toInt()).putExtra("file", getFileNameFromUrl(url)))
                }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        val intent = Intent(this@MusicDownloadService, AppInstaller::class.java)
                        intent.putExtra("path", "${getExternalFilesDir("apk")}/music.apk")
                        intent.putExtra("pkg", "com.vanced.android.apps.youtube.music")
                        localBroadcastManager.sendBroadcast(Intent(HomeFragment.MUSIC_INSTALLING))
                        startService(intent)
                    }

                    override fun onError(error: Error?) {
                        installing = false
                        Toast.makeText(this@MusicDownloadService, getString(R.string.error_downloading, "music"), Toast.LENGTH_SHORT).show()
                    }
                })

        }

    }

    /*
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) == downloadId) {
                //prefs?.edit()?.putBoolean("isMusicDownloading", false)?.apply()
                //cancelNotif(channel, this@MusicDownloadService)
                val bIntent = Intent(this@MusicDownloadService, AppInstaller::class.java)
                bIntent.putExtra("path", "${getExternalFilesDir("apk")}/music.apk")
                bIntent.putExtra("pkg", "com.mgoogle.android.gms")
                startService(bIntent)
            }
        }
    }
     */


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
