package com.vanced.manager.ui.widget.app

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Launch
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.ImagePainter
import com.vanced.manager.R
import com.vanced.manager.ui.component.text.AppVersionText
import com.vanced.manager.ui.component.text.ManagerText

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppCard(
    appName: String,
    appIcon: ImagePainter,
    appInstalledVersion: String?,
    appRemoteVersion: String?,
    onAppDownloadClick: () -> Unit,
    onAppUninstallClick: () -> Unit,
    onAppLaunchClick: () -> Unit,
    onAppInfoClick: () -> Unit,
) {
    BaseAppCard(
        appTitle = {
            ManagerText(
                modifier = Modifier.fillMaxSize(),
                text = appName,
                textStyle = MaterialTheme.typography.titleMedium
            )
        },
        appIcon = {
            Image(
                modifier = Modifier.size(48.dp),
                painter = appIcon,
                contentDescription = "App Icon",
            )
        },
        appTrailing = {
            IconButton(onClick = onAppInfoClick) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "App Info"
                )
            }
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
            if (appInstalledVersion != null) {
                IconButton(onClick = onAppUninstallClick) {
                    Icon(
                        imageVector = Icons.Rounded.DeleteForever,
                        contentDescription = "Uninstall"
                    )
                }
                IconButton(onClick = onAppLaunchClick) {
                    Icon(
                        imageVector = Icons.Rounded.Launch,
                        contentDescription = "Launch",
                    )
                }
            }
            IconButton(onClick = onAppDownloadClick) {
                Icon(
                    imageVector = Icons.Rounded.Download,
                    contentDescription = "Install",
                )
            }
        }
    )
}