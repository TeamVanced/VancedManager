package com.vanced.manager.core.installer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.vanced.manager.core.base.BaseFragment
import java.io.File

object MicrogInstaller {
    
    fun installMicrog(activity: Activity) {
        val pn = activity.packageName
        val apk = File(activity.filesDir.path, "microg.apk")
        val uri =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                activity.let { FileProvider.getUriForFile(it, "$pn.provider", apk) }
            } else
                Uri.fromFile(apk)
        val mIntent = Intent(Intent.ACTION_VIEW)
        mIntent.setDataAndType(uri, "application/vnd.android.package-archive")
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        mIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        mIntent.putExtra(Intent.EXTRA_RETURN_RESULT, true)
        activity.startActivityForResult(mIntent, BaseFragment.MICROG_INSTALL)
    }
    
}