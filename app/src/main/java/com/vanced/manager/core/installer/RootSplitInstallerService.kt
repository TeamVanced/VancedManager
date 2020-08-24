package com.vanced.manager.core.installer

import android.app.Service
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.Nullable
import androidx.annotation.WorkerThread
import androidx.core.net.toUri
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.io.SuFile
import com.vanced.manager.BuildConfig
import com.vanced.manager.ui.fragments.HomeFragment
import com.vanced.manager.utils.AppUtils.sendFailure
import com.vanced.manager.utils.FileInfo
import com.vanced.manager.utils.InternetTools.getJsonInt
import com.vanced.manager.utils.PackageHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList


class RootSplitInstallerService: Service() {

    private var hashjson: FileInfo? = null
    private var vancedVersionCode: Int = 0
    val yPkg = "com.google.android.youtube"

    private val localBroadcastManager by lazy { LocalBroadcastManager.getInstance(this) }


    suspend fun getVer()
    {
        vancedVersionCode = getJsonInt("vanced.json","versionCode", application)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Shell.enableVerboseLogging = BuildConfig.DEBUG
        Shell.setDefaultBuilder(
            Shell.Builder.create()
                .setFlags(Shell.FLAG_REDIRECT_STDERR)
                .setTimeout(10)
        )

        runBlocking { getVer() }
        Shell.getShell {
            var job = CoroutineScope(Dispatchers.IO).launch{
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
                        if(fil.name == "hash.json")
                        {
                            hashjson = fil
                        }
                    }
                    if (modApk != null && hashjson != null) {

                        val hash = parseJson(modApk.name.split(".")[0], hashjson!!)
                        if(overwriteBase(modApk, fileInfoList, vancedVersionCode,hash))
                        {
                            with(localBroadcastManager) {
                                sendBroadcast(Intent(HomeFragment.REFRESH_HOME))
                                sendBroadcast(Intent(HomeFragment.VANCED_INSTALLED))
                            }
                        }
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

    private fun parseJson(s: String, hashjson: FileInfo): String
    {
        val jsonData = SuFile.open(hashjson.file!!.absolutePath).readText(Charsets.UTF_8)
        val jsonObject = Parser.default().parse(StringBuilder(jsonData)) as JsonObject
        return jsonObject.string(s)!!
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
        apkFiles.forEach { apkFile ->
            if(apkFile.name != "black.apk" && apkFile.name != "dark.apk" && apkFile.name != "hash.json")
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
    private fun overwriteBase(apkFile: FileInfo,baseApkFiles: ArrayList<FileInfo>, versionCode: Int,hash: String): Boolean
    {
        if(checkVersion(versionCode,baseApkFiles))
        {
            val path = getVPath()
            apkFile.file?.let {
                val apath = it.absolutePath
                if(sha256Check(apath,hash))
                {
                    if(path?.let { it1 -> moveAPK(apath, it1) }!!)
                    {
                        val fpath = SuFile.open(path).parent!!
                        return chConV(path)
                    }
                }
                else
                {
                    sendFailure(listOf("Corrupt_Data").toMutableList(), applicationContext)

                }

            }
        }
        return false
    }
    //do sha256 check on downloaded apk
    private fun sha256Check(apath: String, hash: String): Boolean {
        val sfile = SuFile.open(apath)
        return checkSHA256(hash,sfile)
    }

    //check version and perform action based on result
    private fun checkVersion(versionCode: Int, baseApkFiles: ArrayList<FileInfo>): Boolean {
        val path = getVPath()
        if (path != null) {
            if(path.contains("/data/app/"))
            {
                when(getPkgVerCode(yPkg)?.let { compareVersion(it,versionCode) })
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



    private fun getPkgVerCode(pkg: String): Int? {
        val pm = packageManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            pm.getPackageInfo(pkg, 0)?.longVersionCode?.and(0xFFFFFFFF)?.toInt()
        else
            pm.getPackageInfo(pkg, 0)?.versionCode

    }

    private fun getPkgInfo(pkg: String): PackageInfo?
    {
        return try {
            val m = packageManager
            val info = m.getPackageInfo(pkg, 0)
            info
        } catch (e:Exception) {
            null
        }
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

    //uninstall current update and install base that works with patch
    private fun fixHigherVer(apkFiles: ArrayList<FileInfo>) : Boolean {

        if(PackageHelper.uninstallApk(yPkg, applicationContext))
        {
            return installSplitApkFiles(apkFiles)
        }
        with(localBroadcastManager) {sendFailure(listOf("Failed_Uninstall").toMutableList(), applicationContext)}
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
        val response = Shell.su("chcon -R u:object_r:apk_data_file:s0 $path").exec()
        //val response = Shell.su("chcon -R u:object_r:system_file:s0 $path").exec()
        return if(response.isSuccess) {
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

        if(apkinF.exists())
        {
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

    private fun checkSHA256(sha256: String, updateFile: File?): Boolean {
        try {
            val mInputPFD = contentResolver.openFileDescriptor(updateFile!!.toUri() , "r")
            val mContentFileDescriptor = mInputPFD!!.fileDescriptor
            val fIS = FileInputStream(mContentFileDescriptor)
            val dataBuffer = ByteArrayOutputStream()
            val buf = ByteArray(1024)
            while (true) {
                val readNum = fIS.read(buf)
                if (readNum == -1) break
                dataBuffer.write(buf, 0, readNum)
            }

            // Generate the checksum
            val sum = generateChecksum(dataBuffer)

            return sum == sha256
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    @Throws(IOException::class)
    private fun generateChecksum(data: ByteArrayOutputStream): String {
        try {
            val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
            val hash: ByteArray = digest.digest(data.toByteArray())
            return printableHexString(hash)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }


    private fun printableHexString(data: ByteArray): String {
        // Create Hex String
        val hexString: StringBuilder = StringBuilder()
        for (aMessageDigest:Byte in data) {
            var h: String = Integer.toHexString(0xFF and aMessageDigest.toInt())
            while (h.length < 2)
                h = "0$h"
            hexString.append(h)
        }
        return hexString.toString()
    }


}