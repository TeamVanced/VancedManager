package com.vanced.manager.installer.util

import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.io.SuFile
import com.topjohnwu.superuser.io.SuFileOutputStream
import com.vanced.manager.io.ManagerSuFile
import com.vanced.manager.io.SUIOException
import com.vanced.manager.repository.manager.PackageManagerResult
import com.vanced.manager.repository.manager.PackageManagerError
import com.vanced.manager.util.errString
import java.io.File
import java.io.IOException

object Patcher {

    fun setupScript(
        app: String,
        stockPackage: String,
        stockPath: String,
    ): PackageManagerResult<Nothing> {
        val postFsDataScriptPath = getAppPostFsScriptPath(app)
        val serviceDScriptPath = getAppServiceDScriptPath(app)

        val postFsDataScript = getPostFsDataScript(stockPackage)
        val serviceDScript = getServiceDScript(getAppPatchPath(app), stockPath)

        val copyServiceDScript = copyScriptToDestination(postFsDataScript, postFsDataScriptPath)
        if (copyServiceDScript.isFailure)
            return PackageManagerResult.Error(
                PackageManagerError.SCRIPT_FAILED_SETUP_POST_FS,
                copyServiceDScript.exceptionOrNull()!!.stackTraceToString()
            )

        val copyPostFsDataScript = copyScriptToDestination(serviceDScript, serviceDScriptPath)
        if (copyPostFsDataScript.isFailure)
            return PackageManagerResult.Error(
                PackageManagerError.SCRIPT_FAILED_SETUP_SERVICE_D,
                copyPostFsDataScript.exceptionOrNull()!!.stackTraceToString()
            )

        return PackageManagerResult.Success(null)
    }

    fun movePatchToDataAdb(patchPath: String, app: String): PackageManagerResult<Nothing> {
        val newPatchPath = getAppPatchPath(app)

        val patchApk = File(patchPath)
        val newPatchApk = SuFile(newPatchPath).apply {
            if (exists())
                delete()

            createNewFile()
        }

        try {
            patchApk.copyTo(newPatchApk)
        } catch (e: IOException) {
            return PackageManagerResult.Error(PackageManagerError.PATCH_FAILED_COPY, e.stackTraceToString())
        }

        val chmod = Shell.su("chmod", "644", newPatchPath).exec()
        if (!chmod.isSuccess)
            return PackageManagerResult.Error(PackageManagerError.PATCH_FAILED_CHMOD, chmod.errString)

        val chown = Shell.su("chown", "system:system", newPatchPath).exec()
        if (!chmod.isSuccess)
            return PackageManagerResult.Error(PackageManagerError.PATCH_FAILED_CHOWN, chown.errString)

        return PackageManagerResult.Success(null)
    }

    fun chconPatch(app: String): PackageManagerResult<Nothing> {
        val chcon = Shell.su("chcon u:object_r:apk_data_file:s0 ${getAppPatchPath(app)}").exec()
        if (!chcon.isSuccess)
            return PackageManagerResult.Error(PackageManagerError.PATCH_FAILED_CHCON, chcon.errString)

        return PackageManagerResult.Success(null)
    }

    fun linkPatch(app: String, stockPackage: String, stockPath: String): PackageManagerResult<Nothing> {
        val umount =
            Shell.su("""for i in ${'$'}(ls /data/app/ | grep $stockPackage | tr " "); do umount -l "/data/app/${"$"}i/base.apk"; done """)
                .exec()
        if (!umount.isSuccess)
            return PackageManagerResult.Error(PackageManagerError.LINK_FAILED_UNMOUNT, umount.errString)

        val mount =
            Shell.su("su", "-mm", "-c", """"mount -o bind ${getAppPatchPath(app)} $stockPath"""")
                .exec()
        if (!mount.isSuccess)
            return PackageManagerResult.Error(PackageManagerError.LINK_FAILED_MOUNT, mount.errString)

        return PackageManagerResult.Success(null)
    }

    fun destroyPatch(app: String) =
        cleanPatchFiles(
            postFsPath = getAppPostFsScriptPath(app),
            serviceDPath = getAppServiceDScriptPath(app),
            patchPath = getAppPatchPath(app)
        )

    //TODO
    fun destroyOldPatch(app: String) =
        cleanPatchFiles(
            postFsPath = "",
            serviceDPath = "",
            patchPath = ""
        )
}

private fun getAppPatchPath(app: String) = "${getAppPatchFolderPath(app)}/base.apk"
private fun getAppPatchFolderPath(app: String) = "/data/adb/vanced_manager/$app"
private fun getAppPostFsScriptPath(app: String) = "/data/adb/post-fs-data.d/$app.sh"
private fun getAppServiceDScriptPath(app: String) = "/data/adb/service.d/$app.sh"

//TODO support dynamic sleep timer
private fun getServiceDScript(patchPath: String, stockPath: String) =
    """
    #!/system/bin/sh
    while [ "${'$'}(getprop sys.boot_completed | tr -d '\r')" != "1" ]; do sleep 1; done
    sleep 1
    chcon u:object_r:apk_data_file:s0 $patchPath
    mount -o bind $patchPath $stockPath
    """.trimIndent()

private fun getPostFsDataScript(stockPackage: String) =
    """
    #!/system/bin/sh
    while read line; do echo \${'$'}{line} | grep $stockPackage | awk '{print \${'$'}2}' | xargs umount -l; done< /proc/mounts
    """.trimIndent()

private fun cleanPatchFiles(
    postFsPath: String,
    serviceDPath: String,
    patchPath: String,
): PackageManagerResult<Nothing> {
    val files = mapOf(
        postFsPath to PackageManagerError.SCRIPT_FAILED_DESTROY_POST_FS,
        serviceDPath to PackageManagerError.SCRIPT_FAILED_DESTROY_SERVICE_D,
        patchPath to PackageManagerError.PATCH_FAILED_DESTROY,
    )

    for ((filePath, errorStatusType) in files) {
        try {
            with(ManagerSuFile(filePath)) {
                if (exists()) delete()
            }
        } catch (e: SUIOException) {
            return PackageManagerResult.Error(errorStatusType, e.stackTraceToString())
        }
    }

    return PackageManagerResult.Success(null)
}

private fun copyScriptToDestination(
    script: String,
    destination: String,
): Result<Nothing?> {
    val scriptFile = SuFile(destination)
        .apply {
            if (!exists()) createNewFile()
        }

    try {
        SuFileOutputStream.open(scriptFile).use {
            it.write(script.toByteArray())
            it.flush()
        }
        val chmod = Shell.su("chmod", "744", scriptFile.absolutePath).exec()
        if (!chmod.isSuccess) {
            return Result.failure(Exception(chmod.errString))
        }
    } catch (e: IOException) {
        return Result.failure(e)
    }

    return Result.success(null)
}