package com.vanced.manager.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.vanced.manager.BuildConfig.APPLICATION_ID
import com.vanced.manager.R
import com.vanced.manager.ui.dialogs.AppDownloadDialog
import com.vanced.manager.ui.fragments.HomeFragment
import com.vanced.manager.utils.DownloadHelper.downloadProgress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object AppUtils {

    const val vancedPkg = "com.vanced.android.youtube"
    const val vancedRootPkg = "com.google.android.youtube"
    const val musicPkg = "com.vanced.android.apps.youtube.music"
    const val musicRootPkg = "com.google.android.apps.youtube.music"
    const val microgPkg = "com.mgoogle.android.gms"
    const val managerPkg = APPLICATION_ID

    fun sendRefresh(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            delay(700)
            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(HomeFragment.REFRESH_HOME))
        }
    }

    fun sendCloseDialog(context: Context) {
        downloadProgress.get()?.installing?.set(false)
        CoroutineScope(Dispatchers.IO).launch {
            delay(700)
            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(AppDownloadDialog.CLOSE_DIALOG))
        }
    }

    fun sendFailure(status: Int, context: Context) {
        downloadProgress.get()?.installing?.set(false)
        //Delay error broadcast until activity (and fragment) get back to the screen
        CoroutineScope(Dispatchers.IO).launch {
            delay(700)
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
            status.contains("Failed_Uninstall") -> context.getString(R.string.failed_uninstall)
            status.contains("Chown_Fail") -> context.getString(R.string.chown_fail)
            status.contains("IFile_Missing") -> context.getString(R.string.ifile_missing)
            status.contains("ModApk_Missing") -> context.getString(R.string.modapk_missing)
            status.contains("Files_Missing_VA") -> context.getString(R.string.files_missing_va)
            status.contains("Path_Missing") -> context.getString(R.string.path_missing)
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
