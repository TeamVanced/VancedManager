package com.vanced.manager.core

import android.app.Application
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.dezlum.codelabs.getjson.GetJson
import com.downloader.PRDownloader
import com.vanced.manager.R
import com.vanced.manager.utils.InternetTools
import com.vanced.manager.utils.NotificationHelper.createNotifChannel

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        //checkUpdates()
        PRDownloader.initialize(applicationContext)
        createNotifChannel(this)
    }

    /*
    private fun checkUpdates() {
        val checkPrefs = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("auto_check_update", true)
        if (checkPrefs) {
            if (GetJson().isConnected(this) && InternetTools.isUpdateAvailable()) {
                Toast.makeText(this, getString(R.string.update_found), Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this, getString(R.string.update_notfound), Toast.LENGTH_SHORT).show()
        }
    }

     */

}