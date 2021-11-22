package com.vanced.manager.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import com.github.zsoltk.compose.backpress.BackPressHandler
import com.github.zsoltk.compose.backpress.LocalBackPressHandler
import com.github.zsoltk.compose.router.Router
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vanced.manager.core.installer.service.AppInstallService
import com.vanced.manager.ui.component.color.managerSurfaceColor
import com.vanced.manager.ui.screens.*
import com.vanced.manager.ui.theme.ManagerTheme
import com.vanced.manager.ui.theme.isDark
import com.vanced.manager.ui.util.Screen
import com.vanced.manager.ui.viewmodel.InstallViewModel
import com.vanced.manager.ui.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val installViewModel: InstallViewModel by viewModel()
    private val mainViewModel: MainViewModel by viewModel()

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

    @OptIn(
        ExperimentalAnimationApi::class,
        ExperimentalMaterial3Api::class,
        ExperimentalFoundationApi::class,
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ManagerTheme {
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
                        when (val screen = backStack.last()) {
                            is Screen.Home -> {
                                HomeLayout(
                                    viewModel = mainViewModel,
                                    onToolbarScreenSelected = {
                                        backStack.push(it)
                                    },
                                    onAppInstallPress = { appName, appVersions, installationOptions ->
                                        if (installationOptions != null) {
                                            backStack.push(
                                                Screen.InstallPreferences(
                                                    appName,
                                                    appVersions,
                                                    installationOptions
                                                )
                                            )
                                        } else {
                                            backStack.push(Screen.Install(appName, appVersions))
                                        }
                                    }
                                )
                            }
                            is Screen.Settings -> {
                                SettingsLayout(
                                    onToolbarBackButtonClick = {
                                        backStack.pop()
                                    }
                                )
                            }
                            is Screen.About -> {
                                AboutLayout(
                                    onToolbarBackButtonClick = {
                                        backStack.pop()
                                    }
                                )
                            }
                            is Screen.Logs -> {

                            }
                            is Screen.InstallPreferences -> {
                                InstallPreferencesScreen(
                                    installationOptions = screen.appInstallationOptions,
                                    onToolbarBackButtonClick = {
                                        backStack.pop()
                                    },
                                    onDoneClick = {
                                        backStack.push(
                                            Screen.Install(
                                                screen.appName,
                                                screen.appVersions
                                            )
                                        )
                                    }
                                )
                            }
                            is Screen.Install -> {
                                InstallScreen(
                                    appName = screen.appName,
                                    appVersions = screen.appVersions,
                                    viewModel = installViewModel
                                )
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