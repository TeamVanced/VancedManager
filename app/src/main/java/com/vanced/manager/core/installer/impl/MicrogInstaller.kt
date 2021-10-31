package com.vanced.manager.core.installer.impl

import com.vanced.manager.core.installer.base.AppInstaller
import com.xinto.apkhelper.installApk

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