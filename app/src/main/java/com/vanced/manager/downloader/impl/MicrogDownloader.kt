package com.vanced.manager.downloader.impl

import com.vanced.manager.domain.model.App
import com.vanced.manager.downloader.api.MicrogAPI
import com.vanced.manager.downloader.base.AppDownloader
import com.vanced.manager.installer.impl.MicrogInstaller
import com.vanced.manager.ui.viewmodel.HomeViewModel

class MicrogDownloader(
    microgInstaller: MicrogInstaller,
    private val microgAPI: MicrogAPI,
) : AppDownloader(
    appName = "microg",
    appInstaller = microgInstaller
) {

    override suspend fun download(
        app: App,
        viewModel: HomeViewModel
    ) {
        downloadFile(
            file = File(
                call = microgAPI.getFile(),
                fileName = "microg.apk"
            ),
            viewModel = viewModel,
            folderStructure = ""
        )
    }

}