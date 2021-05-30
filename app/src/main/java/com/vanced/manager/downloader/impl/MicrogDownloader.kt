package com.vanced.manager.downloader.impl

import com.vanced.manager.downloader.base.BaseDownloader
import com.vanced.manager.ui.composables.InstallationOption

object MicrogDownloader : BaseDownloader("") {

    override suspend fun download() {

    }

    override val installationOptions: List<InstallationOption>
        get() = TODO("Not yet implemented")

}