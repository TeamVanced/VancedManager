package com.vanced.manager

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vanced.manager.preference.defPrefs
import com.vanced.manager.preference.managerTheme
import com.vanced.manager.ui.composables.*
import com.vanced.manager.ui.layouts.*
import com.vanced.manager.ui.screens.Screen
import com.vanced.manager.ui.theme.ComposeTestTheme
import com.vanced.manager.ui.theme.isDark
import com.vanced.manager.ui.theme.managerTheme

class MainActivity : AppCompatActivity() {

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            managerTheme = defPrefs.managerTheme

            ComposeTestTheme {
                //ButtonLayoutActivity()
                MainActivityLayout()
            }
        }
    }

    @ExperimentalAnimationApi
    @Composable
    fun MainActivityLayout() {
        val isMenuExpanded = remember { mutableStateOf(false) }
        val systemUiController = rememberSystemUiController()
        val surfaceColor = managerSurfaceColor()
        val isDark = isDark()
        val navController = rememberNavController()

        val dropdownScreens = listOf(
            Screen.Settings,
            Screen.Logs,
            Screen.About
        )

        SideEffect {
            systemUiController.setSystemBarsColor(surfaceColor, !isDark)
        }

        Scaffold(
            topBar = {
                MainToolbar(
                    navController,
                    dropdownScreens,
                    isMenuExpanded
                )
            },
            backgroundColor = managerSurfaceColor()
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route
            ) {
                composable(Screen.Home.route) {
                    HomeLayout()
                }
            }
        }
    }

    @Composable
    fun MainToolbar(
        navController: NavController,
        dropdownScreens: List<Screen>,
        isMenuExpanded: MutableState<Boolean>
    ) {
        TopAppBar(
            title = {
                navController.currentDestination
                Text(
                    //This does not look good at all
                    text = Screen::class.sealedSubclasses.find {
                        it.objectInstance?.route == navController.currentDestination?.route
                    }?.objectInstance?.displayName ?: "",
                    color = managerAnimatedColor(color = MaterialTheme.colors.onSurface)
                )
            },
            backgroundColor = managerAnimatedColor(color = MaterialTheme.colors.surface),
            actions = {
                if (navController.currentDestination?.route == Screen.Home.route) {
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
                        modifier = Modifier.background(managerCardColor())
                    ) {
                        dropdownScreens.forEach {
                            ManagerDropdownMenuItem(
                                isMenuExpanded = isMenuExpanded,
                                title = it.displayName
                            ) {
                                navController.navigate(it.route) {
                                    popUpTo(Screen.Home.route)
                                    anim {
                                        enter = R.animator.fragment_enter
                                        exit = R.animator.fragment_exit
                                        popEnter = R.animator.fragment_enter_pop
                                        popEnter = R.animator.fragment_exit_pop
                                    }
                                    restoreState = true
                                }
                            }
                        }
                    }
                }
            },
            navigationIcon = if (navController.currentDestination?.route != Screen.Home.route) { {
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