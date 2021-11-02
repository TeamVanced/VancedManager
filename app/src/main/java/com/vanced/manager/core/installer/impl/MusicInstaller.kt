package com.vanced.manager.core.installer.impl

import android.content.Context
import com.vanced.manager.core.installer.base.AppInstaller
import com.vanced.manager.core.installer.util.installApp
import com.vanced.manager.core.preferences.holder.managerVariantPref
import com.vanced.manager.core.preferences.holder.musicVersionPref
import java.io.File

class MusicInstaller(
    private val context: Context
) : AppInstaller() {

    override fun install(appVersions: List<String>?) {
        val version by musicVersionPref
        val variant by managerVariantPref

        val musicApk = File(context.getExternalFilesDir("music/$version/$variant/music.apk")!!.path)

        installApp(musicApk, context)
    }

}