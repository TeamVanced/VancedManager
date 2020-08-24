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
            status.contains("Failed_Uninstall") -> context.getString(R.string.Failed_Uninstall)
            status.contains("Chown_Fail") -> context.getString(R.string.Chown_Fail)
            status.contains("IFile_Missing") -> context.getString(R.string.IFile_Missing)
            status.contains("ModApk_Missing") -> context.getString(R.string.ModApk_Missing)
            status.contains("Files_Missing_VA") -> context.getString(R.string.Files_Missing_VA)
            status.contains("Corrupt_Data") -> context.getString(R.string.Corrupt_Data)
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