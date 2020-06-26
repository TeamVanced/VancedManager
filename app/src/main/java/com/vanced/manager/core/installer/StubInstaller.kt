package com.vanced.manager.core.installer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.topjohnwu.superuser.Shell
import com.vanced.manager.ui.fragments.HomeFragment
import java.io.*

class StubInstaller: Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val apkFile = File(filesDir, "stub.apk")
        if (!apkFile.exists())  {
            copyStub()
        }
        Log.d("VMpath", apkFile.path)
        Log.d("AppLog", "Installing stub...")
        val installResult = Shell.su("pm install ${apkFile.path}").exec()
        Log.d("AppLog", "succeeded installing?${installResult.isSuccess}")
        if (installResult.isSuccess) {
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

    @Throws(IOException::class)
    private fun copyStub() {
        try {
            val stubName = "stub.apk"
            val inAsset = assets.open(stubName)
            val outFile = File(filesDir, stubName)
            val outAsset: OutputStream = FileOutputStream(outFile)
            val buffer = ByteArray(1024)
            var read: Int
            while (inAsset.read(buffer).also { read = it } != -1) {
                outAsset.write(buffer, 0, read)
            }
        } catch (e: IOException) {
            Log.d("VMStub", "Failed to copy stub")
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

