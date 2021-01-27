package com.vanced.manager.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageInstaller
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.io.SuFile
import com.vanced.manager.BuildConfig
import com.vanced.manager.core.installer.AppInstallerService
import com.vanced.manager.core.installer.AppUninstallerService
import com.vanced.manager.utils.AppUtils.musicRootPkg
import com.vanced.manager.utils.AppUtils.playStorePkg
import com.vanced.manager.utils.AppUtils.sendCloseDialog
import com.vanced.manager.utils.AppUtils.sendFailure
import com.vanced.manager.utils.AppUtils.sendRefresh
import com.vanced.manager.utils.AppUtils.vancedRootPkg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object PackageHelper {

    const val apkInstallPath = "/data/adb"
    private const val INSTALLER_TAG = "VMInstall"
    private val vancedThemes = vanced.value?.array<String>("themes")?.value ?: listOf("black", "dark", "pink", "blue")

    init {
        Shell.enableVerboseLogging = BuildConfig.DEBUG
        Shell.setDefaultBuilder(
            Shell.Builder.create()
                .setFlags(Shell.FLAG_REDIRECT_STDERR)
                .setTimeout(10)
        )
    }

    private fun getAppNameRoot(pkg: String): String {
        return when (pkg) {
            vancedRootPkg -> "vanced"
            musicRootPkg -> "music"
            else -> ""
        }
    }

    fun scriptExists(scriptName: String): Boolean {
        val serviceDScript = SuFile.open("$apkInstallPath/service.d/$scriptName.sh")
        val postFsDataScript = SuFile.open("$apkInstallPath/post-fs-data.d/$scriptName.sh")
        if (serviceDScript.exists() && postFsDataScript.exists()) {
            return true
        }
        return false
    }


    fun getPkgNameRoot(app: String): String {
        return when (app) {
            "vanced" -> vancedRootPkg
            "music" -> musicRootPkg
            else -> ""
        }
    }
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

    @Suppress("DEPRECATION")
    fun getPkgVerCode(pkg: String, pm:PackageManager): Int? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            pm.getPackageInfo(pkg, 0)?.longVersionCode?.and(0xFFFFFFFF)?.toInt()
        else
            pm.getPackageInfo(pkg, 0)?.versionCode

    }

    fun downloadStockCheck(pkg: String, versionCode: Int, context: Context): Boolean {
        return try {
            getPkgVerCode(pkg, context.packageManager) != versionCode
        } catch (e: Exception) {
            true
        }
    }

    fun apkExist(context: Context, apk: String): Boolean {
        val apkPath = File(context.getExternalFilesDir(apk.substring(0, apk.length - 4))?.path, apk)
        if (apkPath.exists())
            return true

        return false
    }

    fun musicApkExists(context: Context): Boolean {
        val apkPath = File(context.getExternalFilesDir("music/nonroot")?.path, "nonroot.apk")
        if (apkPath.exists()) {
            return true
        }

        return false
    }

    fun vancedInstallFilesExist(context: Context): Boolean {
        val apksPath = File(context.getExternalFilesDir("vanced/nonroot")?.path.toString())
        val splitFiles = mutableListOf<String>()
        if (apksPath.exists()) {
            val files = apksPath.listFiles()
            if (files?.isNotEmpty() == true) {
                for (file in files) {
                    when {
                        vancedThemes.any { file.name == "$it.apk" } && !splitFiles.contains("base") -> splitFiles.add("base")
                        file.name.matches(Regex("split_config\\.(..)\\.apk")) && !splitFiles.contains("lang") -> splitFiles.add("lang")
                        (file.name.startsWith("split_config.arm") || file.name.startsWith("split_config.x86")) && !splitFiles.contains("arch") -> splitFiles.add("arch")
                    }

                    if (splitFiles.size == 3) {
                        return true
                    }
                }
            }
            return false
        }
        return false
    }

    fun uninstallRootApk(pkg: String): Boolean {
        val app = getAppNameRoot(pkg)
        Shell.su("rm -rf $apkInstallPath/${app.capitalize(Locale.ROOT)}").exec()
        Shell.su("rm $apkInstallPath/post-fs-data.d/$app.sh").exec()
        Shell.su("rm $apkInstallPath/service.d/$app.sh").exec()
        return Shell.su("pm uninstall $pkg").exec().isSuccess
    }

    fun uninstallApk(pkg: String, context: Context) {
        val callbackIntent = Intent(context, AppUninstallerService::class.java)
        callbackIntent.putExtra("pkg", pkg)
        val pendingIntent = PendingIntent.getService(context, 0, callbackIntent, 0)
        try {
            context.packageManager.packageInstaller.uninstall(pkg, pendingIntent.intentSender)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun install(path: String, context: Context) {
        try {
            val callbackIntent = Intent(context, AppInstallerService::class.java)
            val pendingIntent = PendingIntent.getService(context, 0, callbackIntent, 0)
            val packageInstaller = context.packageManager.packageInstaller
            val params = PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
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
        } catch (e: IOException) {
            Log.d(INSTALLER_TAG, e.stackTraceToString())
        }

    }

    private fun installRootMusic(files: ArrayList<FileInfo>, context: Context): Boolean {
        files.forEach { apk ->
            if (apk.name != "root.apk") {
                val newPath = "/data/local/tmp/${apk.file?.name}"

                //moving apk to tmp folder in order to avoid permission denials
                Shell.su("mv ${apk.file?.path} $newPath").exec()
                val command = Shell.su("pm install $newPath").exec()
                Shell.su("rm $newPath").exec()
                if (command.isSuccess) {
                    return true
                } else {
                    sendFailure(command.out, context)
                    sendCloseDialog(context)
                }

            }
        }
        return false
    }

    private fun installRootApp(context: Context, app: String, appVerCode: Int, pkg: String, modApkBool: (fileName: String) -> Boolean) = CoroutineScope(Dispatchers.IO).launch {
        Shell.getShell {
            val apkFilesPath = context.getExternalFilesDir("$app/root")?.path
            val fileInfoList = apkFilesPath?.let { it1 -> getFileInfoList(it1) }
            if (fileInfoList != null) {
                val modApk: FileInfo? = fileInfoList.lastOrNull { modApkBool(it.name) }
                if (modApk != null) {
                    if (overwriteBase(modApk, fileInfoList, appVerCode, pkg, app, context)) {
                        setInstallerPackage(context, pkg, playStorePkg)
                        Log.d(INSTALLER_TAG, "Finished installation")
                        sendRefresh(context)
                        sendCloseDialog(context)
                    }
                } else {
                    sendFailure(listOf("ModApk_Missing").toMutableList(), context)
                    sendCloseDialog(context)
                }
            } else {
                sendFailure(listOf("Files_Missing_VA").toMutableList(), context)
                sendCloseDialog(context)
            }

        }

    }

    fun installMusicRoot(context: Context) {
        installRootApp(
            context,
            "music",
            music.value?.int("versionCode")!!,
            musicRootPkg
        ) {
            it == "root.apk"
        }
    }

    fun installVancedRoot(context: Context) {
        installRootApp(
            context,
            "vanced",
            vanced.value?.int("versionCode")!!,
            vancedRootPkg
        ) { fileName ->
            vancedThemes.any { fileName == "$it.apk" }
        }
    }

    fun installVanced(context: Context): Int {
        val apkFolderPath = context.getExternalFilesDir("vanced/nonroot")?.path.toString() + "/"
        val nameSizeMap = HashMap<String, Long>()
        var totalSize: Long = 0
        var sessionId = 0
        val folder = File(apkFolderPath)
        val listOfFiles = folder.listFiles()
        try {
            for (listOfFile in listOfFiles!!) {
                if (listOfFile.isFile) {
                    Log.d(INSTALLER_TAG, "installApk: " + listOfFile.name)
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
            sessionId = context.packageManager.packageInstaller.createSession(installParams)
            Log.d(INSTALLER_TAG,"Success: created install session [$sessionId]")
            for ((key, value) in nameSizeMap) {
                doWriteSession(sessionId, apkFolderPath + key, value, key, context)
            }
            doCommitSession(sessionId, context)
            Log.d(INSTALLER_TAG,"Success")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return sessionId
    }

    private fun doWriteSession(sessionId: Int, inPath: String?, sizeBytes: Long, splitName: String, context: Context): Int {
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
            session = context.packageManager.packageInstaller.openSession(sessionId)
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
            Log.d(INSTALLER_TAG, "Success: streamed $total bytes")
            return PackageInstaller.STATUS_SUCCESS
        } catch (e: IOException) {
            Log.e(INSTALLER_TAG, "Error: failed to write; " + e.message)
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

    private fun doCommitSession(sessionId: Int, context: Context) {
        var session: PackageInstaller.Session? = null
        try {
            session = context.packageManager.packageInstaller.openSession(sessionId)
            val callbackIntent = Intent(context, AppInstallerService::class.java)
            val pendingIntent = PendingIntent.getService(context, 0, callbackIntent, 0)
            session.commit(pendingIntent.intentSender)
            session.close()
            Log.d(INSTALLER_TAG, "install request sent")
            Log.d(INSTALLER_TAG, "doCommitSession: " + context.packageManager.packageInstaller.mySessions)
            Log.d(INSTALLER_TAG, "doCommitSession: after session commit ")
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            session?.close()
        }
    }

    private fun installSplitApkFiles(apkFiles: ArrayList<FileInfo>, context: Context) : Boolean {
        var sessionId: Int?
        val filenames = arrayOf("black.apk", "dark.apk", "blue.apk", "pink.apk", "hash.json")
        Log.d(INSTALLER_TAG, "installing split apk files: $apkFiles")
        run {
            val sessionIdResult = Shell.su("pm install-create -r -t").exec().out
            val sessionIdPattern = Pattern.compile("(\\d+)")
            val sessionIdMatcher = sessionIdPattern.matcher(sessionIdResult[0])
            sessionIdMatcher.find()
            sessionId = Integer.parseInt(sessionIdMatcher.group(1)!!)
        }
        apkFiles.forEach { apkFile ->
            if (!filenames.any { apkFile.name == it }) {
                Log.d(INSTALLER_TAG, "installing APK: ${apkFile.name} ${apkFile.fileSize}")
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

                    sendFailure(e.stackTrace.map { it.toString() }.toMutableList(), context)
                    sendCloseDialog(context)
                }
                process.waitFor()
            }
        }
        Log.d(INSTALLER_TAG, "committing...")
        val installResult = Shell.su("pm install-commit $sessionId").exec()
        if (installResult.isSuccess) {
            return true
        }
        sendFailure(installResult.out, context)
        sendCloseDialog(context)
        return false
    }

    private fun SimpleDateFormat.tryParse(str: String) = try {
        parse(str) != null
    } catch (e: Exception) {
        false
    }

    private fun getFileInfoList(splitApkPath: String): ArrayList<FileInfo> {
        val parentFile = File(splitApkPath)
        val result = ArrayList<FileInfo>()

        if (parentFile.exists() && parentFile.canRead()) {
            val listFiles = parentFile.listFiles() ?: return ArrayList()
            listFiles.mapTo(result) {
                FileInfo(it.name, it.length(), it)
            }
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

    //overwrite stock Vanced/Music
    private fun overwriteBase(
        apkFile: FileInfo,
        baseApkFiles: ArrayList<FileInfo>,
        versionCode: Int,
        pkg: String,
        app: String,
        context: Context
    ): Boolean {
        if (checkVersion(versionCode, baseApkFiles, pkg, context)) {
            val path = getPackageDir(context, pkg)
            apkFile.file?.let {
                val apath = it.absolutePath

                setupFolder("$apkInstallPath/${app.capitalize(Locale.ROOT)}")
                if (path != null) {
                    val apkFPath = "$apkInstallPath/${app.capitalize(Locale.ROOT)}/base.apk"
                    if (moveAPK(apath, apkFPath, pkg, context)) {
                        if (chConV(apkFPath, context)) {
                            if (setupScript(apkFPath, path, app, pkg, context)) {
                                return linkApp(apkFPath, pkg, path)
                            }
                        }
                    }
                }
            }
        }
        return false
    }

    private fun setupScript(apkFPath: String, path: String, app: String, pkg: String, context: Context): Boolean
    {
        try {
            Log.d(INSTALLER_TAG, "Setting up script")
            context.writeServiceDScript(apkFPath, path, app)
            Shell.su("""echo "#!/system/bin/sh\nwhile read line; do echo \${"$"}{line} | grep $pkg | awk '{print \${'$'}2}' | xargs umount -l; done< /proc/mounts" > /data/adb/post-fs-data.d/$app.sh""").exec()
            return Shell.su("chmod 744 /data/adb/service.d/$app.sh").exec().isSuccess
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    private fun linkApp(apkFPath: String, pkg: String, path: String): Boolean {
        Log.d(INSTALLER_TAG, "Linking app")
        Shell.su("am force-stop $pkg").exec()
        Shell.su("""for i in ${'$'}(ls /data/app/ | grep $pkg | tr " "); do umount -l "/data/app/${"$"}i/base.apk"; done """).exec()
        val response = Shell.su("""su -mm -c "mount -o bind $apkFPath $path"""").exec()
        Thread.sleep(500)
        Shell.su("am force-stop $pkg").exec()
        return response.isSuccess
    }

    private fun setupFolder(apkInstallPath: String): Boolean {
        return Shell.su("mkdir -p $apkInstallPath").exec().isSuccess
    }

    //check version and perform action based on result
    private fun checkVersion(versionCode: Int, baseApkFiles: ArrayList<FileInfo>, pkg: String, context: Context): Boolean {
        Log.d(INSTALLER_TAG, "Checking stock version")
        val path = getPackageDir(context, pkg)
        if (path != null) {
            if (path.contains("/data/app/")) {
                when (getVersionNumber(pkg, context)?.let { compareVersion(it,versionCode) } ) {
                    1 -> return fixHigherVer(baseApkFiles, pkg, context)
                    -1 -> return installStock(baseApkFiles, pkg, context)
                }
                return true
            }
            return installStock(baseApkFiles, pkg, context)
        }
        return installStock(baseApkFiles, pkg, context)
    }

    private fun getPkgInfo(pkg: String, context: Context): PackageInfo? {
        return try {
            context.packageManager.getPackageInfo(pkg, 0)
        } catch (e:Exception) {
            Log.d(INSTALLER_TAG, "Unable to get package info")
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
    private fun fixHigherVer(apkFiles: ArrayList<FileInfo>, pkg: String, context: Context) : Boolean {
        Log.d(INSTALLER_TAG, "Downgrading stock")
        if (uninstallRootApk(pkg)) {
            return if (pkg == vancedRootPkg) installSplitApkFiles(apkFiles, context) else installRootMusic(apkFiles, context)
        }
        sendFailure(listOf("Failed_Uninstall").toMutableList(), context)
        sendCloseDialog(context)
        return false
    }

    //install stock youtube matching vanced version
    private fun installStock(baseApkFiles: ArrayList<FileInfo>, pkg: String, context: Context): Boolean {
        Log.d(INSTALLER_TAG, "Installing stock")
        return if (pkg == vancedRootPkg) installSplitApkFiles(baseApkFiles, context) else installRootMusic(baseApkFiles, context)
    }

    //set chcon to apk_data_file
    private fun chConV(apkFPath: String, context: Context): Boolean {
        Log.d(INSTALLER_TAG, "Running chcon")
        val response = Shell.su("chcon u:object_r:apk_data_file:s0 $apkFPath").exec()
        //val response = Shell.su("chcon -R u:object_r:system_file:s0 $path").exec()
        return if (response.isSuccess) {
            true
        } else {
            sendFailure(response.out, context)
            sendCloseDialog(context)
            false
        }
    }

    //move patch to data/app
    private fun moveAPK(apkFile: String, path: String, pkg: String, context: Context) : Boolean {
        Log.d(INSTALLER_TAG, "Moving app")
        val apkinF = SuFile.open(apkFile)
        val apkoutF = SuFile.open(path)

        if(apkinF.exists()) {
            try {
                Shell.su("am force-stop $pkg").exec()

                //Shell.su("rm -r SuFile.open(path).parent")

                copy(apkinF,apkoutF)
                Shell.su("chmod 644 $path").exec().isSuccess
                return if(Shell.su("chown system:system $path").exec().isSuccess) {
                    true
                } else {
                    sendFailure(listOf("Chown_Fail").toMutableList(), context)
                    sendCloseDialog(context)
                    false
                }

            }
            catch (e: IOException)
            {
                sendFailure(listOf("${e.message}").toMutableList(), context)
                sendCloseDialog(context)
                return false
            }
        }
        sendFailure(listOf("IFile_Missing").toMutableList(), context)
        sendCloseDialog(context)
        return false
    }


    @Throws(IOException::class)
    fun copy(src: File, dst: File) {
        val cmd = Shell.su("mv ${src.absolutePath} ${dst.absolutePath}").exec().isSuccess
        Log.d("ZLog", cmd.toString())
    }

    @Suppress("DEPRECATION")
    private fun getVersionNumber(pkg: String, context: Context): Int? {
        try {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                context.packageManager.getPackageInfo(vancedRootPkg, 0).longVersionCode.and(0xFFFFFFFF).toInt()
            else
                context.packageManager.getPackageInfo(vancedRootPkg, 0).versionCode
        }
        catch (e : Exception) {
            val execRes = Shell.su("dumpsys package $pkg | grep versionCode").exec()
            if(execRes.isSuccess) {
                val result = execRes.out
                var version = 0
                result
                    .asSequence()
                    .map { it.substringAfter("=") }
                    .map { it.substringBefore(" ") }
                    .filter { version < Integer.valueOf(it) }
                    .forEach { version = Integer.valueOf(it) }
                return version
            }
        }
        return null
    }

    //get path of the installed youtube
    fun getPackageDir(context: Context, pkg: String): String?
    {
        val p = getPkgInfo(pkg, context)
        return if(p != null)
        {
            p.applicationInfo.sourceDir
        }
        else
        {
            val execRes = Shell.su("dumpsys package $pkg | grep codePath").exec()
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

    private fun setInstallerPackage(context: Context, target: String, installer: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) return
        try {
            Log.d(INSTALLER_TAG, "Setting installer package to $installer for $target")
            val installerUid = context.packageManager.getPackageUid(installer, 0)
            val res = Shell.su("""su $installerUid -c 'pm set-installer $target $installer'""").exec()
            if (res.out.any { line -> line.contains("Success") }) {
                Log.d(INSTALLER_TAG, "Installer package successfully set")
                return
            }
            Log.d(INSTALLER_TAG, "Failed setting installer package")
        } catch (e: PackageManager.NameNotFoundException) {
            Log.d(INSTALLER_TAG, "Installer package $installer not found. Skipping setting installer")
        }
    }
}
