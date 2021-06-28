package com.vanced.manager.ui.widgets.home.apps.card

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
    val showDownloadDialog = remember { mutableStateOf(false) }
    val showAppInfo = remember { mutableStateOf(false) }
    val showInstallationOptions = remember { mutableStateOf(false) }
    val icon = rememberGlidePainter(
        request = app.iconUrl ?: "",
        fadeIn = true
    )
    val hasInstallationOption = app.installationOptions != null

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
                        showDownloadDialog = showDownloadDialog,
                        showAppInfo = showAppInfo,
                        showInstallationOptions = showInstallationOptions,
                        hasInstallationOptions = hasInstallationOption
                    )
                }
            }
        }
        if (hasInstallationOption) {
            AnimatedVisibility(
                modifier = Modifier.fillMaxWidth(),
                visible = showInstallationOptions.value,
                enter = expandVertically(
                    animationSpec = tween(400)
                ),
                exit = shrinkVertically(
                    animationSpec = tween(400)
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
                            showDownloadDialog.value = true
                        }
                        ManagerCancelButton {
                            showInstallationOptions.value = false
                        }
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