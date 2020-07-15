package com.vanced.manager.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat.checkSelfPermission

object AppUtils {

    fun requestPermission(context: Context) {
        val requestPermissionLauncher = registerForActivityResult()
    }

    fun isPermissionGranted(context: Context): Boolean {
        //Check Storage Permissions
        return checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    }

}