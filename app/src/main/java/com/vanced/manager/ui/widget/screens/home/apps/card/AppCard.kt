package com.vanced.manager.ui.widget.screens.home.apps.card

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.google.accompanist.glide.rememberGlidePainter
import com.vanced.manager.domain.model.App
import com.vanced.manager.ui.component.card.ManagerThemedCard
import com.vanced.manager.ui.component.layout.ManagerButtonColumn
import com.vanced.manager.ui.util.defaultContentPaddingHorizontal
import com.vanced.manager.ui.util.defaultContentPaddingVertical
import com.vanced.manager.ui.viewmodel.HomeViewModel
import com.vanced.manager.ui.widget.button.ManagerCancelButton
import com.vanced.manager.ui.widget.button.ManagerDownloadButton
import com.vanced.manager.ui.widget.screens.home.apps.dialog.AppChangelogDialog
import com.vanced.manager.ui.widget.screens.home.apps.dialog.AppDownloadDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppCard(
    app: App,
    viewModel: HomeViewModel
) {
    var showAppInfoDialog by rememberSaveable { mutableStateOf(false) }
    var showInstallationOptions by rememberSaveable { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope { Dispatchers.IO }

    val icon = rememberGlidePainter(
        request = app.iconUrl ?: "",
        fadeIn = true
    )

    val hasInstallationOptions = app.installationOptions != null
    val animationSpec = tween<IntSize>(400)

    val downloader = app.downloader

    val download: () -> Unit = {
        showInstallationOptions = false
        coroutineScope.launch {
            downloader!!.download(app, viewModel)
        }
    }

    Column {
        ManagerThemedCard {
            Column {
                AppInfoCard(
                    appName = app.name,
                    icon = icon,
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
                                download()
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
                        ManagerDownloadButton(
                            onClick = download
                        )
                        ManagerCancelButton {
                            showInstallationOptions = false
                        }
                    }
                }
            }
        }
    }

    if (app.name != null && downloader != null && downloader.showDownloadScreen.value) {
        AppDownloadDialog(
            app = app.name,
            downloader = downloader,
            onCancelClick = {
                downloader.cancelDownload()
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