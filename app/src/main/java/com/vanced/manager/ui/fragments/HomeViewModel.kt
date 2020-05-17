package com.vanced.manager.ui.fragments

import android.app.Activity
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import com.vanced.manager.R

class HomeViewModel {

    private val pm: PackageManager? = Activity().packageManager

    //we need to check whether these apps are installed or not
    val microgStatus: Boolean? = pm?.let { isPackageInstalled("com.mgoogle.android.gms", it) }
    val vancedStatus: Boolean? = pm?.let { isPackageInstalled("com.vanced.android.youtube", it) }

    private fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    val microgInstalledTxt: String? = pm?.getPackageInfo("com.mgoogle.android.gms", 0)?.versionName
    val vancedInstalledTxt: String? = pm?.getPackageInfo("com.vanced.android.youtube", 0)?.versionName.toString()



}