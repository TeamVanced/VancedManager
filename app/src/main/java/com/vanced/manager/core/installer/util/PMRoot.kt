package com.vanced.manager.core.installer.util

import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.io.SuFile
import com.topjohnwu.superuser.io.SuFileOutputStream
import com.vanced.manager.core.util.errString
import com.vanced.manager.core.util.outString
import java.io.File
import java.io.IOException

object PMRoot {

    fun installApp(apkPath: String): PMRootStatus<Nothing> {
        val apk = File(apkPath)
        val tmpApk = copyApkToTemp(apk) { error ->
            return PMRootStatus.Error(PMRootStatusType.SESSION_FAILED_COPY, error)
        }

        val install = Shell.su("pm", "install", "-r", tmpApk.absolutePath).exec()

        tmpApk.delete()

        if (!install.isSuccess) {
            val errString = install.errString
            return PMRootStatus.Error(getEnumForInstallFailed(errString), errString)
        }

        return PMRootStatus.Success()
    }

    fun installSplitApp(apkPaths: List<String>): PMRootStatus<Nothing> {
        val installCreate = Shell.su("pm", "install-create", "-r").exec()

        if (!installCreate.isSuccess)
            return PMRootStatus.Error(PMRootStatusType.SESSION_FAILED_CREATE, installCreate.errString)

        val sessionId = installCreate.outString

        if (sessionId.toIntOrNull() == null)
            return PMRootStatus.Error(PMRootStatusType.SESSION_INVALID_ID, installCreate.errString)

        for (apkPath in apkPaths) {
            val apk = File(apkPath)
            val tmpApk = copyApkToTemp(apk) { error ->
                return PMRootStatus.Error(PMRootStatusType.SESSION_FAILED_COPY, error)
            }

            val installWrite =
                Shell.su("pm", "install-write", sessionId, tmpApk.name, tmpApk.absolutePath)
                    .exec()

            tmpApk.delete()

            if (!installWrite.isSuccess)
                return PMRootStatus.Error(PMRootStatusType.SESSION_FAILED_WRITE, installWrite.errString)
        }

        val installCommit = Shell.su("pm", "install-commit", sessionId).exec()

        if (!installCommit.isSuccess) {
            val errString = installCommit.errString
            return PMRootStatus.Error(getEnumForInstallFailed(errString), errString)
        }

        return PMRootStatus.Success()
    }

    fun uninstallApp(pkg: String): PMRootStatus<Nothing> {
        val uninstall = Shell.su("pm", "uninstall", pkg).exec()

        if (!uninstall.isSuccess)
            return PMRootStatus.Error(PMRootStatusType.UNINSTALL_FAILED, uninstall.errString)

        return PMRootStatus.Success()
    }

    fun setInstallerPackage(targetPkg: String, installerPkg: String): PMRootStatus<Nothing> {
        val setInstaller = Shell.su("pm", "set-installer", targetPkg, installerPkg)
            .exec()

        if (!setInstaller.isSuccess)
            return PMRootStatus.Error(PMRootStatusType.ACTION_FAILED_SET_INSTALLER, setInstaller.errString)

        return PMRootStatus.Success()
    }

    fun forceStopApp(pkg: String): PMRootStatus<Nothing> {
        val stopApp = Shell.su("am", "force-stop", pkg).exec()

        if (!stopApp.isSuccess)
            return PMRootStatus.Error(PMRootStatusType.ACTION_FAILED_FORCE_STOP_APP, stopApp.errString)

        return PMRootStatus.Success()
    }

    fun getPackageDir(pkg: String): PMRootStatus<String> {
        val delimeter = "path: "
        val dumpsys = Shell.su("dumpsys", "package", pkg, "|", "grep", delimeter).exec()

        if (!dumpsys.isSuccess)
            return PMRootStatus.Error(PMRootStatusType.ACTION_FAILED_GET_PACKAGE_DIR, dumpsys.errString)

        return PMRootStatus.Success(dumpsys.outString.removePrefix(delimeter))
    }
}

private inline fun copyApkToTemp(
    apk: File,
    onError: (String) -> Unit
): SuFile {
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
        onError(e.stackTraceToString())
    }

    return tmpApk
}

private fun getEnumForInstallFailed(outString: String) =
    when {
        outString.contains("INSTALL_FAILED_ABORTED") -> PMRootStatusType.INSTALL_FAILED_ABORTED
        outString.contains("INSTALL_FAILED_ALREADY_EXISTS") -> PMRootStatusType.INSTALL_FAILED_ALREADY_EXISTS
        outString.contains("INSTALL_FAILED_CPU_ABI_INCOMPATIBLE") -> PMRootStatusType.INSTALL_FAILED_CPU_ABI_INCOMPATIBLE
        outString.contains("INSTALL_FAILED_INSUFFICIENT_STORAGE") -> PMRootStatusType.INSTALL_FAILED_INSUFFICIENT_STORAGE
        outString.contains("INSTALL_FAILED_INVALID_APK") -> PMRootStatusType.INSTALL_FAILED_INVALID_APK
        outString.contains("INSTALL_FAILED_VERSION_DOWNGRADE") -> PMRootStatusType.INSTALL_FAILED_VERSION_DOWNGRADE
        outString.contains("INSTALL_PARSE_FAILED_NO_CERTIFICATES") -> PMRootStatusType.INSTALL_FAILED_PARSE_NO_CERTIFICATES
        else -> PMRootStatusType.INSTALL_FAILED_UNKNOWN
    }