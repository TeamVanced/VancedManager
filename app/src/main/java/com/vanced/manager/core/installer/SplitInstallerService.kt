package com.vanced.manager.core.installer

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import com.vanced.manager.R

class SplitInstallerService: Service() {
    private val TAG = "VMInstall"

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.getIntExtra(PackageInstaller.EXTRA_STATUS, -999)) {
            PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                Toast.makeText(this, "Installing...", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Requesting user confirmation for installation")
                val confirmationIntent =
                    intent.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)
                confirmationIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                try {
                    startActivity(confirmationIntent)
                } catch (e: Exception) {
                }
            }
            PackageInstaller.STATUS_SUCCESS -> {
                Log.d(TAG, "Installation succeed")
                getSharedPreferences("installPrefs", Context.MODE_PRIVATE).edit().putBoolean("isInstalling", false).apply()
                launchVanced()
            }
            PackageInstaller.STATUS_FAILURE_ABORTED -> alertBuilder("user aborted installation")
            PackageInstaller.STATUS_FAILURE_INVALID -> alertBuilder("apk files are invalid")
            PackageInstaller.STATUS_FAILURE_CONFLICT -> alertBuilder("app conflicts with already installed app")
            PackageInstaller.STATUS_FAILURE_STORAGE -> alertBuilder("there was an error with storage.\n Hold up how is that even possible?")
            else -> {
                Log.d(TAG, "Installation failed")
                alertBuilder("Installation failed")
            }
        }
        stopSelf()
        return START_NOT_STICKY
    }

    private fun alertBuilder(msg: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage("Operation failed because $msg")
            .setPositiveButton(getString(R.string.close)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun launchVanced() {
        val intent = Intent()
        intent.component = ComponentName("com.vanced.android.youtube", "com.vanced.android.youtube.HomeActivity")
        AlertDialog.Builder(this)
            .setTitle("Success!")
            .setMessage("Vanced has been successfully installed, do you want to launch it now?")
            .setPositiveButton("Launch") {
                _, _ -> startActivity(intent)
            }
            .setNegativeButton("Cancel") {
                dialog, _ -> dialog.dismiss()
            }
            .create()
            .show()
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}