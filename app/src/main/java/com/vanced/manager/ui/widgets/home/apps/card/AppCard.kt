package com.vanced.manager.ui.widgets.home.apps.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.accompanist.glide.rememberGlidePainter
import com.vanced.manager.domain.model.App
import com.vanced.manager.ui.components.button.ManagerThemedButton
import com.vanced.manager.ui.components.card.ManagerThemedCard
import com.vanced.manager.ui.widgets.home.apps.dialog.AppChangelogDialog
import com.vanced.manager.ui.widgets.home.apps.dialog.AppDownloadDialog

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppCard(
    app: App,
    fetching: Boolean
) {
    val showDownloadDialog = remember { mutableStateOf(false) }
    val showAppInfo = remember { mutableStateOf(false) }
    val showInstallationOptions = remember { mutableStateOf(false) }
    val icon = rememberGlidePainter(
        request = app.iconUrl ?: "",
        fadeIn = true
    )
    val hasInstallationOption = app.installationOptions != null

    ManagerThemedCard {
        Column {
            AppInfoCard(
                appName = app.name ?: "",
                icon = icon,
                fetching = fetching
            )
            AppActionCard(
                appInstalledVersion = app.installedVersion,
                appRemoteVersion = app.remoteVersion,
                showDownloadDialog = showDownloadDialog,
                showAppInfo = showAppInfo,
                showInstallationOptions = showInstallationOptions,
                hasInstallationOptions = hasInstallationOption
            )
            if (hasInstallationOption) {
                AnimatedVisibility(
                    modifier = Modifier.fillMaxWidth(),
                    visible = showInstallationOptions.value
                ) {
                    app.installationOptions?.forEach {
                        it.item()
                    }
                    ManagerThemedButton(onClick = {
                        showDownloadDialog.value = true
                    }) {

                    }
                }
            }
        }
    }

    if (app.name != null && app.downloader != null) {
        AppDownloadDialog(
            app = app.name,
            downloader = app.downloader,
            showDialog = showDownloadDialog
        )
    }

    if (app.name != null && app.changelog != null) {
        AppChangelogDialog(
            appName = app.name,
            changelog = app.changelog,
            showDialog = showAppInfo
        )
    }
}