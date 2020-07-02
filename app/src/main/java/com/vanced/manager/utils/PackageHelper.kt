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

    fun getPackageVersionName(packageName: String, packageManager: PackageManager): String {
        return if (isPackageInstalled(packageName, packageManager))
            packageManager.getPackageInfo(packageName, 0).versionName
        else
            ""
    }

    fun uninstallApk(pkg: String, activity: Activity) {
        val callbackIntent = Intent(activity.applicationContext, AppUninstallerService::class.java)
        callbackIntent.putExtra("pkg", pkg)
        val pendingIntent = PendingIntent.getService(activity.applicationContext, 0, callbackIntent, 0)
        activity.packageManager.packageInstaller.uninstall(pkg, pendingIntent.intentSender)
    }
}