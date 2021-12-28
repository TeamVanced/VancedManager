package com.vanced.manager.core.installer.util

import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.io.SuFile
import com.topjohnwu.superuser.io.SuFileOutputStream
import com.vanced.manager.core.util.errString
import com.vanced.manager.core.util.outString
import java.io.File
import java.io.IOException

object PMRoot {

    fun installApp(apkPath: String): PMRootResult<Nothing> {
        val apk = File(apkPath)
        val tmpApk = copyApkToTemp(apk).getOrElse { exception ->
            return PMRootResult.Error(PMRootStatus.SESSION_FAILED_COPY, exception.stackTraceToString())
        }

        val install = Shell.su("pm", "install", "-r", tmpApk.absolutePath).exec()

        tmpApk.delete()

        if (!install.isSuccess) {
            val errString = install.errString
            return PMRootResult.Error(getEnumForInstallFailed(errString), errString)
        }

        return PMRootResult.Success()
    }

    fun installSplitApp(apkPaths: List<String>): PMRootResult<Nothing> {
        val installCreate = Shell.su("pm", "install-create", "-r").exec()

        if (!installCreate.isSuccess)
            return PMRootResult.Error(PMRootStatus.SESSION_FAILED_CREATE, installCreate.errString)

        val sessionId = installCreate.outString

        if (sessionId.toIntOrNull() == null)
            return PMRootResult.Error(PMRootStatus.SESSION_INVALID_ID, installCreate.errString)

        for (apkPath in apkPaths) {
            val apk = File(apkPath)
            val tmpApk = copyApkToTemp(apk).getOrElse { exception ->
                return PMRootResult.Error(PMRootStatus.SESSION_FAILED_COPY, exception.stackTraceToString())
            }

            val installWrite =
                Shell.su("pm", "install-write", sessionId, tmpApk.name, tmpApk.absolutePath)
                    .exec()

            tmpApk.delete()

            if (!installWrite.isSuccess)
                return PMRootResult.Error(PMRootStatus.SESSION_FAILED_WRITE, installWrite.errString)
        }

        val installCommit = Shell.su("pm", "install-commit", sessionId).exec()

        if (!installCommit.isSuccess) {
            val errString = installCommit.errString
            return PMRootResult.Error(getEnumForInstallFailed(errString), errString)
        }

        return PMRootResult.Success()
    }

    fun uninstallApp(pkg: String): PMRootResult<Nothing> {
        val uninstall = Shell.su("pm", "uninstall", pkg).exec()

        if (!uninstall.isSuccess)
            return PMRootResult.Error(PMRootStatus.UNINSTALL_FAILED, uninstall.errString)

        return PMRootResult.Success()
    }

    fun setInstallerPackage(targetPkg: String, installerPkg: String): PMRootResult<Nothing> {
        val setInstaller = Shell.su("pm", "set-installer", targetPkg, installerPkg)
            .exec()

        if (!setInstaller.isSuccess)
            return PMRootResult.Error(PMRootStatus.ACTION_FAILED_SET_INSTALLER, setInstaller.errString)

        return PMRootResult.Success()
    }

    fun forceStopApp(pkg: String): PMRootResult<Nothing> {
        val stopApp = Shell.su("am", "force-stop", pkg).exec()

        if (!stopApp.isSuccess)
            return PMRootResult.Error(PMRootStatus.ACTION_FAILED_FORCE_STOP_APP, stopApp.errString)

        return PMRootResult.Success()
    }

    fun getPackageVersionName(pkg: String): PMRootResult<String> {
        val keyword = "versionName="
        val dumpsys = Shell.su("dumpsys", "package", pkg, "|", "grep", keyword).exec()

        if (!dumpsys.isSuccess)
            return PMRootResult.Error(PMRootStatus.ACTION_FAILED_GET_PACKAGE_VERSION_NAME,
                dumpsys.errString)

        return PMRootResult.Success(dumpsys.outString.removePrefix(keyword))
    }

    fun getPackageVersionCode(pkg: String): PMRootResult<Long> {
        val keyword = "versionCode="
        val dumpsys = Shell.su("dumpsys", "package", pkg, "|", "grep", keyword).exec()

        if (!dumpsys.isSuccess)
            return PMRootResult.Error(PMRootStatus.ACTION_FAILED_GET_PACKAGE_VERSION_CODE,
                dumpsys.errString)

        return PMRootResult.Success(dumpsys.outString.removePrefix(keyword).substringAfter("minSdk")
            .toLong())
    }

    fun getPackageDir(pkg: String): PMRootResult<String> {
        val keyword = "path: "
        val dumpsys = Shell.su("dumpsys", "package", pkg, "|", "grep", keyword).exec()

        if (!dumpsys.isSuccess)
            return PMRootResult.Error(PMRootStatus.ACTION_FAILED_GET_PACKAGE_DIR, dumpsys.errString)

        return PMRootResult.Success(dumpsys.outString.removePrefix(keyword))
    }
}

private fun copyApkToTemp(apk: File, ): Result<SuFile> {
    val tmpPath = "/data/local/tmp/${apk.name}"

    val tmpApk = SuFile(tmpPath).apply {
        createNewFile()
    }

    try {
        SuFileOutputStream.open(tmpApk).use {
            it.write(apk.readBytes())
            it.flush()
        }
    } catch (e: IOException) {
        return Result.failure(e)
    }

    return Result.success(tmpApk)
}

private fun getEnumForInstallFailed(outString: String) =
    when {
        outString.contains("INSTALL_FAILED_ABORTED") -> PMRootStatus.INSTALL_FAILED_ABORTED
        outString.contains("INSTALL_FAILED_ALREADY_EXISTS") -> PMRootStatus.INSTALL_FAILED_ALREADY_EXISTS
        outString.contains("INSTALL_FAILED_CPU_ABI_INCOMPATIBLE") -> PMRootStatus.INSTALL_FAILED_CPU_ABI_INCOMPATIBLE
        outString.contains("INSTALL_FAILED_INSUFFICIENT_STORAGE") -> PMRootStatus.INSTALL_FAILED_INSUFFICIENT_STORAGE
        outString.contains("INSTALL_FAILED_INVALID_APK") -> PMRootStatus.INSTALL_FAILED_INVALID_APK
        outString.contains("INSTALL_FAILED_VERSION_DOWNGRADE") -> PMRootStatus.INSTALL_FAILED_VERSION_DOWNGRADE
        outString.contains("INSTALL_PARSE_FAILED_NO_CERTIFICATES") -> PMRootStatus.INSTALL_FAILED_PARSE_NO_CERTIFICATES
        else -> PMRootStatus.INSTALL_FAILED_UNKNOWN
    }