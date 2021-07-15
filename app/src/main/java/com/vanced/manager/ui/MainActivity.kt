package com.vanced.manager.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
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
import androidx.compose.ui.util.fastForEach
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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

    @Composable
    fun MainActivityLayout() {
        val isMenuExpanded = remember { mutableStateOf(false) }
        val systemUiController = rememberSystemUiController()
        val surfaceColor = managerSurfaceColor()
        val isDark = isDark()
        val navController = rememberNavController()

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
                    isMenuExpanded = isMenuExpanded
                )
            },
            backgroundColor = managerSurfaceColor()
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route
            ) {
                screens.fastForEach { screen ->
                    composable(screen.route) {
                        screen.content()
                    }
                }
            }
        }
    }

    @Composable
    fun MainToolbar(
        navController: NavController,
        screens: List<Screen>,
        isMenuExpanded: MutableState<Boolean>
    ) {
        val currentScreenRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        TopAppBar(
            title = {
                ToolbarTitleText(managerString(stringId = screens.find { it.route == currentScreenRoute }?.displayName))
            },
            backgroundColor = managerAnimatedColor(color = MaterialTheme.colors.surface),
            actions = {
                if (currentScreenRoute == Screen.Home.route) {
                    IconButton(
                        onClick = { isMenuExpanded.value = !isMenuExpanded.value }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null,
                            tint = managerTextColor()
                        )
                    }

                    DropdownMenu(
                        expanded = isMenuExpanded.value,
                        onDismissRequest = {
                            isMenuExpanded.value = false
                        },
                        modifier = Modifier.background(MaterialTheme.colors.surface)
                    ) {
                        screens.filter { it.route != currentScreenRoute }.forEach {
                            ManagerDropdownMenuItem(
                                title = stringResource(id = it.displayName)
                            ) {
                                isMenuExpanded.value = !isMenuExpanded.value
                                navController.navigate(it.route) {
                                    popUpTo(Screen.Home.route) {
                                        saveState = true
                                    }
                                }
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