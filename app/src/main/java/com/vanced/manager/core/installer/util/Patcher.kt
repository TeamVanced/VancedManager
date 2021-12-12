package com.vanced.manager.core.installer.util

import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.io.SuFile
import com.topjohnwu.superuser.io.SuFileOutputStream
import com.vanced.manager.core.util.errString
import java.io.File
import java.io.IOException

object Patcher {

    fun setupScript(
        app: String,
        pkg: String,
        stockPath: String,
    ): PMRootStatus<Nothing> {
        val postFsDataScriptPath = getAppPostFsScriptPath(app)
        val serviceDScriptPath = getAppServiceDScriptPath(app)

        val postFsDataScript = getPostFsDataScript(pkg)
        val serviceDScript = getServiceDScript(getAppPatchPath(app), stockPath)

        copyScriptToDestination(postFsDataScriptPath, postFsDataScript) { error ->
            return PMRootStatus.Error(PMRootStatusType.SCRIPT_FAILED_SETUP_POST_FS, error)
        }

        copyScriptToDestination(serviceDScriptPath, serviceDScript) { error ->
            return PMRootStatus.Error(PMRootStatusType.SCRIPT_FAILED_SETUP_SERVICE_D, error)
        }

        return PMRootStatus.Success()
    }

    fun movePatchToDataAdb(patchPath: String, app: String): PMRootStatus<Nothing> {
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
            return PMRootStatus.Error(PMRootStatusType.PATCH_FAILED_COPY, e.stackTraceToString())
        }

        val chmod = Shell.su("chmod", "644", newPatchPath).exec()
        if (!chmod.isSuccess)
            return PMRootStatus.Error(PMRootStatusType.PATCH_FAILED_CHMOD, chmod.errString)

        val chown = Shell.su("chown", "system:system", newPatchPath).exec()
        if (!chmod.isSuccess)
            return PMRootStatus.Error(PMRootStatusType.PATCH_FAILED_CHOWN, chown.errString)

        return PMRootStatus.Success()
    }

    fun chconPatch(app: String): PMRootStatus<Nothing> {
        val chcon = Shell.su("chcon u:object_r:apk_data_file:s0 ${getAppPatchPath(app)}").exec()
        if (!chcon.isSuccess)
            return PMRootStatus.Error(PMRootStatusType.PATCH_FAILED_CHCON, chcon.errString)

        return PMRootStatus.Success()
    }

    fun linkPatch(app: String, pkg: String, stockPath: String): PMRootStatus<Nothing> {
        val umount = Shell.su("""for i in ${'$'}(ls /data/app/ | grep $pkg | tr " "); do umount -l "/data/app/${"$"}i/base.apk"; done """).exec()
        if (!umount.isSuccess)
            return PMRootStatus.Error(PMRootStatusType.LINK_FAILED_UNMOUNT, umount.errString)

        val mount = Shell.su("su", "-mm", "-c", """"mount -o bind ${getAppPatchPath(app)} $stockPath"""").exec()
        if (!mount.isSuccess)
            return PMRootStatus.Error(PMRootStatusType.LINK_FAILED_MOUNT, mount.errString)

        return PMRootStatus.Success()
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

    //TODO return proper error if destroying was unsuccessful
    private fun cleanPatchFiles(
        postFsPath: String,
        serviceDPath: String,
        patchPath: String,
    ): PMRootStatus<Nothing> {
        val postFs = SuFile(postFsPath)
        if (postFs.exists() && !postFs.delete())
            return PMRootStatus.Error(PMRootStatusType.SCRIPT_FAILED_DESTROY_POST_FS, "")

        val serviceD = SuFile(serviceDPath)
        if (serviceD.exists() && !serviceD.delete())
            return PMRootStatus.Error(PMRootStatusType.SCRIPT_FAILED_DESTROY_SERVICE_D, "")

        val patch = SuFile(patchPath)
        if (patch.exists() && !patch.delete())
            return PMRootStatus.Error(PMRootStatusType.PATCH_FAILED_DESTROY, "")

        return PMRootStatus.Success()
    }
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

private fun getPostFsDataScript(pkg: String) =
    """
    #!/system/bin/sh
    while read line; do echo \${'$'}{line} | grep $pkg | awk '{print \${'$'}2}' | xargs umount -l; done< /proc/mounts
    """.trimIndent()

private inline fun copyScriptToDestination(
    scriptDestination: String,
    script: String,
    onError: (String) -> Unit
) {
    val scriptFile = SuFile(scriptDestination)
        .apply {
            if (!exists())
                createNewFile()
        }

    try {
        SuFileOutputStream.open(scriptFile).use {
            it.write(script.toByteArray())
            it.flush()
        }
        val chmod = Shell.su("chmod", "744", scriptFile.absolutePath).exec()
        if (!chmod.isSuccess) {
            onError(chmod.errString)
        }
    } catch (e: IOException) {
        onError(e.stackTraceToString())
    }
}