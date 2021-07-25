package com.vanced.manager.installer.impl

import com.vanced.manager.installer.base.AppInstaller
import com.vanced.manager.preferences.holder.managerVariantPref
import com.vanced.manager.preferences.holder.musicVersionPref
import com.xinto.apkhelper.installApk

class MusicInstaller : AppInstaller() {

    override fun install(
        onDone: () -> Unit
    ) {
        super.install(onDone)

        val version by musicVersionPref
        val variant by managerVariantPref

        installApk(
            apkPath = context.getExternalFilesDir("music/$version/$variant/music.apk")!!.path,
            context = context
        )
    }

}