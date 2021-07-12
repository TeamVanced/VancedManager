package com.vanced.manager.downloader.impl

import com.vanced.manager.domain.model.App
import com.vanced.manager.downloader.base.AppDownloader
import com.vanced.manager.ui.viewmodel.HomeViewModel

class MicrogDownloader : AppDownloader("") {

    override suspend fun download(app: App, viewModel: HomeViewModel) {
        TODO("Not yet implemented")
    }

}