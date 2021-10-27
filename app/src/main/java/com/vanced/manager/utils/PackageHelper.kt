package com.vanced.manager.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageInstaller
import android.content.pm.PackageManager
import android.os.Build
import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.io.SuFile
import com.vanced.manager.core.installer.AppInstallerService
import com.vanced.manager.core.installer.AppUninstallerService
import com.vanced.manager.utils.AppUtils.log
import com.vanced.manager.utils.AppUtils.musicRootPkg
import com.vanced.manager.utils.AppUtils.playStorePkg
import com.vanced.manager.utils.AppUtils.sendCloseDialog
import com.vanced.manager.utils.AppUtils.sendFailure
import com.vanced.manager.utils.AppUtils.sendRefresh
import com.vanced.manager.utils.AppUtils.vancedRootPkg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.*

object PackageHelper {

    const val apkInstallPath = "/data/adb"
    private const val INSTALLER_TAG = "VMInstall"
    private val vancedThemes =
        vanced.value?.array<String>("themes")?.value ?: listOf("black", "dark", "pink", "blue")

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
    fun getPkgVerCode(pkg: String, pm: PackageManager): Int? {
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
                        vancedThemes.any { file.name == "$it.apk" } && !splitFiles.contains("base") -> splitFiles.add(
                            "base"
                        )
                        file.name.matches(Regex("split_config\\.(..)\\.apk")) && !splitFiles.contains(
                            "lang"
                        ) -> splitFiles.add("lang")
                        (file.name.startsWith("split_config.arm") || file.name.startsWith("split_config.x86")) && !splitFiles.contains(
                            "arch"
                        ) -> splitFiles.add("arch")
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
        val pendingIntent = PendingIntent.getService(context, 0, callbackIntent, intentFlags)
        try {
            context.packageManager.packageInstaller.uninstall(pkg, pendingIntent.intentSender)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun install(path: String, context: Context) {
        val callbackIntent = Intent(context, AppInstallerService::class.java)
        val pendingIntent = PendingIntent.getService(context, 0, callbackIntent, intentFlags)
        val packageInstaller = context.packageManager.packageInstaller
        val params =
            PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            params.setRequireUserAction(PackageInstaller.SessionParams.USER_ACTION_NOT_REQUIRED)
        }
        val sessionId: Int
        var session: PackageInstaller.Session? = null
        try {
            sessionId = packageInstaller.createSession(params)
            session = packageInstaller.openSession(sessionId)
            val inputStream: InputStream = FileInputStream(path)
            val outputStream = session.openWrite("install", 0, -1)
            val buffer = ByteArray(65536)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }
            session.fsync(outputStream)
            inputStream.close()
            outputStream.close()
            session.commit(pendingIntent.intentSender)
        } catch (e: Exception) {
            log(INSTALLER_TAG, e.stackTraceToString())
            sendFailure(e.stackTraceToString(), context)
            sendCloseDialog(context)
        } finally {
            session?.close()
        }
    }

    private fun installRootMusic(files: List<File>, context: Context): Boolean {
        files.forEach { apk ->
            if (apk.name != "root.apk") {
                val newPath = "/data/local/tmp/${apk.name}"

                //Copy apk to tmp folder in order to avoid permission denials
                Shell.su("cp ${apk.path} $newPath").exec()
                val command = Shell.su("pm install -r $newPath").exec()
                Shell.su("rm $newPath").exec()
                if (!command.isSuccess) {
                    sendFailure(command.out, context)
                    sendCloseDialog(context)
                    return false
                }
            }
        }
        return true
    }

    private fun installRootApp(
        context: Context,
        app: String,
        appVerCode: Int?,
        pkg: String,
        modApkBool: (fileName: String) -> Boolean
    ) = CoroutineScope(Dispatchers.IO).launch {
        if (!isMagiskInstalled()) {
            sendFailure("NO_MAGISK", context)
            sendCloseDialog(context)
            return@launch
        }

        val apkFilesPath = context.getExternalFilesDir("$app/root")?.path
        val files = File(apkFilesPath.toString()).listFiles()?.toList()
        if (files != null) {
            val modApk: File? = files.lastOrNull { modApkBool(it.name) }
            if (modApk != null) {
                if (appVerCode != null) {
                    if (overwriteBase(modApk, files, appVerCode, pkg, app, context)) {
                        setInstallerPackage(context, pkg, playStorePkg)
                        log(INSTALLER_TAG, "Finished installation")
                        sendRefresh(context)
                        sendCloseDialog(context)
                    }
                } else {
                    sendFailure("appVerCode is null", context)
                    sendCloseDialog(context)
                }
            } else {
                sendFailure("ModApk_Missing", context)
                sendCloseDialog(context)
            }
        } else {
            sendFailure("Files_Missing_VA", context)
            sendCloseDialog(context)
        }

    }

