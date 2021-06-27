package com.vanced.manager.ui.widgets.home.apps.card

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Launch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanced.manager.R
import com.vanced.manager.ui.components.button.IconButton
import com.vanced.manager.ui.components.color.ThemedCardContentColorProvider
import com.vanced.manager.ui.components.text.ManagerText
import com.vanced.manager.ui.utils.defaultContentPaddingHorizontal
import com.vanced.manager.ui.components.text.AppVersionText

@Composable
fun AppActionCard(
    appRemoteVersion: String?,
    appInstalledVersion: String?,
    showDownloadDialog: MutableState<Boolean>,
    showAppInfo: MutableState<Boolean>,
    showInstallationOptions: MutableState<Boolean>,
    hasInstallationOptions: Boolean
) {
    Row(
        modifier = Modifier.padding(horizontal = defaultContentPaddingHorizontal, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.Start),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ManagerText(stringId = R.string.app_versions)
            AppVersionText(
                stringId = R.string.app_version_latest,
                version = appRemoteVersion
            )
            AppVersionText(
                stringId = R.string.app_version_installed,
                version = appInstalledVersion
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
            IconButton(icon = Icons.Rounded.DeleteForever, contentDescription = "Uninstall") {}
            IconButton(icon = Icons.Rounded.Launch, contentDescription = "Launch") {}
            IconButton(icon = Icons.Rounded.Download, contentDescription = "Install") {
                if (hasInstallationOptions) {
                    showInstallationOptions.value = true
                } else {
                    showDownloadDialog.value = true
                }
            }
        }
    }
}