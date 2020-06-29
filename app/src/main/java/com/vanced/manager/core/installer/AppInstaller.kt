package com.vanced.manager.core.installer

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.IBinder
import java.io.FileInputStream
import java.io.InputStream

class AppInstaller: Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val callbackIntent = Intent(applicationContext, AppInstallerService::class.java)
        val pendingIntent = PendingIntent.getService(applicationContext, 0, callbackIntent, 0)
        val packageInstaller = packageManager.packageInstaller
        val params = PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
        params.setAppPackageName(intent?.getStringExtra("pkg"))
        val sessionId = packageInstaller.createSession(params)
        val session = packageInstaller.openSession(sessionId)
        val inputStream: InputStream = FileInputStream(intent?.getStringExtra("path") as String)
        val outputStream = session.openWrite("install", 0, -1)
        val buffer = ByteArray(65536)
        var c: Int
        while (inputStream.read(buffer).also { c = it } != -1) {
            outputStream.write(buffer, 0, c)
        }
        session.fsync(outputStream)
        inputStream.close()
        outputStream.close()
        session.commit(pendingIntent.intentSender)
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}