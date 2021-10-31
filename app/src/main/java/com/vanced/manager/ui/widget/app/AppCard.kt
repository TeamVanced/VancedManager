package com.vanced.manager.ui.widget.app

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Launch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.ImagePainter
import com.vanced.manager.R
import com.vanced.manager.ui.component.button.IconButton
import com.vanced.manager.ui.component.text.AppVersionText
import com.vanced.manager.ui.component.text.ManagerText

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppCard(
    appName: String,
    appIcon: ImagePainter,
    appInstalledVersion: String?,
    appRemoteVersion: String?,
    onDownloadClick: () -> Unit,
    onUninstallClick: () -> Unit,
    onLaunchClick: () -> Unit,
    onInfoClick: () -> Unit,
) {
    BaseAppCard(
        appTitle = {
            ManagerText(
                modifier = Modifier.fillMaxSize(),
                text = appName,
                textStyle = MaterialTheme.typography.h5
            )
        },
        appIcon = {
            Image(
                modifier = Modifier.size(48.dp),
                painter = appIcon,
                contentDescription = "App Icon",
            )
        },
        appVersionsColumn = {
            AppVersionText(
                text = stringResource(
                    id = R.string.app_version_latest,
                    appRemoteVersion ?: stringResource(
                        id = R.string.app_content_unavailable
                    )
                )
            )
            AppVersionText(
                text = stringResource(
                    id = R.string.app_version_installed,
                    appInstalledVersion ?: stringResource(
                        id = R.string.app_content_unavailable
                    )
                )
            )
        },
        appActionsRow = {
            IconButton(
                icon = Icons.Outlined.Info,
                contentDescription = "App Info",
                onClick = onInfoClick
            )
            IconButton(
                icon = Icons.Rounded.DeleteForever,
                contentDescription = "Uninstall",
                onClick = onUninstallClick
            )
            IconButton(
                icon = Icons.Rounded.Launch,
                contentDescription = "Launch",
                onClick = onLaunchClick
            )
            IconButton(
                icon = Icons.Rounded.Download,
                contentDescription = "Install",
                onClick = onDownloadClick
            )
        }
    )
}