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
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.io.SuFile
import com.vanced.manager.BuildConfig
import com.vanced.manager.ui.fragments.HomeFragment
import com.vanced.manager.utils.AppUtils.sendFailure
import com.vanced.manager.utils.FileInfo
import com.vanced.manager.utils.InternetTools.getJsonInt
import com.vanced.manager.utils.PackageHelper
import com.vanced.manager.utils.PackageHelper.getPkgVerCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

class RootSplitInstallerService: Service() {

    private val vancedVersionCode by lazy{ runBlocking { getJsonInt("vanced.json","versionCode", applicationContext) }}
    private val yPkg = "com.google.android.youtube"
    private val apkInstallPath = "/data/adb/Vanced/"
    private val localBroadcastManager by lazy { LocalBroadcastManager.getInstance(this) }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Shell.enableVerboseLogging = BuildConfig.DEBUG
        Shell.setDefaultBuilder(
            Shell.Builder.create()
                .setFlags(Shell.FLAG_REDIRECT_STDERR)
                .setTimeout(10)
        )

        Shell.getShell {
            CoroutineScope(Dispatchers.IO).launch {
                val apkFilesPath = getExternalFilesDir("apks")?.path
                val fileInfoList = apkFilesPath?.let { it1 -> getFileInfoList(it1) }
                if (fileInfoList != null) {
                    var modApk: FileInfo? = null
                    for (file in fileInfoList) {
                        if (file.name == "dark.apk" || file.name == "black.apk") {
                            modApk = file
                        }
                    }
                    if (modApk != null) {
                        if (getDefaultSharedPreferences(this@RootSplitInstallerService).getBoolean("new_installer", false)) {
                            if (overwriteBase(modApk, fileInfoList, vancedVersionCode)) {
                                with(localBroadcastManager) {
                                    sendBroadcast(Intent(HomeFragment.REFRESH_HOME))
                                    sendBroadcast(Intent(HomeFragment.VANCED_INSTALLED))
                                }
                            }
                        } else
                            installSplitApkFiles(fileInfoList)

                    }
                    else
                    {
                        sendFailure(listOf("ModApk_Missing").toMutableList(), applicationContext)
                    }
                    //installSplitApkFiles(fileInfoList)
                }
                else
                {
                    sendFailure(listOf("Files_Missing_VA").toMutableList(), applicationContext)
                }
            }

        }
        stopSelf()
        return START_NOT_STICKY
    }

    @WorkerThread
    private fun installSplitApkFiles(apkFiles: ArrayList<FileInfo>) : Boolean {
        var sessionId: Int?
        Log.d("AppLog", "installing split apk files:$apkFiles")
        run {
            val sessionIdResult = Shell.su("pm install-create -r -t").exec().out
            val sessionIdPattern = Pattern.compile("(\\d+)")
            val sessionIdMatcher = sessionIdPattern.matcher(sessionIdResult[0])
            sessionIdMatcher.find()
            sessionId = Integer.parseInt(sessionIdMatcher.group(1)!!)
        }
        var useNew = (getDefaultSharedPreferences(this@RootSplitInstallerService).getBoolean("new_installer", false))
        apkFiles.forEach { apkFile ->
            if(((apkFile.name != "black.apk" && apkFile.name != "dark.apk") || !useNew) && apkFile.name != "hash.json")
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
            return true
        } else
            sendFailure(installResult.out, this)
        return false
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

    //install Vanced
    private fun overwriteBase(apkFile: FileInfo,baseApkFiles: ArrayList<FileInfo>, versionCode: Int): Boolean {
        if (checkVersion(versionCode,baseApkFiles)) {
            val path = getPackageDir()
            apkFile.file?.let {
                val apath = it.absolutePath

                setupFolder(apkInstallPath)
                if(path != null)
                {
                    val apkFPath = apkInstallPath + "base.apk"
                    if(moveAPK(apath, apkFPath))
                    {
                        if(chConV(apkFPath))
                        {
                            if(setupScript(apkFPath,path))
                            {
                                return linkVanced(apkFPath,path)
                            }

                        }
                    }

                }

            }
        }
        return false
    }

    private fun setupScript(apkFPath: String, path: String): Boolean
    {
        if(Shell.su(""" echo  "#!/system/bin/sh\nmount -o bind $apkFPath $path" > /data/adb/service.d/vanced.sh """).exec().isSuccess)
        {
            return Shell.su("chmod 744 /data/adb/service.d/vanced.sh").exec().isSuccess
        }
        return false
    }

    private fun linkVanced(apkFPath: String, path: String): Boolean
    {
        Shell.su("am force-stop $yPkg").exec()
        val umountv = Shell.su("""for i in ${'$'}(ls /data/app/ | grep com.google.android.youtube | tr " "); do umount -l "/data/app/${"$"}i/base.apk"; done """)
        val response = Shell.su("""su -mm -c "mount -o bind $apkFPath $path"""").exec()
        Thread.sleep(500)

        return response.isSuccess
    }

    private fun setupFolder(apkInstallPath: String): Boolean {
        return Shell.su("mkdir -p $apkInstallPath").exec().isSuccess
    }

    //check version and perform action based on result
    private fun checkVersion(versionCode: Int, baseApkFiles: ArrayList<FileInfo>): Boolean {
        val path = getPackageDir()
        if (path != null) {
            if(path.contains("/data/app/"))
            {
                when(getVersionNumber()?.let { compareVersion(it,versionCode) })
                {
                    1 -> {return fixHigherVer(baseApkFiles) }
                    -1 -> {return fixLowerVer(baseApkFiles) }
                }
                return true
            }
            else
            {
                return fixNoInstall(baseApkFiles)
            }
        }
        return fixNoInstall(baseApkFiles)
    }

    private fun getPkgInfo(pkg: String): PackageInfo? {
        return try {
            packageManager.getPackageInfo(pkg, 0)
        } catch (e:Exception) {
            Log.d("VMpm", "Unable to get package info")
            null
        }
    }

    private fun compareVersion(pkgVerCode: Int, versionCode: Int): Int {
        return when {
            pkgVerCode > versionCode -> 1
            pkgVerCode < versionCode -> -1
            else -> 0
        }
    }

    //uninstall current update and install base that works with patch
    private fun fixHigherVer(apkFiles: ArrayList<FileInfo>) : Boolean {
        if(PackageHelper.uninstallApk(yPkg, applicationContext)) {
            return installSplitApkFiles(apkFiles)
        }
        sendFailure(listOf("Failed_Uninstall").toMutableList(), this)
        return false
    }

    //install newer stock youtube
    private fun fixLowerVer(apkFiles: ArrayList<FileInfo>): Boolean {
        return installSplitApkFiles(apkFiles)
    }

    //install stock youtube since no install was found
    private fun fixNoInstall(baseApkFiles: ArrayList<FileInfo>): Boolean {
        return installSplitApkFiles(baseApkFiles)
    }

    //set chcon to apk_data_file
    private fun chConV(path: String): Boolean {
        val response = Shell.su("chcon u:object_r:apk_data_file:s0 $path").exec()
        //val response = Shell.su("chcon -R u:object_r:system_file:s0 $path").exec()
        return if (response.isSuccess) {
            true
        } else {
            sendFailure(response.out, applicationContext)
            false
        }
    }

    //move patch to data/app
    private fun moveAPK(apkFile: String, path: String) : Boolean {

        val apkinF = SuFile.open(apkFile)
        val apkoutF = SuFile.open(path)

        if(apkinF.exists()) {
            try {
                Shell.su("am force-stop $yPkg").exec()

                //Shell.su("rm -r SuFile.open(path).parent")

                copy(apkinF,apkoutF)
                Shell.su("chmod 644 $path").exec().isSuccess
                return if(Shell.su("chown system:system $path").exec().isSuccess) {
                    true
                } else {
                    sendFailure(listOf("Chown_Fail").toMutableList(), applicationContext)
                    false
                }

            }
            catch (e: IOException)
            {
                sendFailure(listOf("${e.message}").toMutableList(), applicationContext)
                return false
            }
        }
        else {
            sendFailure(listOf("IFile_Missing").toMutableList(), applicationContext)
            return false
        }
    }


    @Throws(IOException::class)
    fun copy(src: File?, dst: File?) {
        val cmd = Shell.su("mv ${src!!.absolutePath} ${dst!!.absolutePath}").exec().isSuccess
        Log.d("ZLog", cmd.toString())
    }

    //get path of the installed youtube
    private fun getVPath(): String? {
        return try {
            val p = getPkgInfo(yPkg)
            p?.applicationInfo?.sourceDir
        } catch (e: Exception) {
            null
        }

    }

    private fun getVersionNumber(): Int?
    {
        try {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                packageManager.getPackageInfo(yPkg, 0)?.longVersionCode?.and(0xFFFFFFFF)?.toInt()
            else
                packageManager.getPackageInfo(yPkg, 0)?.versionCode
        }
        catch (e : Exception)
        {
            val execRes = Shell.su("dumpsys package com.google.android.youtube | grep versionCode").exec()
            if(execRes.isSuccess)
            {
                val result = execRes.out
                var version: Int = 0
                for(line in result)
                {
                    val versionCode = line.substringAfter("=")
                    val versionCodeFiltered = versionCode.substringBefore(" ")
                    if(version < Integer.valueOf(versionCodeFiltered))
                    {
                        version = Integer.valueOf(versionCodeFiltered)
                    }
                }
                return version
            }
        }
        return null
    }

    private fun getPackageDir(): String?
    {
        return try {
            val p = getPkgInfo(yPkg)
            p?.applicationInfo?.sourceDir
        } catch (e: Exception) {
             val execRes = Shell.su("dumpsys package com.google.android.youtube | grep codePath").exec()
            if(execRes.isSuccess)
            {
                val result = execRes.out
                for (line in result)
                {
                    if(line.contains("data/app")) "${line.substringAfter("=")}/base.apk"
                }
            }
            null
        }
    }

}


