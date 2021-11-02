package com.vanced.manager.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.zsoltk.compose.backpress.BackPressHandler
import com.github.zsoltk.compose.backpress.LocalBackPressHandler
import com.github.zsoltk.compose.router.Router
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vanced.manager.core.installer.service.AppInstallService
import com.vanced.manager.ui.component.color.managerAnimatedColor
import com.vanced.manager.ui.component.color.managerSurfaceColor
import com.vanced.manager.ui.component.color.managerTextColor
import com.vanced.manager.ui.component.menu.ManagerDropdownMenuItem
import com.vanced.manager.ui.component.text.ToolbarTitleText
import com.vanced.manager.ui.resources.managerString
import com.vanced.manager.ui.screens.*
import com.vanced.manager.ui.theme.ManagerTheme
import com.vanced.manager.ui.theme.isDark
import com.vanced.manager.ui.util.Screen
import com.vanced.manager.ui.viewmodel.InstallViewModel
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val installViewModel: InstallViewModel by inject()

    private val backPressHandler = BackPressHandler()

    private val installBroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action != AppInstallService.APP_INSTALL_STATUS) return

            installViewModel.postInstallStatus(
                pmStatus = intent.getIntExtra(AppInstallService.EXTRA_INSTALL_STATUS, -999),
                extra = intent.getStringExtra(AppInstallService.EXTRA_INSTALL_EXTRA)!!,
            )
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ManagerTheme {
                var isMenuExpanded by remember { mutableStateOf(false) }
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

                val surfaceColor = managerSurfaceColor()

                val isDark = isDark()

                val systemUiController = rememberSystemUiController()

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = surfaceColor,
                        darkIcons = !isDark
                    )
                }

                CompositionLocalProvider(
                    LocalBackPressHandler provides backPressHandler
                ) {
                    Router<Screen>("VancedManager", Screen.Home) { backStack ->
                        val screen = backStack.last()
                        currentScreen = screen

                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    title = {
                                        ToolbarTitleText(
                                            text = managerString(
                                                stringId = currentScreen.displayName
                                            )
                                        )
                                    },
                                    backgroundColor = managerAnimatedColor(color = MaterialTheme.colors.surface),
                                    actions = {
                                        if (currentScreen is Screen.Home) {
                                            IconButton(
                                                onClick = { isMenuExpanded = true }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.MoreVert,
                                                    contentDescription = null,
                                                    tint = managerTextColor()
                                                )
                                            }

                                            DropdownMenu(
                                                expanded = isMenuExpanded,
                                                onDismissRequest = {
                                                    isMenuExpanded = false
                                                },
                                                modifier = Modifier.background(MaterialTheme.colors.surface),
                                            ) {
                                                ManagerDropdownMenuItem(
                                                    title = stringResource(id = Screen.Settings.displayName)
                                                ) {
                                                    isMenuExpanded = false
                                                    backStack.push(Screen.Settings)
                                                }
                                            }
                                        }
                                    },
                                    navigationIcon = if (currentScreen !is Screen.Home) {
                                        {
                                            IconButton(onClick = {
                                                backStack.pop()
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Default.ArrowBackIos,
                                                    contentDescription = null,
                                                    tint = managerTextColor()
                                                )
                                            }
                                        }
                                    } else null,
                                    elevation = 0.dp
                                )
                            },
                            backgroundColor = surfaceColor
                        ) {
                            when (screen) {
                                is Screen.Home -> {
                                    HomeLayout(
                                        onAppInstallPress = { appName, appVersions, installationOptions ->
                                            if (installationOptions != null) {
                                                backStack.push(Screen.InstallPreferences(appName, appVersions,  installationOptions))
                                            } else {
                                                backStack.push(Screen.Install(appName, appVersions))
                                            }
                                        }
                                    )
                                }
                                is Screen.Settings -> {
                                    SettingsLayout()
                                }
                                is Screen.About -> {
                                    AboutLayout()
                                }
                                is Screen.Logs -> {

                                }
                                is Screen.InstallPreferences -> {
                                    InstallPreferencesScreen(
                                        installationOptions = screen.appInstallationOptions,
                                        onDoneClick = {
                                            backStack.push(Screen.Install(screen.appName, screen.appVersions))
                                        }
                                    )
                                }
                                is Screen.Install -> {
                                    InstallScreen(screen.appName, screen.appVersions)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (!backPressHandler.handle())
            super.onBackPressed()
    }

    override fun onStart() {
        super.onStart()

        registerReceiver(
            installBroadcastReceiver,
            IntentFilter().apply {
                addAction(AppInstallService.APP_INSTALL_STATUS)
            }
        )
    }

    override fun onStop() {
        super.onStop()

        unregisterReceiver(installBroadcastReceiver)
    }


}