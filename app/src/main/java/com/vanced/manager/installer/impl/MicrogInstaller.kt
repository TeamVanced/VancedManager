package com.vanced.manager.installer.impl

import com.vanced.manager.installer.base.AppInstaller
import com.vanced.manager.preferences.holder.managerVariantPref
import com.vanced.manager.preferences.holder.vancedVersionPref
import com.xinto.apkhelper.installApk
import com.xinto.apkhelper.installSplitApks

class MicrogInstaller : AppInstaller() {

    override fun install(
        onDone: () -> Unit
    ) {
        super.install(onDone)

        installApk(
            apkPath = context.getExternalFilesDir("microg/microg.apk")!!.path,
            context = context
        )
    }

}