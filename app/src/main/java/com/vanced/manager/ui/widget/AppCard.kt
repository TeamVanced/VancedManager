package com.vanced.manager.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.GetApp
import androidx.compose.material.icons.rounded.Launch
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.vanced.manager.R
import com.vanced.manager.domain.model.AppState
import com.vanced.manager.ui.component.ManagerElevatedCard
import com.vanced.manager.ui.component.ManagerListItem
import com.vanced.manager.ui.component.ManagerText
import com.vanced.manager.ui.theme.LargeShape
import com.vanced.manager.ui.theme.MediumShape
import com.vanced.manager.ui.theme.SmallShape
import com.vanced.manager.ui.util.DefaultContentPaddingHorizontal
import com.vanced.manager.ui.util.DefaultContentPaddingVertical

@Composable
fun AppCard(
    appName: String,
    appIcon: Painter,
    appInstalledVersion: String?,
    appRemoteVersion: String?,
    appState: AppState,
    onAppDownloadClick: () -> Unit,
    onAppUninstallClick: () -> Unit,
    onAppLaunchClick: () -> Unit,
    onAppInfoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BaseAppCard(
        modifier = modifier,
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
            ManagerText(
                text = stringResource(
                    id = R.string.app_version_latest,
                    appRemoteVersion ?: stringResource(
                        id = R.string.app_content_unavailable
                    )
                )
            )
            ManagerText(
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
                when (appState) {
                    AppState.NOT_INSTALLED -> {
                        Icon(
                            imageVector = Icons.Rounded.GetApp,
                            contentDescription = "Install",
                        )
                    }
                    AppState.INSTALLED -> {
                        Icon(
                            imageVector = Icons.Rounded.GetApp,
                            contentDescription = "Install",
                        )
                    }
                    AppState.NEEDS_UPDATE -> {
                        Icon(
                            imageVector = Icons.Rounded.Update,
                            contentDescription = "Update",
                        )
                    }
                }

            }
        }
    )
}

@Composable
fun AppCardPlaceholder(
    modifier: Modifier = Modifier
) {
    BaseAppCard(
        modifier = modifier,
        appTitle = {
            ManagerText(
                modifier = Modifier
                    .managerPlaceholder(
                        visible = true,
                        shape = MediumShape
                    ),
                text = " ".repeat(40),
                textStyle = MaterialTheme.typography.titleMedium
            )
        },
        appIcon = {
            Box(
                modifier = Modifier
                    .managerPlaceholder(
                        visible = true,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .size(48.dp)
            )
        },
        appTrailing = {
            Box(
                modifier = Modifier
                    .managerPlaceholder(
                        visible = true,
                        shape = MediumShape
                    )
                    .size(24.dp)
            )
        },
        appVersionsColumn = {
            ManagerText(
                modifier = Modifier
                    .managerPlaceholder(
                        visible = true,
                        shape = SmallShape
                    ),
                text = " ".repeat(30)
            )
            ManagerText(
                modifier = Modifier
                    .managerPlaceholder(
                        visible = true,
                        shape = SmallShape
                    ),
                text = " ".repeat(30)
            )
        },
        appActionsRow = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(36.dp)
                    .managerPlaceholder(
                        visible = true,
                        shape = MediumShape
                    )
            )
        }
    )
}

@Composable
private fun BaseAppCard(
    appTitle: @Composable () -> Unit,
    appIcon: @Composable () -> Unit,
    appTrailing: @Composable () -> Unit,
    appVersionsColumn: @Composable ColumnScope.() -> Unit,
    appActionsRow: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    ManagerElevatedCard(
        modifier = modifier,
        shape = LargeShape,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = DefaultContentPaddingHorizontal,
                    vertical = DefaultContentPaddingVertical
                ),
            verticalArrangement = Arrangement
                .spacedBy(DefaultContentPaddingVertical)
        ) {
            ManagerListItem(
                modifier = Modifier.fillMaxWidth(),
                title = appTitle,
                icon = appIcon,
                trailing = appTrailing
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(LargeShape),
                thickness = 2.dp,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    ManagerText(
                        text = stringResource(id = R.string.app_versions),
                        textStyle = MaterialTheme.typography.bodyMedium
                    )
                    ProvideTextStyle(value = MaterialTheme.typography.bodySmall) {
                        appVersionsColumn()
                    }
                }
                Row(
                    modifier = Modifier.wrapContentWidth(),
                    content = appActionsRow,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                )
            }
        }
    }
}

private fun Modifier.managerPlaceholder(
    visible: Boolean,
    shape: Shape
) = composed {
    placeholder(
        visible = visible,
        shape = shape,
        color = MaterialTheme.colorScheme.surfaceVariant,
        highlight = PlaceholderHighlight.shimmer(
            highlightColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}