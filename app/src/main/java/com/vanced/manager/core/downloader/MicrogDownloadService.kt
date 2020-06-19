package com.vanced.manager.core.downloader

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.dezlum.codelabs.getjson.GetJson
import com.vanced.manager.ui.fragments.HomeFragment
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import zlc.season.rxdownload4.download
import zlc.season.rxdownload4.file
import zlc.season.rxdownload4.task.Task
import zlc.season.rxdownload4.utils.getFileNameFromUrl

class MicrogDownloadService: Service() {

    private var disposable: Disposable? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        downloadMicrog()
        return START_NOT_STICKY
    }

    private fun downloadMicrog() {
        val prefs = getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
        prefs?.edit()?.putBoolean("isMicrogDownloading", true)?.apply()

        val apkUrl = GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/microg.json")
        val dwnldUrl = apkUrl.get("url").asString
        val task = filesDir?.path?.let {
            Task(
                url = dwnldUrl,
                saveName = getFileNameFromUrl(dwnldUrl),
                savePath = it
            )
        }

        if (task?.file()?.exists()!!)
            task.file().delete()

        disposable = task
            .download()
            .observeOn(Schedulers.newThread())
            .subscribeBy(
                onNext = { progress ->
                    val intent = Intent(HomeFragment.MICROG_DOWNLOADING)
                    intent.action = HomeFragment.MICROG_DOWNLOADING
                    intent.putExtra("microgProgress", progress.percentStr().toInt())
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                },
                onComplete = {
                    prefs?.edit()?.putBoolean("isMicrogDownloading", false)?.apply()
                },
                onError = { throwable ->
                    Toast.makeText(this, throwable.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            )
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}