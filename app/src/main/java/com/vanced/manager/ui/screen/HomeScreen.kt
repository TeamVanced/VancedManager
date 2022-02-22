package com.vanced.manager.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.vanced.manager.R
import com.vanced.manager.core.util.socialMedia
import com.vanced.manager.core.util.sponsors
import com.vanced.manager.domain.model.App
import com.vanced.manager.domain.model.InstallationOption
import com.vanced.manager.ui.component.*
import com.vanced.manager.ui.resource.managerString
import com.vanced.manager.ui.util.Screen
import com.vanced.manager.ui.viewmodel.MainViewModel
import com.vanced.manager.ui.widget.AppCard
import com.vanced.manager.ui.widget.AppCardPlaceholder
import com.vanced.manager.ui.widget.LinkCard

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onToolbarScreenSelected: (Screen) -> Unit,
    onAppDownloadClick: (
        appName: String,
        appVersions: List<String>?,
        installationOptions: List<InstallationOption>?
    ) -> Unit,
    onAppUninstallClick: (appPackage: String) -> Unit,
    onAppLaunchClick: (appName: String, appPackage: String) -> Unit,
) {
    val refreshState =
        rememberSwipeRefreshState(isRefreshing = viewModel.appState.isFetching)
    var menuExpanded by remember { mutableStateOf(false) }
    val dropdownScreens = remember { listOf(Screen.Settings, Screen.About) }

    ManagerScaffold(
        topBar = {
            HomeScreenTopBar(
                modifier = Modifier,
                menuExpanded = menuExpanded,
                dropdownScreens = dropdownScreens,
                onActionClick = {
                    menuExpanded = true
                },
                onDropdownItemClick = onToolbarScreenSelected,
                onDropdownDismissRequest = {
                    menuExpanded = false
                })
        }
    ) { paddingValues ->
        ManagerSwipeRefresh(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            swipeRefreshState = refreshState,
            onRefresh = { viewModel.fetch() }
        ) {
            AnimatedContent(
                modifier = Modifier.fillMaxSize(),
                targetState = viewModel.appState,
                transitionSpec = {
                    scaleIn(initialScale = 0.9f) + fadeIn() with
                            scaleOut(targetScale = 0.9f) + fadeOut()
                }
            ) { animatedAppState ->
                when (animatedAppState) {
                    is StateFetching -> {
                        HomeScreenLoading(
                            modifier = Modifier.fillMaxSize(),
                            appsCount = animatedAppState.placeholderAppsCount
                        )
                    }
                    is StateSuccess -> {
                        HomeScreenLoaded(
                            modifier = Modifier.fillMaxSize(),
                            apps = animatedAppState.apps,
                            onAppDownloadClick = onAppDownloadClick,
                            onAppUninstallClick = onAppUninstallClick,
                            onAppLaunchClick = onAppLaunchClick
                        )
                    }
                    is StateError -> {
                        //TODO
                    }
                }
            }
        }
    }
}

typealias StateFetching = MainViewModel.AppState.Fetching
typealias StateSuccess = MainViewModel.AppState.Success
typealias StateError = MainViewModel.AppState.Error

@Composable
private fun HomeScreenTopBar(
    menuExpanded: Boolean,
    dropdownScreens: List<Screen>,
    onActionClick: () -> Unit,
    onDropdownItemClick: (Screen) -> Unit,
    onDropdownDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    ManagerCenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            ManagerText(managerString(Screen.Home.displayName))
        },
        actions = {
            IconButton(onClick = onActionClick) {
                Icon(
                    Icons.Rounded.MoreVert,
                    contentDescription = "Navigation"
                )
            }

            ManagerDropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = onDropdownDismissRequest
            ) {
                for (dropdownScreen in dropdownScreens) {
                    ManagerDropdownMenuItem(
                        title = managerString(dropdownScreen.displayName),
                        onClick = {
                            onDropdownItemClick(dropdownScreen)
                        }
                    )
                }
            }
        }
    )
}

@Composable
private fun HomeScreenLoaded(
    modifier: Modifier = Modifier,
    apps: List<App>,
    onAppDownloadClick: (
        appName: String,
        appVersions: List<String>?,
        installationOptions: List<InstallationOption>?
    ) -> Unit,
    onAppUninstallClick: (appPackage: String) -> Unit,
    onAppLaunchClick: (appName: String, appPackage: String) -> Unit,
) {
    HomeScreenBody(modifier = modifier) {
        managerCategory(categoryName = {
            managerString(R.string.home_category_apps)
        }) {
            items(apps) { app ->
                val appIcon = rememberImagePainter(app.iconUrl) {
                    diskCachePolicy(CachePolicy.ENABLED)
                }

                var showAppInfoDialog by remember { mutableStateOf(false) }

                AppCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    appName = app.name,
                    appIcon = appIcon,
                    appInstalledVersion = app.installedVersion,
                    appRemoteVersion = app.remoteVersion,
                    onAppDownloadClick = {
                        onAppDownloadClick(
                            app.name,
                            app.versions,
                            app.installationOptions
                        )
                    },
                    onAppUninstallClick = {
                        onAppUninstallClick(
                            app.packageName
                        )
                    },
                    onAppLaunchClick = {
                        onAppLaunchClick(
                            app.name,
                            app.packageName,
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
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeScreenLoading(
    modifier: Modifier = Modifier,
    appsCount: Int,
) {
    HomeScreenBody(modifier = modifier) {
        managerCategory(categoryName = {
            managerString(R.string.home_category_apps)
        }) {
            items(appsCount) {
                AppCardPlaceholder(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private inline fun HomeScreenBody(
    modifier: Modifier = Modifier,
    crossinline appsCategory: LazyListScope.() -> Unit,
) {
    ManagerLazyColumn(modifier = modifier) {
        appsCategory()
        managerCategory(categoryName = {
            managerString(R.string.home_category_support_us)
        }) {
            item {
                ManagerLazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(sponsors) { sponsor ->
                        LinkCard(
                            text = sponsor.title,
                            icon = painterResource(sponsor.icon),
                            url = sponsor.link
                        )
                    }
                }
            }
        }
        managerCategory(categoryName = {
            managerString(R.string.home_category_social_media)
        }) {
            item {
                ManagerLazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(socialMedia) { socialMedia ->
                        LinkCard(
                            text = socialMedia.title,
                            icon = painterResource(socialMedia.icon),
                            url = socialMedia.link
                        )
                    }
                }
            }
        }
    }
}
