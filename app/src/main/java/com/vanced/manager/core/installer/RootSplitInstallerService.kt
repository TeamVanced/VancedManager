package com.vanced.manager.core.installer

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.Nullable
import androidx.annotation.WorkerThread
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.topjohnwu.superuser.Shell
import com.vanced.manager.R
import com.vanced.manager.ui.MainActivity
import com.vanced.manager.utils.FileInfo
import com.vanced.manager.utils.NotificationHelper.createBasicNotif
import java.io.File
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

class RootSplitInstallerService: Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Shell.getShell {
            val isRoot = it.isRoot
            Log.d("AppLog", "isRoot ?$isRoot ")
            AsyncTask.execute {
                val apkFilesPath = cacheDir.path
                val fileInfoList = getFileInfoList(apkFilesPath)
                installSplitApkFiles(fileInfoList)
            }
        }
        stopSelf()
        return START_NOT_STICKY
    }

    @WorkerThread
    private fun installSplitApkFiles(apkFiles: ArrayList<FileInfo>) {
        var sessionId: Int?
        val notifId = 666
        Log.d("AppLog", "installing split apk files:$apkFiles")
        run {
            val sessionIdResult = Shell.su("pm install-create -r -t").exec().out
            val sessionIdPattern = Pattern.compile("(\\d+)")
            val sessionIdMatcher = sessionIdPattern.matcher(sessionIdResult[0])
            sessionIdMatcher.find()
            sessionId = Integer.parseInt(sessionIdMatcher.group(1)!!)
        }
        for (apkFile in apkFiles) {
            Log.d("AppLog", "installing APK : ${apkFile.name} ${apkFile.fileSize} ")
            createBasicNotif(getString(R.string.installing_app, "Vanced"), notifId, this)
            val command = arrayOf("su", "-c", "pm", "install-write", "-S", "${apkFile.fileSize}", "$sessionId", apkFile.name)
            val process: Process = Runtime.getRuntime().exec(command)
            val inputPipe = apkFile.getInputStream()
            try {
                process.outputStream.use { outputStream -> inputPipe.copyTo(outputStream) }
            } catch (e: Exception) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    process.destroyForcibly()
                else
                    process.destroy()

                throw RuntimeException(e)
            }
            process.waitFor()
            val inputStr = process.inputStream.readBytes().toString(Charset.defaultCharset())
            val errStr = process.errorStream.readBytes().toString(Charset.defaultCharset())
            val isSucceeded = process.exitValue() == 0
            Log.d("AppLog", "isSucceeded?$isSucceeded inputStr:$inputStr errStr:$errStr")
        }
        Log.d("AppLog", "committing...")
        val installResult = Shell.su("pm install-commit $sessionId").exec()
        Log.d("AppLog", "succeeded installing?${installResult.isSuccess}")
        getSharedPreferences("installPrefs", Context.MODE_PRIVATE).edit().putBoolean("isInstalling", false).apply()
        if (installResult.isSuccess) {
            val mIntent = Intent(MainActivity.INSTALL_COMPLETED)
            mIntent.action = MainActivity.INSTALL_COMPLETED
            mIntent.putExtra("package", "split")
            LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent)
            createBasicNotif(getString(R.string.successfully_installed, "Vanced"), notifId, this)
        } else {
            val mIntent = Intent(MainActivity.INSTALL_FAILED)
            mIntent.action = MainActivity.INSTALL_FAILED
            mIntent.putExtra("errorMsg", getString(R.string.installation_signature))
            LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent)
            createBasicNotif(getString(R.string.installation_signature), notifId, this)
        }
    }

    private fun SimpleDateFormat.tryParse(str: String) = try {
        parse(str) != null
    } catch (e: Exception) {
        false
    }

    @WorkerThread
    private fun getFileInfoList(splitApkPath: String): ArrayList<FileInfo> {
        val parentFile = File(splitApkPath)
        val result = ArrayList<FileInfo>()

        if (parentFile.exists() && parentFile.canRead()) {
            val listFiles = parentFile.listFiles() ?: return ArrayList()
            for (file in listFiles)
                result.add(FileInfo(file.name, file.length(), file))
            return result
        }
        val longLines = Shell.su("ls -l $splitApkPath").exec().out
        val pattern = Pattern.compile(" +")
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        longLinesLoop@ for (line in longLines) {
            val matcher = pattern.matcher(line)
            for (i in 0 until 4)
                if (!matcher.find())
                    continue@longLinesLoop
            val startSizeStr = matcher.end()
            matcher.find()
            val endSizeStr = matcher.start()
            val fileSizeStr = line.substring(startSizeStr, endSizeStr)
            while (true) {
                val testTimeStr: String =
                    line.substring(matcher.end(), line.indexOf(' ', matcher.end()))
                if (formatter.tryParse(testTimeStr)) {
                    //found time, so apk is next
                    val fileName = line.substring(line.indexOf(' ', matcher.end()) + 1)
                    if (fileName.endsWith("apk"))
                        result.add(FileInfo(fileName, fileSizeStr.toLong(), File(splitApkPath, fileName)))
                    break
                }
                matcher.find()
            }
        }
        return result
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}