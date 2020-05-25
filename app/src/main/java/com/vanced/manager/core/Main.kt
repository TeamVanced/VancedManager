package com.vanced.manager.core

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager
import com.dezlum.codelabs.getjson.GetJson
import com.vanced.manager.R
import com.vanced.manager.core.base.BaseActivity
import com.vanced.manager.core.installer.SplitInstallerService
import zlc.season.rxdownload4.file
import java.io.*

// This activity will NOT be used in manifest
// since MainActivity will extend it
@SuppressLint("Registered")
open class Main: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val firstStart = prefs.getBoolean("firstStart", true)
        val isUpgrading = prefs.getBoolean("isUpgrading", false)

        //Easter Egg
        val falseStatement = prefs.getBoolean("statement", true)

        when {
            firstStart -> showSecurityDialog()
            !falseStatement -> statementFalse()
            isUpgrading -> {
                val apkUrl = GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/manager.json")
                val dwnldUrl = apkUrl.get("url").asString

                if (dwnldUrl.file().exists())
                    dwnldUrl.file().delete()

                prefs.edit().putBoolean("isUpgrading", false).apply()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            cacheDir.deleteRecursively()
        } catch (e: Exception) {
            Log.d("VMCache", "Unable to delete cacheDir")
        }
    }

    private fun showSecurityDialog() {
        AlertDialog.Builder(this)
            .setTitle(resources.getString(R.string.welcome))
            .setMessage(resources.getString(R.string.security_context))
            .setPositiveButton(resources.getString(R.string.close)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.edit().putBoolean("firstStart", false).apply()
    }

    //Easter Egg
    private fun statementFalse() {
        AlertDialog.Builder(this)
            .setTitle("Wait what?")
            .setMessage("So this statement is false huh? I'll go with True!")
            .setPositiveButton("wut?") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.edit().putBoolean("statement", true).apply()
    }

    fun installSplitApk(): Int {
        val apkFolderPath = cacheDir.path
        val nameSizeMap = HashMap<String, Long>()
        var totalSize: Long = 0
        var sessionId = 0
        val folder = File(apkFolderPath)
        val listOfFiles = folder.listFiles()
        try {
            for (listOfFile in listOfFiles!!) {
                if (listOfFile.isFile) {
                    Log.d("AppLog", "installApk: " + listOfFile.name)
                    nameSizeMap[listOfFile.name] = listOfFile.length()
                    totalSize += listOfFile.length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        }
        val installParams = PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
        installParams.setSize(totalSize)
        try {
            sessionId = packageManager.packageInstaller.createSession(installParams)
            Log.d("AppLog","Success: created install session [$sessionId]")
            for ((key, value) in nameSizeMap) {
                doWriteSession(sessionId, apkFolderPath + key, value, key)
            }
            doCommitSession(sessionId)
            Log.d("AppLog","Success")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return sessionId
    }

    private fun doWriteSession(sessionId: Int, inPath: String?, sizeBytes: Long, splitName: String): Int {
        var inPathToUse = inPath
        var sizeBytesToUse = sizeBytes
        if ("-" == inPathToUse) {
            inPathToUse = null
        } else if (inPathToUse != null) {
            val file = File(inPathToUse)
            if (file.isFile)
                sizeBytesToUse = file.length()
        }
        var session: PackageInstaller.Session? = null
        var inputStream: InputStream? = null
        var out: OutputStream? = null
        try {
            session = packageManager.packageInstaller.openSession(sessionId)
            if (inPathToUse != null) {
                inputStream = FileInputStream(inPathToUse)
            }
            out = session.openWrite(splitName, 0, sizeBytesToUse)
            var total = 0
            val buffer = ByteArray(65536)
            var c: Int
            while (true) {
                c = inputStream!!.read(buffer)
                if (c == -1)
                    break
                total += c
                out.write(buffer, 0, c)
            }
            session.fsync(out)
            Log.d("AppLog", "Success: streamed $total bytes")
            return PackageInstaller.STATUS_SUCCESS
        } catch (e: IOException) {
            Log.e("AppLog", "Error: failed to write; " + e.message)
            return PackageInstaller.STATUS_FAILURE
        } finally {
            try {
                out?.close()
                inputStream?.close()
                session?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun doCommitSession(sessionId: Int) {
        var session: PackageInstaller.Session? = null
        try {
            try {
                session = packageManager.packageInstaller.openSession(sessionId)
                val callbackIntent = Intent(applicationContext, SplitInstallerService::class.java)
                val pendingIntent = PendingIntent.getService(applicationContext, 0, callbackIntent, 0)
                session.commit(pendingIntent.intentSender)
                session.close()
                Log.d("AppLog", "install request sent")
                Log.d("AppLog", "doCommitSession: " + packageManager.packageInstaller.mySessions)
                Log.d("AppLog", "doCommitSession: after session commit ")
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } finally {
            session!!.close()
        }
    }

}