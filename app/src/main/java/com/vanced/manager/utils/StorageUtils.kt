package com.vanced.manager.utils

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.fragment.app.FragmentActivity
import com.vanced.manager.ui.MainActivity
import com.vanced.manager.ui.dialogs.DialogContainer.storageDialog

@Suppress("DEPRECATION")
val managerPath get() = "${Environment.getExternalStorageDirectory().path}/Vanced Manager"

inline fun performStorageAction(activity: FragmentActivity, action: () -> Unit) {
    if (canAccessStorage(activity)) {
        action()
    } else {
        storageDialog(activity)
    }
}

fun canAccessStorage(activity: FragmentActivity): Boolean = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> Environment.isExternalStorageManager()
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
        listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).all {
            activity.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }
    }
    else -> true
}

fun requestStoragePerms(activity: FragmentActivity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        activity.startActivity(
            Intent(
                Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
            )
        )
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        activity.requestPermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            MainActivity.REQUEST_CODE
        )
    }
}