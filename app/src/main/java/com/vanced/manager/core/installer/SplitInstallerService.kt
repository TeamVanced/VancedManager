package com.vanced.manager.core.installer

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.vanced.manager.R
import com.vanced.manager.ui.MainActivity
import com.vanced.manager.utils.AppUtils.getErrorMessage
import com.vanced.manager.utils.AppUtils.sendRefreshHome
import com.vanced.manager.utils.NotificationHelper.createBasicNotif

class SplitInstallerService: Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val notifId = 666
        when (intent.getIntExtra(PackageInstaller.EXTRA_STATUS, -999)) {
            PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                createBasicNotif(getString(R.string.installing_app, "Vanced"), notifId, this)
                Log.d(TAG, "Requesting user confirmation for installation")
                val confirmationIntent = intent.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)
                confirmationIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                try {
                    startActivity(confirmationIntent)
                } catch (e: Exception) {
                }
            }
            PackageInstaller.STATUS_SUCCESS -> {
                Log.d(TAG, "Installation succeed")
                getSharedPreferences("installPrefs", Context.MODE_PRIVATE).edit().putBoolean("isInstalling", false).apply()
                sendRefreshHome(this)
                createBasicNotif(
                    getString(R.string.successfully_installed, "Vanced"),
                    notifId,
                    this
                )
            }
            else -> {
                sendFailure(intent.getIntExtra(PackageInstaller.EXTRA_STATUS, -999))
                createBasicNotif(
                    getErrorMessage(intent.getIntExtra(PackageInstaller.EXTRA_STATUS, -999), this),
                    notifId,
                    this
                )
            }
        }
        stopSelf()
        return START_NOT_STICKY
    }

    private fun sendFailure(status: Int) {
        val mIntent = Intent(MainActivity.INSTALL_FAILED)
        mIntent.action = MainActivity.INSTALL_FAILED
        mIntent.putExtra("errorMsg", getErrorMessage(status, this))
        LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object{
        const val TAG = "VMInstall"
    }

}