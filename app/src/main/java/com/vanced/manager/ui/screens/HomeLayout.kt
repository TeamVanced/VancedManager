package com.vanced.manager.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.vanced.manager.R
import com.vanced.manager.core.util.socialMedia
import com.vanced.manager.core.util.sponsors
import com.vanced.manager.ui.component.card.ManagerLinkCard
import com.vanced.manager.ui.component.dialog.ManagerDialog
import com.vanced.manager.ui.component.layout.ManagerScrollableColumn
import com.vanced.manager.ui.component.layout.ManagerSwipeRefresh
import com.vanced.manager.ui.component.layout.ScrollableItemRow
import com.vanced.manager.ui.component.text.ManagerText
import com.vanced.manager.ui.resources.managerString
import com.vanced.manager.ui.util.DefaultContentPaddingVertical
import com.vanced.manager.ui.util.Screen
import com.vanced.manager.ui.viewmodel.MainViewModel
import com.vanced.manager.ui.widget.app.AppCard
import com.vanced.manager.ui.widget.app.AppCardPlaceholder
import com.vanced.manager.ui.widget.button.ManagerCloseButton
import com.vanced.manager.ui.widget.layout.CategoryLayout
import org.koin.androidx.compose.getViewModel

@ExperimentalAnimationApi
@Composable
fun HomeLayout(
    navController: NavController
) {
    val viewModel: MainViewModel = getViewModel()
    val appState by viewModel.appState.collectAsState()

    val refreshState = rememberSwipeRefreshState(isRefreshing = appState is MainViewModel.AppState.Fetching)
    ManagerSwipeRefresh(
        refreshState = refreshState,
        onRefresh = { viewModel.fetch() }
    ) {
        ManagerScrollableColumn(
            contentPaddingVertical = DefaultContentPaddingVertical,
            itemSpacing = 18.dp
        ) {
            CategoryLayout(
                categoryName = managerString(R.string.home_category_apps),
                contentPaddingHorizontal = 0.dp
            ) {
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

                                    var showAppInfoDialog by rememberSaveable { mutableStateOf(false) }

                                    AppCard(
                                        appName = app.name,
                                        appIcon = appIcon,
                                        appInstalledVersion = app.installedVersion,
                                        appRemoteVersion = app.remoteVersion,
                                        onDownloadClick = {
                                            if (app.installationOptions != null) {
                                                navController.navigate(Screen.InstallPreferences.route)
                                            } else {
                                                navController.navigate(Screen.Install.route)
                                            }

                                        },
                                        onUninstallClick = { /*TODO*/ },
                                        onLaunchClick = { /*TODO*/ },
                                        onInfoClick = {
                                            showAppInfoDialog = true
                                        }
                                    )

                                    if (showAppInfoDialog) {
                                        ManagerDialog(
                                            title = managerString(R.string.app_info_title, app.name),
                                            onDismissRequest = { showAppInfoDialog = false },
                                            buttons = {
                                                ManagerCloseButton(onClick = {
                                                    showAppInfoDialog = false
                                                })
                                            }
                                        ) {
                                            ManagerText(
                                                modifier = Modifier.padding(top = 4.dp),
                                                text = app.changelog,
                                                textStyle = MaterialTheme.typography.subtitle1
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
            CategoryLayout(managerString(R.string.home_category_support_us)) {
                ScrollableItemRow(items = sponsors) { sponsor ->
                    ManagerLinkCard(
                        icon = sponsor.icon,
                        title = sponsor.title,
                        link = sponsor.link
                    )
                }
            }
            CategoryLayout( managerString(R.string.home_category_social_media)) {
                ScrollableItemRow(items = socialMedia) { socialMedia ->
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