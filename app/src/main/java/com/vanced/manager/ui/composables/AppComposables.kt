package com.vanced.manager.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.glide.rememberGlidePainter
import com.vanced.manager.R
import com.vanced.manager.domain.model.App
import com.vanced.manager.downloader.base.BaseDownloader
import com.vanced.manager.ui.theme.managerAccentColor

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppCard(
    app: App,
    installationOptions: List<InstallationOption>? = null
) {
    val showDownloadDialog = remember { mutableStateOf(false) }
    val showAppInfo = remember { mutableStateOf(false) }
    val showInstallationOptions = remember { mutableStateOf(false) }
    val icon = rememberGlidePainter(
        request = app.iconUrl ?: "",
        requestBuilder = {
            placeholder(R.drawable.ic_app_icon_placeholder)
        },
        fadeIn = true
    )
    ManagerCard {
        Column {
            AppInfoCard(
                appName = app.name,
                icon = icon
            )
            AppActionCard(
                appInstalledVersion = app.installedVersion,
                appRemoteVersion = app.remoteVersion,
                showDownloadDialog = showDownloadDialog,
                showAppInfo = showAppInfo,
                showInstallationOptions = showInstallationOptions
            )
            if (installationOptions != null) {
                AnimatedVisibility(
                    modifier = Modifier.fillMaxWidth(),
                    visible = showInstallationOptions.value
                ) {
                    installationOptions.forEach {
                        it.Content()
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
        ChangeLogDialog(
            appName = app.name,
            changelog = app.changelog,
            icon = icon,
            show = showAppInfo
        )
    }
}

@Composable
fun AppInfoCard(
    appName: String?,
    icon: Painter
) {
    ManagerListItem(
        title = appName ?: "",
        icon = {
            Image(
                painter = icon,
                contentDescription = "",
                modifier = Modifier.size(48.dp, 48.dp),
            )
        },
        topPadding = 8.dp,
        bottomPadding = 8.dp
    )
}

@Composable
fun AppActionCard(
    appRemoteVersion: String?,
    appInstalledVersion: String?,
    showDownloadDialog: MutableState<Boolean>,
    showAppInfo: MutableState<Boolean>,
    showInstallationOptions: MutableState<Boolean>
) {
    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colors.managerAccentColor) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .background(managerAccentColor().copy(alpha = 0.15f)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
            ) {
                Text(
                    text = "Latest: ${appRemoteVersion ?: "Unavailable"}",
                    fontSize = 12.sp
                )
                Text(
                    text = "Installed: ${appInstalledVersion ?: "Unavailable"}",
                    fontSize = 12.sp
                )
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
                    .wrapContentWidth(Alignment.End)
            ) {
                IconButton(icon = Icons.Outlined.Info, contentDescription = "App Info") {
                    showAppInfo.value = true
                }
                IconButton(icon = Icons.Default.DeleteForever, contentDescription = "Uninstall") {}
                IconButton(icon = Icons.Default.Launch, contentDescription = "Launch") {}
                IconButton(icon = Icons.Default.Download, contentDescription = "Install") {
                    showInstallationOptions.value = true
                }
            }
        }
    }
}

@Composable
fun AppDownloadDialog(
    app: String,
    downloader: BaseDownloader,
    showDialog: MutableState<Boolean>
) {
    val coroutineScope = rememberCoroutineScope()

    val rememberProgress = remember { downloader.downloadProgress }
    val rememberFile = remember { downloader.downloadFile }
    val rememberInstalling = remember { downloader.installing }

    val showProgress = remember { mutableStateOf(false) }
    ManagerDialog(title = app, isShown = showDialog, buttons = {
        AppDownloadDialogButtons(
            showProgress = showProgress,
            showDialog = showDialog,
            downloader = downloader,
            coroutineScope = coroutineScope
        )
    }) {
        AppDownloadDialogProgress(
            progress = rememberProgress,
            file = rememberFile,
            showProgress = showProgress.value,
            installing = rememberInstalling
        )
    }
}

@Composable
fun ChangeLogDialog(
    appName: String,
    changelog: String,
    icon: Painter,
    show: MutableState<Boolean>
) {
    if (show.value) {
        ManagerDialog(
            title = "About $appName",
            isShown = show,
            buttons = {
                ManagerThemedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { show.value = false }
                ) {
                    Text(text = "Close")
                }
            }
        ) {
            Image(
                painter = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.CenterHorizontally)
            )
            HeaderView(
                modifier = Modifier.padding(horizontal = 8.dp),
                headerName = "Changelog",
                headerPadding = 0.dp
            ) {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = changelog,
                    fontSize = 14.sp
                )
            }
        }
    }
}


