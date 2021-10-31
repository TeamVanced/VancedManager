package com.vanced.manager.core.installer.base

import android.content.Context
import androidx.annotation.CallSuper
import com.vanced.manager.core.util.log
import com.xinto.apkhelper.statusCallback
import com.xinto.apkhelper.statusCallbackBuilder
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class AppInstaller : KoinComponent {

    val context: Context by inject()

    @CallSuper
    open fun install(
        onDone: () -> Unit
    ) {
        setStatusCallback(onDone = onDone)
    }

    private fun setStatusCallback(
        onDone: () -> Unit
    ) {
        statusCallback = statusCallbackBuilder(
            onInstall = { _, _ ->
                onDone()
            },
            onInstallFailed = { error, _, _ ->
                onDone()
                log("install", error)
            }
        )
    }

}