package com.vanced.manager.downloader.impl

import com.vanced.manager.domain.model.App
import com.vanced.manager.downloader.base.AppDownloader
import com.vanced.manager.installer.MicrogInstaller
import com.vanced.manager.ui.viewmodel.HomeViewModel

class MicrogDownloader(
    microgInstaller: MicrogInstaller
) : AppDownloader(
    appName = "microg",
    appInstaller = microgInstaller
) {

    override suspend fun download(app: App, viewModel: HomeViewModel) {
        TODO("Not yet implemented")
    }

}