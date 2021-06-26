package com.vanced.manager.installer

import android.content.Context
import com.vanced.manager.ui.viewmodel.HomeViewModel
import com.xinto.apkhelper.installSplitApks
import com.xinto.apkhelper.statusCallback
import com.xinto.apkhelper.statusCallbackBuilder

class AppInstaller(
    private val context: Context,
    private val viewModel: HomeViewModel
) {

    fun installVanced(version: String) {
        installSplitApks(context.getExternalFilesDir("vanced/$version")!!.path, context)
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