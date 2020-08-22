package com.vanced.manager.core.installer

import android.app.Service
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.Nullable
import androidx.annotation.WorkerThread
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.topjohnwu.superuser.Shell
import com.vanced.manager.ui.fragments.HomeFragment
import com.vanced.manager.utils.AppUtils.sendFailure
import com.vanced.manager.utils.FileInfo
import com.vanced.manager.utils.InternetTools.getJsonInt
import com.vanced.manager.utils.PackageHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList


class RootSplitInstallerService: Service() {

    private var vancedVersionCode: Int = 0
    val yPkg = "com.google.android.youtube"

    private val localBroadcastManager by lazy { LocalBroadcastManager.getInstance(this) }

    suspend fun getVer()
    {
        vancedVersionCode = getJsonInt("vanced.json","versionCode", application)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        runBlocking { getVer() }

        Shell.getShell {
            CoroutineScope(Dispatchers.IO).launch {
                val apkFilesPath = getExternalFilesDir("apks")?.path
                val fileInfoList = apkFilesPath?.let { it1 -> getFileInfoList(it1) }
                if (fileInfoList != null) {
                    var modApk: FileInfo? = null
                    for (fil in fileInfoList)
                    {
                        if(fil.name == "dark.apk" || fil.name == "black.apk")
                        {
                            modApk = fil
                        }
                    }
                    if (modApk != null) {
                        overwriteBase(modApk, fileInfoList,vancedVersionCode)
                    }
                    else
                    {
                        throw RuntimeException("Missing dark.apk or black.apk")
                    }
                    //installSplitApkFiles(fileInfoList)
                }
            }

        }
        stopSelf()
        return START_NOT_STICKY
    }

    @WorkerThread
    private fun installSplitApkFiles(apkFiles: ArrayList<FileInfo>) {
        var sessionId: Int?
        Log.d("AppLog", "installing split apk files:$apkFiles")
        run {
            val sessionIdResult = Shell.su("pm install-create -r -t").exec().out
            val sessionIdPattern = Pattern.compile("(\\d+)")
            val sessionIdMatcher = sessionIdPattern.matcher(sessionIdResult[0])
            sessionIdMatcher.find()
            sessionId = Integer.parseInt(sessionIdMatcher.group(1)!!)
        }
        apkFiles.forEach { apkFile ->
            if(apkFile.name != "black.apk" && apkFile.name != "dark.apk")
            {
                Log.d("AppLog", "installing APK : ${apkFile.name} ${apkFile.fileSize} ")
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
            }
        }
        Log.d("AppLog", "committing...")
        val installResult = Shell.su("pm install-commit $sessionId").exec()
        if (installResult.isSuccess) {
            with(localBroadcastManager) {
                sendBroadcast(Intent(HomeFragment.REFRESH_HOME))
                sendBroadcast(Intent(HomeFragment.VANCED_INSTALLED))
            }
        } else
            sendFailure(installResult.out, this)
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



    private fun overwriteBase(apkFile: FileInfo, baseApkFiles: ArrayList<FileInfo>, versionCode: Int)
    {
        checkVersion(versionCode,baseApkFiles)
        var path = getVPath()
        apkFile.file?.absolutePath?.let {
            moveAPK(it, path)
            chConV(path)
        }


    }

    private fun checkVersion(versionCode: Int, baseApkFiles: ArrayList<FileInfo>) {
        val path = getVPath()
        if(path.contains("/data/app/"))
        {
            when(compareVersion(getPkgVerCode(yPkg),versionCode))
            {
                1 -> fixHigherVer(baseApkFiles)
                -1 -> fixLowerVer(baseApkFiles)
            }
        }
        else
        {
            fixNoInstall(baseApkFiles)
        }
    }



    private fun getPkgVerCode(pkg: String): Int {
        val pm = packageManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            pm.getPackageInfo(pkg, 0).longVersionCode.and(0xFFFFFFFF).toInt()
        else
            pm.getPackageInfo(pkg, 0).versionCode

    }

    private fun getPkgInfo(pkg: String): PackageInfo
    {
        val m = packageManager
        val p: PackageInfo = m.getPackageInfo(pkg, 0)
        return p
    }

    private fun compareVersion(pkgVerCode: Int, versionCode: Int): Int
    {
        return if(pkgVerCode > versionCode)
            1
        else if (pkgVerCode < versionCode)
            -1
        else
            0
    }

    private fun fixHigherVer(apkFiles: ArrayList<FileInfo>) {

        PackageHelper.uninstallApk(yPkg, applicationContext)
        installSplitApkFiles(apkFiles)
    }

    private fun fixLowerVer(apkFiles: ArrayList<FileInfo>) {
        installSplitApkFiles(apkFiles)
    }

    private fun fixNoInstall(baseApkFiles: ArrayList<FileInfo>) {
        installSplitApkFiles(baseApkFiles)
    }

    private fun chConV(path: String) {
        Shell.su("chcon -R u:object_r:system_file:s0 $path").exec()
    }

    private fun moveAPK(apkFile: String, path: String) {
        Shell.su("cp $apkFile $path").exec()
        Shell.su("chmod 644 $path").exec()

    }

    private fun getVPath(): String {
        val p = getPkgInfo(yPkg)
        return p.applicationInfo.sourceDir
    }

}