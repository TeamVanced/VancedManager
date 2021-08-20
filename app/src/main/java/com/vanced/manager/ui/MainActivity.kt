package com.vanced.manager.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vanced.manager.ui.component.color.managerAnimatedColor
import com.vanced.manager.ui.component.color.managerSurfaceColor
import com.vanced.manager.ui.component.color.managerTextColor
import com.vanced.manager.ui.component.menu.ManagerDropdownMenuItem
import com.vanced.manager.ui.component.text.ToolbarTitleText
import com.vanced.manager.ui.navigation.ManagerNavigator
import com.vanced.manager.ui.navigation.NavigationController
import com.vanced.manager.ui.navigation.rememberNavigationController
import com.vanced.manager.ui.resources.managerString
import com.vanced.manager.ui.theme.ManagerTheme
import com.vanced.manager.ui.theme.isDark
import com.vanced.manager.ui.util.Screen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ManagerTheme {
                MainActivityLayout()
            }
        }
    }

    @Composable
    fun MainActivityLayout() {
        val systemUiController = rememberSystemUiController()
        val surfaceColor = managerSurfaceColor()
        val isDark = isDark()

        val navController = rememberNavigationController<Screen>(
            initialScreen = Screen.Home
        )

        val screens = listOf(
            Screen.Home,
            Screen.Settings,
            Screen.About,
            Screen.Logs
        )

        SideEffect {
            systemUiController.setSystemBarsColor(surfaceColor, !isDark)
        }

        BackHandler {
            if (!navController.pop()) {
                finish()
            }
        }

        Scaffold(
            topBar = {
                MainToolbar(
                    navController = navController,
                    screens = screens,
                )
            },
            backgroundColor = surfaceColor
        ) {
            ManagerNavigator(
                navigationController = navController
            ) {
                it.content()
            }
        }
    }

    @Composable
    fun MainToolbar(
        navController: NavigationController<Screen>,
        screens: List<Screen>,
    ) {
        var isMenuExpanded by remember { mutableStateOf(false) }

        val currentScreenRoute = navController.screens.last().route
        TopAppBar(
            title = {
                ToolbarTitleText(managerString(stringId = screens.find { it.route == currentScreenRoute }?.displayName))
            },
            backgroundColor = managerAnimatedColor(color = MaterialTheme.colors.surface),
            actions = {
                if (currentScreenRoute == Screen.Home.route) {
                    IconButton(
                        onClick = { isMenuExpanded= !isMenuExpanded }
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
                        modifier = Modifier.background(MaterialTheme.colors.surface)
                    ) {
                        screens.filter { it.route != currentScreenRoute }.forEach {
                            ManagerDropdownMenuItem(
                                title = stringResource(id = it.displayName)
                            ) {
                                isMenuExpanded = !isMenuExpanded
                                navController.push(it)
                            }
                        }
                    }
                }
            },
            navigationIcon = if (currentScreenRoute != Screen.Home.route) {{
                IconButton(onClick = {
                    navController.pop()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIos,
                        contentDescription = null,
                        tint = managerTextColor()
                    )
                }
            }} else null,
            elevation = 0.dp
        )
    }

}