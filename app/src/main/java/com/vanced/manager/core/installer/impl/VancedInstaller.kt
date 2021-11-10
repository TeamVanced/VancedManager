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
        val absoluteVersion = getLatestOrProvidedAppVersion(vancedVersionPref, appVersions)

        val apks = context
            .getExternalFilesDir("vanced/$absoluteVersion/$managerVariantPref")!!
            .listFiles { file ->
                file.extension == "apk"
            }

        installSplitApp(apks!!, context)
    }

}