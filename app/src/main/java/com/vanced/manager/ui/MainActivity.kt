package com.vanced.manager.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vanced.manager.ui.component.color.managerAnimatedColor
import com.vanced.manager.ui.component.color.managerSurfaceColor
import com.vanced.manager.ui.component.color.managerTextColor
import com.vanced.manager.ui.component.menu.ManagerDropdownMenuItem
import com.vanced.manager.ui.component.text.ToolbarTitleText
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

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun MainActivityLayout() {
        val systemUiController = rememberSystemUiController()
        val surfaceColor = managerSurfaceColor()
        val isDark = isDark()

        val navController = rememberAnimatedNavController()

        val screens = listOf(
            Screen.Home,
            Screen.Settings,
            Screen.About,
            Screen.Logs
        )

        SideEffect {
            systemUiController.setSystemBarsColor(surfaceColor, !isDark)
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
            AnimatedNavHost(
                navController = navController,
                startDestination = "home",
            ) {
                screens.fastForEach { screen ->
                    composable(
                        route = screen.route,
                        enterTransition = { _, _ ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.Start) + fadeIn()
                        },
                        exitTransition = { _, _ ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.End) + fadeOut()
                        },
                        popEnterTransition = { _, _ ->
                            slideIntoContainer(AnimatedContentScope.SlideDirection.End) + fadeIn()
                        },
                        popExitTransition = { _, _ ->
                            slideOutOfContainer(AnimatedContentScope.SlideDirection.Start) + fadeOut()
                        }
                    ) {
                        screen.content()
                    }
                }
            }
        }
    }

    @Composable
    fun MainToolbar(
        navController: NavHostController,
        screens: List<Screen>,
    ) {
        var isMenuExpanded by remember { mutableStateOf(false) }

        val currentScreenRoute = navController.currentBackStackEntryAsState().value?.destination?.route
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
                        screens.filter { it.route != currentScreenRoute }.forEach { screen ->
                            ManagerDropdownMenuItem(
                                title = stringResource(id = screen.displayName)
                            ) {
                                isMenuExpanded = !isMenuExpanded
                                navController.navigate(screen.route)
                            }
                        }
                    }
                }
            },
            navigationIcon = if (currentScreenRoute != Screen.Home.route) {{
                IconButton(onClick = {
                    navController.popBackStack()
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