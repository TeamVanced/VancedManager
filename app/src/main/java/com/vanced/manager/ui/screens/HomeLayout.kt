package com.vanced.manager.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.vanced.manager.R
import com.vanced.manager.core.util.socialMedia
import com.vanced.manager.core.util.sponsors
import com.vanced.manager.domain.model.InstallationOption
import com.vanced.manager.ui.component.card.ManagerLinkCard
import com.vanced.manager.ui.component.dialog.ManagerDialog
import com.vanced.manager.ui.component.layout.ManagerLazyColumn
import com.vanced.manager.ui.component.layout.ManagerScaffold
import com.vanced.manager.ui.component.layout.ManagerSwipeRefresh
import com.vanced.manager.ui.component.layout.ScrollableItemRow
import com.vanced.manager.ui.component.menu.ManagerDropdownMenu
import com.vanced.manager.ui.component.menu.ManagerDropdownMenuItem
import com.vanced.manager.ui.component.text.ManagerText
import com.vanced.manager.ui.component.topappbar.ManagerTopAppBar
import com.vanced.manager.ui.resources.managerString
import com.vanced.manager.ui.util.DefaultContentPaddingHorizontal
import com.vanced.manager.ui.util.Screen
import com.vanced.manager.ui.viewmodel.MainViewModel
import com.vanced.manager.ui.widget.app.AppCard
import com.vanced.manager.ui.widget.app.AppCardPlaceholder
import com.vanced.manager.ui.widget.layout.managerCategory

@ExperimentalMaterial3Api
@ExperimentalAnimationApi
@Composable
fun HomeLayout(
    viewModel: MainViewModel,
    onToolbarScreenSelected: (Screen) -> Unit,
    onAppInstallPress: (
        appName: String,
        appVersions: List<String>?,
        installationOptions: List<InstallationOption>?
    ) -> Unit
) {
    val appState = viewModel.appState

    val refreshState =
        rememberSwipeRefreshState(isRefreshing = appState is MainViewModel.AppState.Fetching)
    var isMenuExpanded by remember { mutableStateOf(false) }
    val dropdownScreens = remember { listOf(Screen.Settings, Screen.About) }

    val homeCategoryApps = managerString(R.string.home_category_apps)
    val homeCategorySupportUs = managerString(R.string.home_category_support_us)
    val homeCategorySocialMedia = managerString(R.string.home_category_social_media)

    ManagerScaffold(
        topBar = {
            ManagerTopAppBar(
                title = managerString(Screen.Home.displayName),
                actions = {
                    IconButton(onClick = { isMenuExpanded = true }) {
                        Icon(
                            Icons.Rounded.MoreVert,
                            contentDescription = "Navigation"
                        )
                    }

                    ManagerDropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = {
                            isMenuExpanded = false
                        }
                    ) {
                        for (dropdownScreen in dropdownScreens) {
                            ManagerDropdownMenuItem(
                                title = managerString(dropdownScreen.displayName),
                                onClick = {
                                    onToolbarScreenSelected(dropdownScreen)
                                    isMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        ManagerSwipeRefresh(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            refreshState = refreshState,
            onRefresh = { viewModel.fetch() }
        ) {
            ManagerLazyColumn {
                managerCategory(homeCategoryApps) {
                    AnimatedContent(
                        targetState = appState,
                        transitionSpec = {
                            scaleIn(initialScale = 0.9f) + fadeIn() with
                                    scaleOut(targetScale = 0.9f) + fadeOut()
                        }
                    ) { animatedAppState ->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            when (animatedAppState) {
                                is MainViewModel.AppState.Success -> {
                                    for (app in animatedAppState.apps) {
                                        val appIcon = rememberImagePainter(app.iconUrl) {
                                            diskCachePolicy(CachePolicy.ENABLED)
                                        }

                                        var showAppInfoDialog by rememberSaveable {
                                            mutableStateOf(
                                                false
                                            )
                                        }

                                        AppCard(
                                            appName = app.name,
                                            appIcon = appIcon,
                                            appInstalledVersion = app.installedVersion,
                                            appRemoteVersion = app.remoteVersion,
                                            onAppDownloadClick = {
                                                onAppInstallPress(
                                                    app.name,
                                                    app.versions,
                                                    app.installationOptions
                                                )
                                            },
                                            onAppUninstallClick = {
                                                viewModel.uninstallApp(
                                                    appPackage = app.packageName
                                                )
                                            },
                                            onAppLaunchClick = {
                                                viewModel.launchApp(
                                                    appName = app.name,
                                                    appPackage = app.packageName,
                                                    appPackageRoot = app.packageNameRoot
                                                )
                                            },
                                            onAppInfoClick = {
                                                showAppInfoDialog = true
                                            }
                                        )

                                        if (showAppInfoDialog) {
                                            ManagerDialog(
                                                title = managerString(
                                                    R.string.app_info_title,
                                                    app.name
                                                ),
                                                onDismissRequest = { showAppInfoDialog = false },
                                                confirmButton = {
                                                    TextButton(onClick = {
                                                        showAppInfoDialog = false
                                                    }) {
                                                        ManagerText(text = managerString(R.string.dialog_button_close))
                                                    }
                                                },
                                            ) {
                                                ManagerText(
                                                    modifier = Modifier.padding(top = 4.dp),
                                                    text = app.changelog,
                                                    //textStyle = MaterialTheme.typography.bodyMedium
                                                )
                                            }
                                        }
                                    }
                                }
                                is MainViewModel.AppState.Fetching -> {
                                    for (i in 0 until animatedAppState.placeholderAppsCount) {
                                        AppCardPlaceholder()
                                    }
                                }
                                is MainViewModel.AppState.Error -> {

                                }
                            }
                        }
                    }
                }
                managerCategory(homeCategorySupportUs) {
                    ScrollableItemRow(
                        modifier = Modifier
                            .fillMaxWidth(),
                        items = sponsors
                    ) { sponsor ->
                        ManagerLinkCard(
                            icon = sponsor.icon,
                            title = sponsor.title,
                            link = sponsor.link
                        )
                    }
                }
                managerCategory(homeCategorySocialMedia) {
                    ScrollableItemRow(
                        modifier = Modifier
                            .fillMaxWidth(),
                        items = socialMedia
                    ) { socialMedia ->
                        ManagerLinkCard(
                            icon = socialMedia.icon,
                            title = socialMedia.title,
                            link = socialMedia.link
                        )
                    }
                }
            }
        }
    }
}