package com.vanced.manager.utils

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import com.vanced.manager.core.installer.AppUninstallerService

object PackageHelper {

    fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    /*
    @Throws(IOException::class)
    fun installApp(
        context: Context,
        path: String,
        pkg: String?
    ) {
        val callbackIntent = Intent(context.applicationContext, AppInstallerService::class.java)
        val pendingIntent = PendingIntent.getService(context.applicationContext, 0, callbackIntent, 0)
        val packageInstaller = context.packageManager.packageInstaller
        val params = SessionParams(SessionParams.MODE_FULL_INSTALL)
        params.setAppPackageName(pkg)
        val sessionId = packageInstaller.createSession(params)
        val session = packageInstaller.openSession(sessionId)
        val inputStream: InputStream = FileInputStream(path)
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
    }
     */

    fun uninstallApk(pkg: String, activity: Activity) {
        val callbackIntent = Intent(activity.applicationContext, AppUninstallerService::class.java)
        callbackIntent.putExtra("pkg", pkg)
        val pendingIntent = PendingIntent.getService(activity.applicationContext, 0, callbackIntent, 0)
        activity.packageManager.packageInstaller.uninstall(pkg, pendingIntent.intentSender)
    }
}