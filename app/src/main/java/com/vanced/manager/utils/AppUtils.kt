package com.vanced.manager.utils

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.vanced.manager.R
import com.vanced.manager.core.downloader.*
import com.vanced.manager.core.installer.*
import com.vanced.manager.ui.fragments.HomeFragment

object AppUtils {

    fun isInstallationRunning(context: Context): Boolean {
        val serviceClasses = listOf(VancedDownloadService::class.java, MicrogDownloadService::class.java, AppInstaller::class.java, AppInstallerService::class.java, SplitInstaller::class.java, SplitInstallerService::class.java, RootSplitInstallerService::class.java)
        serviceClasses.forEach { service ->
            return isServiceRunning(service, context)
        }
        return false
    }

    @Suppress("DEPRECATION")
    private fun isServiceRunning(cls: Class<out Service>, context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningServices = activityManager.getRunningServices(Int.MAX_VALUE)
        runningServices.forEach { info ->
            if (info.service.className == cls.name) {
                Log.d("VMService", "${cls.name} is running")
                return true
            }
        }
        return false
    }

    fun sendFailure(context: Context, status: Int) {
        val mIntent = Intent(HomeFragment.INSTALL_FAILED)
        mIntent.putExtra("errorMsg", getErrorMessage(status, context))
        LocalBroadcastManager.getInstance(context).sendBroadcast(mIntent)
    }

    private fun getErrorMessage(status: Int, context: Context): String {
        return when (status) {
            PackageInstaller.STATUS_FAILURE_ABORTED -> context.getString(R.string.installation_aborted)
            PackageInstaller.STATUS_FAILURE_BLOCKED -> context.getString(R.string.installation_blocked)
            PackageInstaller.STATUS_FAILURE_STORAGE -> context.getString(R.string.installation_storage)
            PackageInstaller.STATUS_FAILURE_INVALID -> context.getString(R.string.installation_invalid)
            PackageInstaller.STATUS_FAILURE_INCOMPATIBLE -> context.getString(R.string.installation_incompatible)
            PackageInstaller.STATUS_FAILURE_CONFLICT -> context.getString(R.string.installation_conflict)
            else ->
                if (MiuiHelper.isMiui())
                    context.getString(R.string.installation_miui)
                else
                    context.getString(R.string.installation_failed)
        }
    }

}