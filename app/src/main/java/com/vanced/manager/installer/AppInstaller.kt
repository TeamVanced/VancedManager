package com.vanced.manager.installer

import com.xinto.apkhelper.statusCallback
import com.xinto.apkhelper.statusCallbackBuilder
import com.vanced.manager.ui.viewmodel.HomeViewModel

class AppInstaller(
    private val viewModel: HomeViewModel
) {

    fun installVanced() {

    }

    fun installMusic() {

    }

    fun installMicrog() {

    }

    init {
        statusCallback = statusCallbackBuilder(
            onAction = {
                viewModel.fetch()
            }
        )
    }

}