package com.vanced.manager.installer

import androidx.compose.runtime.MutableState
import com.vanced.manager.installer.base.AppInstaller
import com.vanced.manager.ui.preferences.holder.managerVariantPref
import com.vanced.manager.ui.preferences.holder.vancedVersionPref
import com.vanced.manager.ui.viewmodel.HomeViewModel
import com.xinto.apkhelper.installSplitApks

class MicrogInstaller : AppInstaller() {

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