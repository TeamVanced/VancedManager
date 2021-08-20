package com.vanced.manager.ui.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import com.vanced.manager.ui.util.Screen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <T : Screen> ManagerNavigator(
    navigationController: NavigationController<T>,
    content: @Composable (targetContent: T) -> Unit
) {
    val saveableStateHolder = rememberSaveableStateHolder()
    val screens = remember { navigationController.screens }

    //TODO Animation is not working for some weird reasons
    AnimatedContent(
        transitionSpec = {
            if (targetState.size > initialState.size) {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Start) with
                slideOutOfContainer(AnimatedContentScope.SlideDirection.End)
            } else {
                slideIntoContainer(AnimatedContentScope.SlideDirection.End) with
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Start)
            }
        },
        targetState = screens
    ) { targetContents ->
        val targetContent = targetContents.last()
        saveableStateHolder.SaveableStateProvider(key = targetContent.route) {
            content(targetContent)
        }
    }
}