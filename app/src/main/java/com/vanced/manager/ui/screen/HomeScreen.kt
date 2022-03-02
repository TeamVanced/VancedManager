package com.vanced.manager.ui.screen

import androidx.annotation.DrawableRes
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
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.vanced.manager.R
import com.vanced.manager.domain.model.App
import com.vanced.manager.network.util.*
import com.vanced.manager.ui.component.*
import com.vanced.manager.ui.resource.managerString
import com.vanced.manager.ui.util.Screen
import com.vanced.manager.ui.viewmodel.ManagerState
import com.vanced.manager.ui.widget.AppCard
import com.vanced.manager.ui.widget.AppCardPlaceholder
import com.vanced.manager.ui.widget.LinkCard

@Composable
fun HomeScreen(
    managerState: ManagerState,
    onRefresh: () -> Unit,
    onToolbarScreenSelected: (Screen) -> Unit,
    onAppDownloadClick: (App) -> Unit,
    onAppUninstallClick: (App) -> Unit,
    onAppLaunchClick: (App) -> Unit,
) {
    val refreshState =
        rememberSwipeRefreshState(isRefreshing = managerState.isFetching)
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
            onRefresh = onRefresh
        ) {
            AnimatedContent(
                modifier = Modifier.fillMaxSize(),
                targetState = managerState,
                transitionSpec = {
                    scaleIn(initialScale = 0.9f) + fadeIn() with
                            scaleOut(targetScale = 0.9f) + fadeOut()
                }
            ) { animatedAppState ->
                when (animatedAppState) {
                    is ManagerState.Fetching -> {
                        HomeScreenLoading(
                            modifier = Modifier.fillMaxSize(),
                            appsCount = animatedAppState.placeholderAppsCount
                        )
                    }
                    is ManagerState.Success -> {
                        HomeScreenLoaded(
                            modifier = Modifier.fillMaxSize(),
                            apps = animatedAppState.apps,
                            onAppDownloadClick = onAppDownloadClick,
                            onAppUninstallClick = onAppUninstallClick,
                            onAppLaunchClick = onAppLaunchClick
                        )
                    }
                    is ManagerState.Error -> {
                        //TODO
                    }
                }
            }
        }
    }
}

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
    onAppDownloadClick: (App) -> Unit,
    onAppUninstallClick: (App) -> Unit,
    onAppLaunchClick: (App) -> Unit,
) {
    HomeScreenBody(modifier = modifier) {
        managerCategory(categoryName = {
            managerString(R.string.home_category_apps)
        }) {
            items(apps) { app ->
                val appIcon = painterResource(id = app.iconResId)

                var showAppInfoDialog by remember { mutableStateOf(false) }

                AppCard(
                    modifier = Modifier.fillMaxWidth(),
                    appName = app.name,
                    appIcon = appIcon,
                    appInstalledVersion = app.installedVersionName,
                    appRemoteVersion = app.remoteVersionName,
                    appState = app.state,
                    onAppDownloadClick = {
                        onAppDownloadClick(app)
                    },
                    onAppUninstallClick = {
                        onAppUninstallClick(app)
                    },
                    onAppLaunchClick = {
                        onAppLaunchClick(app)
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

data class Link(
    val title: String,
    val link: String,
    @DrawableRes val icon: Int
)


val sponsors = listOf(
    Link(
        title = "Brave",
        link = URL_SPONSOR_BRAVE,
        icon = R.drawable.ic_brave
    ),
    Link(
        title = "Adguard",
        link = URL_SPONSOR_ADGUARD,
        icon = R.drawable.ic_adguard
    )
)

val socialMedia = listOf(
    Link(
        title = "Instagram",
        link = URL_MEDIA_INSTAGRAM,
        icon = R.drawable.ic_instagram
    ),
    Link(
        title = "YouTube",
        link = URL_MEDIA_YOUTUBE,
        icon = R.drawable.ic_youtube
    ),
    Link(
        title = "GitHub",
        link = URL_MEDIA_GITHUB,
        icon = R.drawable.ic_github
    ),
    Link(
        title = "Website",
        link = URL_MEDIA_WEBSITE,
        icon = R.drawable.ic_website
    ),
    Link(
        title = "Telegram",
        link = URL_MEDIA_TELEGRAM,
        icon = R.drawable.ic_telegram
    ),
    Link(
        title = "Twitter",
        link = URL_MEDIA_TWITTER,
        icon = R.drawable.ic_twitter
    ),
    Link(
        title = "Discord",
        link = URL_MEDIA_DISCORD,
        icon = R.drawable.ic_discord
    ),
    Link(
        title = "Reddit",
        link = URL_MEDIA_REDDIT,
        icon = R.drawable.ic_reddit
    ),
)