    fun installMusicRoot(context: Context) {
        installRootApp(
            context,
            "music",
            music.value?.int("versionCode"),
            musicRootPkg
        ) {
            it == "root.apk"
        }
    }

    fun installVancedRoot(context: Context) {
        installRootApp(
            context,
            "vanced",
            vanced.value?.int("versionCode"),
            vancedRootPkg
        ) { fileName ->
            vancedThemes.any { fileName == "$it.apk" }
        }
    }

    fun installSplitApkFiles(
        context: Context,
        appName: String
    ) {
        val packageInstaller = context.packageManager.packageInstaller
        val folder = File(context.getExternalFilesDir("$appName/nonroot")?.path.toString())
        var session: PackageInstaller.Session? = null
        val sessionId: Int
        val sessionParams =
            PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            sessionParams.setRequireUserAction(PackageInstaller.SessionParams.USER_ACTION_NOT_REQUIRED)
        }
        val callbackIntent = Intent(context, AppInstallerService::class.java)
        val pendingIntent = PendingIntent.getService(context, 0, callbackIntent, intentFlags)
        try {
            sessionId = packageInstaller.createSession(sessionParams)
            session = packageInstaller.openSession(sessionId)
            folder.listFiles()?.forEach { apk ->
                val inputStream = FileInputStream(apk)
                val outputStream = session.openWrite(apk.name, 0, apk.length())
                val buffer = ByteArray(65536)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }
                session.fsync(outputStream)
                inputStream.close()
                outputStream.close()
            }
            session.commit(pendingIntent.intentSender)
        } catch (e: Exception) {
            log(INSTALLER_TAG, e.stackTraceToString())
            sendFailure(e.stackTraceToString(), context)
            sendCloseDialog(context)
        } finally {
            session?.close()
        }
    }

    private fun installSplitApkFilesRoot(apkFiles: List<File>?, context: Context): Boolean {
        val filenames = arrayOf("black.apk", "dark.apk", "blue.apk", "pink.apk", "hash.json")
        log(INSTALLER_TAG, "installing split apk files: ${apkFiles?.map { it.name }}")
        val sessionId =
            Shell.su("pm install-create -r").exec().out.joinToString(" ").filter { it.isDigit() }
                .toIntOrNull()

        if (sessionId == null) {
            sendFailure("Session ID is null", context)
            sendCloseDialog(context)
            return false
        }

        apkFiles?.filter { !filenames.contains(it.name) }?.forEach { apkFile ->
            val apkName = apkFile.name
            log(INSTALLER_TAG, "installing APK: $apkName")
            val newPath = "/data/local/tmp/$apkName"
            //Copy apk to tmp folder in order to avoid permission denials
            Shell.su("cp ${apkFile.path} $newPath").exec()
            val command = Shell.su("pm install-write $sessionId $apkName $newPath").exec()
            Shell.su("rm $newPath").exec()
            if (!command.isSuccess) {
                sendFailure(command.out, context)
                sendCloseDialog(context)
                return false
            }
        }
        log(INSTALLER_TAG, "committing...")
        val installResult = Shell.su("pm install-commit $sessionId").exec()
        if (!installResult.isSuccess) {
            sendFailure(installResult.out, context)
            sendCloseDialog(context)
            return false
        }
        return true
    }


    //overwrite stock Vanced/Music
    private fun overwriteBase(
        apkFile: File,
        baseApkFiles: List<File>,
        versionCode: Int,
        pkg: String,
        app: String,
        context: Context
    ): Boolean {
        if (checkVersion(versionCode, baseApkFiles, pkg, context)) {
            val path = getPackageDir(context, pkg)
            val apath = apkFile.absolutePath

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
        return false
    }

    private fun setupScript(
        apkFPath: String,
        path: String,
        app: String,
        pkg: String,
        context: Context
    ): Boolean {
        try {
            log(INSTALLER_TAG, "Setting up script")
            context.writeServiceDScript(apkFPath, path, app)
            Shell.su("""echo "#!/system/bin/sh\nwhile read line; do echo \${"$"}{line} | grep $pkg | awk '{print \${'$'}2}' | xargs umount -l; done< /proc/mounts" > /data/adb/post-fs-data.d/$app.sh""")
                .exec()
            return Shell.su("chmod 744 /data/adb/service.d/$app.sh").exec().isSuccess
        } catch (e: IOException) {
            sendFailure(e.stackTraceToString(), context)
            sendCloseDialog(context)
            log(INSTALLER_TAG, e.stackTraceToString())
        }
        return false
    }

    private fun linkApp(apkFPath: String, pkg: String, path: String): Boolean {
        log(INSTALLER_TAG, "Linking app")
        Shell.su("am force-stop $pkg").exec()
        Shell.su("""for i in ${'$'}(ls /data/app/ | grep $pkg | tr " "); do umount -l "/data/app/${"$"}i/base.apk"; done """)
            .exec()
        val response = Shell.su("""su -mm -c "mount -o bind $apkFPath $path"""").exec()
        Thread.sleep(500)
        Shell.su("am force-stop $pkg").exec()
        return response.isSuccess
    }

    private fun setupFolder(apkInstallPath: String): Boolean {
        return Shell.su("mkdir -p $apkInstallPath").exec().isSuccess
    }

    //check version and perform action based on result
    private fun checkVersion(
        versionCode: Int,
        baseApkFiles: List<File>,
        pkg: String,
        context: Context
    ): Boolean {
        log(INSTALLER_TAG, "Checking stock version")
        val path = getPackageDir(context, pkg)
        if (path != null) {
            if (path.contains("/data/app/")) {
                when (getVersionNumber(pkg, context)?.let { compareVersion(it, versionCode) }) {
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
        } catch (e: Exception) {
            log(INSTALLER_TAG, "Unable to get package info")
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
    private fun fixHigherVer(apkFiles: List<File>, pkg: String, context: Context): Boolean {
        log(INSTALLER_TAG, "Downgrading stock")
        if (uninstallRootApk(pkg)) {
            return if (pkg == vancedRootPkg) installSplitApkFilesRoot(
                apkFiles,
                context
            ) else installRootMusic(apkFiles, context)
        }
        sendFailure(listOf("Failed_Uninstall").toMutableList(), context)
        sendCloseDialog(context)
        return false
    }

    //install stock youtube matching vanced version
    private fun installStock(baseApkFiles: List<File>, pkg: String, context: Context): Boolean {
        log(INSTALLER_TAG, "Installing stock")
        return if (pkg == vancedRootPkg) installSplitApkFilesRoot(
            baseApkFiles,
            context
        ) else installRootMusic(baseApkFiles, context)
    }

    private fun isMagiskInstalled() = Shell.su("magisk -c").exec().isSuccess

    //set chcon to apk_data_file
    private fun chConV(apkFPath: String, context: Context): Boolean {
        log(INSTALLER_TAG, "Running chcon")
        val response = Shell.su("chcon u:object_r:apk_data_file:s0 $apkFPath").exec()
        //val response = Shell.su("chcon -R u:object_r:system_file:s0 $path").exec()
        if (!response.isSuccess) {
            sendFailure(response.out, context)
            sendCloseDialog(context)
            return false
        }
        return true
    }

    //move patch to data/app
    private fun moveAPK(apkFile: String, path: String, pkg: String, context: Context): Boolean {
        log(INSTALLER_TAG, "Moving app")
        Shell.su("am force-stop $pkg").exec()

        val mv = Shell.su("cp $apkFile $path").exec()
        if (!mv.isSuccess) {
            sendFailure(mv.out.apply { add(0, "MV_Fail") }, context)
            sendCloseDialog(context)
            return false
        }

        val chmod = Shell.su("chmod 644 $path").exec()
        if (!chmod.isSuccess) {
            sendFailure(chmod.out.apply { add(0, "Chmod_Fail") }, context)
            sendCloseDialog(context)
            return false
        }

        val chown = Shell.su("chown system:system $path").exec()
        if (!chown.isSuccess) {
            sendFailure(chown.out.apply { add(0, "Chown_Fail") }, context)
            sendCloseDialog(context)
            return false
        }

        return true
    }

    @Suppress("DEPRECATION")
    private fun getVersionNumber(pkg: String, context: Context): Int? {
        try {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                context.packageManager.getPackageInfo(vancedRootPkg, 0).longVersionCode.and(
                    0xFFFFFFFF
                ).toInt()
            else
                context.packageManager.getPackageInfo(vancedRootPkg, 0).versionCode
        } catch (e: Exception) {
            val execRes = Shell.su("dumpsys package $pkg | grep versionCode").exec()
            if (execRes.isSuccess) {
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
    fun getPackageDir(context: Context, pkg: String): String? {
        val p = getPkgInfo(pkg, context)
        return if (p != null) {
            p.applicationInfo.sourceDir
        } else {
            val execRes = Shell.su("dumpsys package $pkg | grep codePath").exec()
            if (execRes.isSuccess) {
                val result = execRes.out
                for (line in result) {
                    if (line.contains("data/app")) "${line.substringAfter("=")}/base.apk"
                }
            }
            null
        }
    }

    private fun setInstallerPackage(context: Context, target: String, installer: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) return
        try {
            log(INSTALLER_TAG, "Setting installer package to $installer for $target")
            val installerUid = context.packageManager.getPackageUid(installer, 0)
            val res =
                Shell.su("""su $installerUid -c 'pm set-installer $target $installer'""").exec()
            if (res.out.any { line -> line.contains("Success") }) {
                log(INSTALLER_TAG, "Installer package successfully set")
                return
            }
            log(INSTALLER_TAG, "Failed setting installer package")
        } catch (e: PackageManager.NameNotFoundException) {
            log(INSTALLER_TAG, "Installer package $installer not found. Skipping setting installer")
        }
    }
}
