package com.vanced.manager.utils

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.vanced.manager.core.installer.AppUninstallerService
import java.lang.Exception

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

    fun getPkgVerCode(pkg: String, pm:PackageManager): Int? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            pm.getPackageInfo(pkg, 0)?.longVersionCode?.and(0xFFFFFFFF)?.toInt()
        else
            pm.getPackageInfo(pkg, 0)?.versionCode

    }

    fun uninstallApk(pkg: String, activity: Activity) {
        val callbackIntent = Intent(activity.applicationContext, AppUninstallerService::class.java)
        callbackIntent.putExtra("pkg", pkg)
        val pendingIntent = PendingIntent.getService(activity.applicationContext, 0, callbackIntent, 0)
        activity.packageManager.packageInstaller.uninstall(pkg, pendingIntent.intentSender)
    }

    fun uninstallApk(pkg: String, applicationContext: Context): Boolean {
        val callbackIntent = Intent(applicationContext, AppUninstallerService::class.java)
        callbackIntent.putExtra("pkg", pkg)
        val pendingIntent = PendingIntent.getService(applicationContext, 0, callbackIntent, 0)
        try {
            applicationContext.packageManager.packageInstaller.uninstall(pkg, pendingIntent.intentSender)
            return true
        }
        catch (e: Exception)
        {
            e.printStackTrace()
            return false;
        }
    }
}