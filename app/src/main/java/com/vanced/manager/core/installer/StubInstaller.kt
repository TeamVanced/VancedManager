package com.vanced.manager.core.installer

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.topjohnwu.superuser.Shell
import com.vanced.manager.ui.fragments.HomeFragment

class StubInstaller: Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val apkFile = assets.openFd("stub.apk")
        Log.d("AppLog", "Installing stub...")
        val installResult = Shell.su("cat \"${apkFile}\" | pm install -t -S ${apkFile.length}").exec()
        Log.d("AppLog", "succeeded installing?${installResult.isSuccess}")
        if (installResult.isSuccess) {
            getSharedPreferences("installPrefs", Context.MODE_PRIVATE).edit().putBoolean("isInstalling", false).apply()
            val mIntent = Intent(HomeFragment.SIGNATURE_DISABLED)
            mIntent.action = HomeFragment.SIGNATURE_DISABLED
            LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent)
        } else {
            val mIntent = Intent(HomeFragment.SIGNATURE_ENABLED)
            mIntent.action = HomeFragment.SIGNATURE_ENABLED
            LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent)
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}