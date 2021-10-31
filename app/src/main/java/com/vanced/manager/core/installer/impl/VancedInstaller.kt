package com.vanced.manager.core.installer.impl

import com.vanced.manager.core.installer.base.AppInstaller
import com.vanced.manager.core.preferences.holder.managerVariantPref
import com.vanced.manager.core.preferences.holder.vancedVersionPref
import com.xinto.apkhelper.installSplitApks

class VancedInstaller : AppInstaller() {

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