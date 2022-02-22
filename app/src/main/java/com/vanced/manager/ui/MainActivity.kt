package com.vanced.manager.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import com.github.zsoltk.compose.backpress.BackPressHandler
import com.github.zsoltk.compose.backpress.LocalBackPressHandler
import com.github.zsoltk.compose.router.Router
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vanced.manager.core.installer.service.AppInstallService
import com.vanced.manager.core.installer.service.AppUninstallService
import com.vanced.manager.ui.screen.*
import com.vanced.manager.ui.theme.ManagerTheme
import com.vanced.manager.ui.theme.isDark
import com.vanced.manager.ui.util.Screen
import com.vanced.manager.ui.util.animated
import com.vanced.manager.ui.viewmodel.InstallViewModel
import com.vanced.manager.ui.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val installViewModel: InstallViewModel by viewModel()
    private val mainViewModel: MainViewModel by viewModel()

    private val backPressHandler = BackPressHandler()

    private val installBroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                AppInstallService.APP_INSTALL_ACTION -> {
                    installViewModel.postInstallStatus(
                        pmStatus = intent.getIntExtra(AppInstallService.EXTRA_INSTALL_STATUS, -999),
                        extra = intent.getStringExtra(AppInstallService.EXTRA_INSTALL_STATUS_MESSAGE)!!,
                    )
                    mainViewModel.fetch()
                }
                AppUninstallService.APP_UNINSTALL_ACTION -> {
                    mainViewModel.fetch()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.fetch()
        setContent {
            ManagerTheme {
                val surfaceColor = MaterialTheme.colorScheme.surface.animated

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
                                HomeScreen(
                                    viewModel = mainViewModel,
                                    onToolbarScreenSelected = {
                                        backStack.push(it)
                                    },
                                    onAppDownloadClick = { appName, appVersions, installationOptions ->
                                        if (installationOptions != null) {
                                            backStack.push(
                                                Screen.Configuration(
                                                    appName,
                                                    appVersions,
                                                    installationOptions
                                                )
                                            )
                                        } else {
                                            backStack.push(Screen.Install(appName, appVersions))
                                        }
                                    },
                                    onAppLaunchClick = { appName, packageName ->
                                        mainViewModel.launchApp(appName, packageName)
                                    },
                                    onAppUninstallClick = { packageName ->
                                        mainViewModel.uninstallApp(packageName)
                                    }
                                )
                            }
                            is Screen.Settings -> {
                                SettingsScreen(
                                    onToolbarBackButtonClick = {
                                        backStack.pop()
                                    }
                                )
                            }
                            is Screen.About -> {
                                AboutScreen(
                                    onToolbarBackButtonClick = {
                                        backStack.pop()
                                    }
                                )
                            }
                            is Screen.Logs -> {

                            }
                            is Screen.Configuration -> {
                                ConfigurationScreen(
                                    installationOptions = screen.appInstallationOptions,
                                    onToolbarBackButtonClick = {
                                        backStack.pop()
                                    },
                                    onFinishClick = {
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
                                    viewModel = installViewModel,
                                    onFinishClick = {
                                        installViewModel.clear()
                                        backStack.newRoot(Screen.Home)
                                    }
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
                addAction(AppInstallService.APP_INSTALL_ACTION)
                addAction(AppUninstallService.APP_UNINSTALL_ACTION)
            }
        )
    }

    override fun onStop() {
        super.onStop()

        unregisterReceiver(installBroadcastReceiver)
    }


}