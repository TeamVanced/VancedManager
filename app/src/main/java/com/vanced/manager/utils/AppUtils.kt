package com.vanced.manager.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.vanced.manager.R
import com.vanced.manager.ui.fragments.HomeFragment
import kotlinx.coroutines.*

object AppUtils {

    var installing = false

    fun sendFailure(status: Int, context: Context) {
        //Delay error broadcast until activity (and fragment) get back to the screen
        CoroutineScope(Dispatchers.IO).launch {
            delay(500)
            val intent = Intent(HomeFragment.INSTALL_FAILED)
            intent.putExtra("errorMsg", getErrorMessage(status, context))
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }
    }

    fun sendFailure(error: MutableList<String>, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val intent = Intent(HomeFragment.INSTALL_FAILED)
            intent.putExtra("errorMsg", getErrorMessage(error.joinToString(), context))
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }
    }

    private fun getErrorMessage(status: String, context: Context): String {
        return when {
            status.contains("INSTALL_FAILED_ABORTED") -> context.getString(R.string.installation_aborted)
            status.contains("INSTALL_FAILED_ALREADY_EXISTS") -> context.getString(R.string.installation_conflict)
            status.contains("INSTALL_FAILED_CPU_ABI_INCOMPATIBLE") -> context.getString(R.string.installation_incompatible)
            status.contains("INSTALL_FAILED_INSUFFICIENT_STORAGE") -> context.getString(R.string.installation_storage)
            status.contains("INSTALL_FAILED_INVALID_APK") -> context.getString(R.string.installation_invalid)
            status.contains("INSTALL_FAILED_VERSION_DOWNGRADE") -> context.getString(R.string.installation_downgrade)
            status.contains("INSTALL_PARSE_FAILED_NO_CERTIFICATES") -> context.getString(R.string.installation_signature)
            status.contains("Failed_Uninstall") -> "Failed Uninstall Of Installed Version, Try Manually"
            status.contains("Chown_Fail") -> "Failed To Chown, Try Again"
            status.contains("IFile_Missing") -> "Input File Missing"
            status.contains("ModApk_Missing") -> "modApk Is Null Missing (dark.apk/black.apk) In apks Folder"
            status.contains("Files_Missing_VA") -> "Files are missing, Failed Download?"
            status.contains("Corrupt_Data") -> "Download Went Corrupt, Retry or clear VanM Data"
            else ->
                if (MiuiHelper.isMiui())
                    context.getString(R.string.installation_miui)
                else
                    context.getString(R.string.installation_failed)
        }
    }

    private fun getErrorMessage(status: Int, context: Context): String {
        return when (status) {
            PackageInstaller.STATUS_FAILURE_ABORTED -> context.getString(R.string.installation_aborted)
            PackageInstaller.STATUS_FAILURE_BLOCKED -> context.getString(R.string.installation_blocked)
            PackageInstaller.STATUS_FAILURE_CONFLICT -> context.getString(R.string.installation_conflict)
            PackageInstaller.STATUS_FAILURE_INCOMPATIBLE -> context.getString(R.string.installation_incompatible)
            PackageInstaller.STATUS_FAILURE_INVALID -> context.getString(R.string.installation_invalid)
            PackageInstaller.STATUS_FAILURE_STORAGE -> context.getString(R.string.installation_storage)
            else ->
                if (MiuiHelper.isMiui())
                    context.getString(R.string.installation_miui)
                else
                    context.getString(R.string.installation_failed)
        }
    }


}