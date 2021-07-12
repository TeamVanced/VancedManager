package com.vanced.manager.installer

import com.vanced.manager.installer.base.AppInstaller
import com.vanced.manager.preferences.holder.managerVariantPref
import com.vanced.manager.preferences.holder.vancedVersionPref
import com.xinto.apkhelper.installSplitApks

class MusicInstaller : AppInstaller() {

    override fun install(
        onDone: () -> Unit
    ) {
        super.install(onDone)

        val version by vancedVersionPref
        val variant by managerVariantPref

        installSplitApks(
            apksPath = context.getExternalFilesDir("vanced/$version/$variant")!!.path,
            context = context
        )
    }

}