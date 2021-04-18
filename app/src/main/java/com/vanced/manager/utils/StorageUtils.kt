package com.vanced.manager.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import com.vanced.manager.ui.dialogs.DialogContainer.storageDialog

const val storage = "/storage/emulated/0"

const val internalPath = "$storage/Android/data/com.vanced.manager/files"
const val externalPath = "$storage/Vanced Manager"

val Context.managerPath: String
    get() {
        var path = defPrefs.managerStorage ?: internalPath

        if (path == externalPath && !canAccessStorage(this)) {
            path = internalPath
        }

        return path
    }

fun Context.getFilePathInStorage(child: String): String = "$managerPath/$child"


inline fun performStorageAction(activity: FragmentActivity, action: (managerPath: String) -> Unit) {
    val managerPath = activity.managerPath

    if (managerPath == internalPath) {
        return action(managerPath)
    }

    if (canAccessStorage(activity)) {
        action(managerPath)
    } else {
        storageDialog(activity)
    }
}

fun canAccessStorage(context: Context): Boolean = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> Environment.isExternalStorageManager()
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
        listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).all {
            context.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }
    }
    else -> true
}

fun requestStoragePerms(activity: FragmentActivity, permissionLauncher: ActivityResultLauncher<Array<String>>?) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        activity.startActivity(
            Intent(
                Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
            )
        )
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        permissionLauncher?.launch(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )
    }
}