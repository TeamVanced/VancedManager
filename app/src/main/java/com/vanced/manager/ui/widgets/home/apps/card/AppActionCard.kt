package com.vanced.manager.ui.widgets.home.apps.card

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Launch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vanced.manager.R
import com.vanced.manager.ui.components.button.IconButton
import com.vanced.manager.ui.components.text.AppVersionText
import com.vanced.manager.ui.components.text.ManagerText
import com.vanced.manager.ui.utils.defaultContentPaddingHorizontal

@Composable
fun AppActionCard(
    appRemoteVersion: String?,
    appInstalledVersion: String?,
    onInfoClick: () -> Unit,
    onUninstallClick: () -> Unit,
    onLaunchClick: () -> Unit,
    onDownloadClick: () -> Unit,
) {
    Row(
        modifier = Modifier.padding(
            horizontal = defaultContentPaddingHorizontal
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.Start),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ManagerText(
                text = stringResource(id = R.string.app_versions)
            )
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
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .wrapContentWidth(Alignment.End)
        ) {
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
    }
}