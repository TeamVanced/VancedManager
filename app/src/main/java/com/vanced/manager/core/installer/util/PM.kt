package com.vanced.manager.core.installer.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.content.pm.PackageManager
import android.os.Build
import com.vanced.manager.core.installer.service.AppInstallService
import java.io.File
import java.io.FileInputStream

private const val byteArraySize = 1024 * 1024 // Because 1,048,576 is not readable

fun installApp(apk: File, context: Context) {
    val packageInstaller = context.packageManager.packageInstaller
    val session =
        packageInstaller.openSession(packageInstaller.createSession(sessionParams))
    writeApkToSession(apk, session)
    session.commit(getIntentSender(context))
    session.close()
}

fun installSplitApp(apks: Array<File>, context: Context) {
    val packageInstaller = context.packageManager.packageInstaller
    val session =
        packageInstaller.openSession(packageInstaller.createSession(sessionParams))
    for (apk in apks) {
        writeApkToSession(apk, session)
    }
    session.commit(getIntentSender(context))
    session.close()
}

private val intentFlags
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        PendingIntent.FLAG_MUTABLE
    else
        0

private val sessionParams
    get() = PackageInstaller.SessionParams(
        PackageInstaller.SessionParams.MODE_FULL_INSTALL
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setInstallReason(PackageManager.INSTALL_REASON_USER)
        }
    }

private fun getIntentSender(context: Context) =
    PendingIntent.getService(
        context,
        0,
        Intent(context, AppInstallService::class.java),
        intentFlags
    ).intentSender

private fun writeApkToSession(
    apk: File,
    session: PackageInstaller.Session
) {
    val inputStream = FileInputStream(apk)
    val outputStream = session.openWrite(apk.name, 0, apk.length())
    val buffer = ByteArray(byteArraySize)
    var length: Int
    while (inputStream.read(buffer).also { length = it } > 0) {
        outputStream.write(buffer, 0, length)
    }
    session.fsync(outputStream)
    inputStream.close()
    outputStream.flush()
    outputStream.close()
}