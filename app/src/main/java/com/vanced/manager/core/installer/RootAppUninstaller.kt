package com.vanced.manager.core.installer

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.topjohnwu.superuser.Shell

class RootAppUninstaller: Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val serviceData: Bundle? = intent?.extras
        val data = serviceData?.get("Data")
        val uninstall = Shell.su("pm uninstall $data").exec()
        if (uninstall.isSuccess) {
            Log.d("VMUninstall", "Succesfully uninstalled $data")
        } else {
            Log.d("VMUninstall", "Failed to uninstall $data")
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}