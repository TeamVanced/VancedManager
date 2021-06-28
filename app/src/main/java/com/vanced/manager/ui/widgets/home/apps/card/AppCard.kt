package com.vanced.manager.ui.widgets.home.apps.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.google.accompanist.glide.rememberGlidePainter
import com.vanced.manager.domain.model.App
import com.vanced.manager.ui.components.card.ManagerThemedCard
import com.vanced.manager.ui.components.layout.ManagerButtonColumn
import com.vanced.manager.ui.utils.defaultContentPaddingHorizontal
import com.vanced.manager.ui.utils.defaultContentPaddingVertical
import com.vanced.manager.ui.widgets.button.ManagerCancelButton
import com.vanced.manager.ui.widgets.button.ManagerDownloadButton
import com.vanced.manager.ui.widgets.home.apps.dialog.AppChangelogDialog
import com.vanced.manager.ui.widgets.home.apps.dialog.AppDownloadDialog

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppCard(
    app: App,
    fetching: Boolean
) {
    var showDownloadDialog by remember { mutableStateOf(false) }
    var showAppInfoDialog by remember { mutableStateOf(false) }
    var showInstallationOptions by remember { mutableStateOf(false) }

    val icon = rememberGlidePainter(
        request = app.iconUrl ?: "",
        fadeIn = true
    )

    val hasInstallationOptions = app.installationOptions != null
    val animationSpec = tween<IntSize>(400)

    fun download() {

    }

    Column {
        ManagerThemedCard {
            Column {
                AppInfoCard(
                    appName = app.name ?: "",
                    icon = icon,
                    fetching = fetching
                )
                Column(
                    modifier = Modifier.padding(vertical = defaultContentPaddingVertical)
                ) {
                    AppActionCard(
                        appInstalledVersion = app.installedVersion,
                        appRemoteVersion = app.remoteVersion,
                        onDownloadClick = {
                            if (hasInstallationOptions) {
                                showInstallationOptions = true
                            } else {
                                showDownloadDialog = true
                            }
                        },
                        onInfoClick = {
                            showAppInfoDialog = true
                        },
                        onLaunchClick = {},
                        onUninstallClick = {}
                    )
                }
            }
        }
        if (hasInstallationOptions) {
            AnimatedVisibility(
                modifier = Modifier.fillMaxWidth(),
                visible = showInstallationOptions,
                enter = expandVertically(
                    animationSpec = animationSpec
                ),
                exit = shrinkVertically(
                    animationSpec = animationSpec
                )
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    app.installationOptions?.forEach {
                        it.item()
                    }
                    ManagerButtonColumn(
                        modifier = Modifier
                            .padding(horizontal = defaultContentPaddingHorizontal)
                            .padding(top = defaultContentPaddingVertical)
                    ) {
                        ManagerDownloadButton {
                            showDownloadDialog = true
                        }
                        ManagerCancelButton {
                            showInstallationOptions = false
                        }
                    }
                }
            }
        }
    }

    if (app.name != null && app.downloader != null && showDownloadDialog) {
        AppDownloadDialog(
            app = app.name,
            downloader = app.downloader,
            onCancelClick = {
                showDownloadDialog = false
            }
        )
    }

    if (app.name != null && app.changelog != null && showAppInfoDialog) {
        AppChangelogDialog(
            appName = app.name,
            changelog = app.changelog,
            onDismissRequest = {
                showAppInfoDialog = false
            }
        )
    }
}