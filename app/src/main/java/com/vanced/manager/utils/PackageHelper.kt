package com.vanced.manager.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import com.vanced.manager.core.base.BaseFragment

object PackageHelper {

    fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun uninstallApp(pkgName: String, context: Context) {
        try {
            val uri = Uri.parse("package:$pkgName")
            val uninstall = Intent(Intent.ACTION_DELETE, uri)
            uninstall.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            uninstall.putExtra(Intent.EXTRA_RETURN_RESULT, true)
           Activity().startActivityForResult(uninstall, BaseFragment.APP_UNINSTALL)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Failed to uninstall", Toast.LENGTH_SHORT).show()
        }
    }
}