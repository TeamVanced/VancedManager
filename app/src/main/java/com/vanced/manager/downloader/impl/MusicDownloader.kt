package com.vanced.manager.downloader.impl

import com.vanced.manager.domain.model.App
import com.vanced.manager.downloader.api.MusicAPI
import com.vanced.manager.downloader.base.AppDownloader
import com.vanced.manager.installer.impl.MusicInstaller
import com.vanced.manager.preferences.holder.managerVariantPref
import com.vanced.manager.preferences.holder.musicVersionPref
import com.vanced.manager.ui.viewmodel.HomeViewModel

class MusicDownloader(
    musicInstaller: MusicInstaller,
    private val musicAPI: MusicAPI,
) : AppDownloader(
    appName = "music",
    appInstaller = musicInstaller
) {

    private val version by musicVersionPref
    private val variant by managerVariantPref

    override suspend fun download(
        app: App,
        viewModel: HomeViewModel
    ) {
        downloadFile(
            file = File(
                call = musicAPI.getFiles(
                    version = version,
                    variant = variant,
                ),
                fileName = "music.apk"
            ),
            viewModel = viewModel,
            folderStructure = "$version/$variant"
        )
    }

}