package com.vanced.manager.core.installer.impl

import android.content.Context
import com.vanced.manager.core.installer.base.AppInstaller
import com.vanced.manager.core.installer.util.installSplitApp
import com.vanced.manager.core.preferences.holder.managerVariantPref
import com.vanced.manager.core.preferences.holder.vancedVersionPref
import com.vanced.manager.core.util.getLatestOrProvidedAppVersion

class VancedInstaller(
    private val context: Context
) : AppInstaller() {

    override fun install(appVersions: List<String>?) {
        val version by vancedVersionPref
        val variant by managerVariantPref

        val absoluteVersion = getLatestOrProvidedAppVersion(version, appVersions)

        val apks = context
            .getExternalFilesDir("vanced/$absoluteVersion/$variant")!!
            .listFiles { file ->
                file.extension == "apk"
            }

        installSplitApp(apks!!, context)
    }

}