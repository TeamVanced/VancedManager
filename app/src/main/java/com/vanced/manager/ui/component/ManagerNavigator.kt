package com.vanced.manager.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.with
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.vanced.manager.ui.util.Screen

@Composable
fun <T> rememberManagerNavigationController(
    initialScreen: T
) = remember {
    ManagerNavigationControllerImpl(initialScreen)
}

interface ManagerNavigationController<T> {

    val screens: SnapshotStateList<T>

    fun push(item: T)

    fun pop(): Boolean

}

class ManagerNavigationControllerImpl<T>(
    initialScreen: T
) : ManagerNavigationController<T> {

    override val screens: SnapshotStateList<T> =
        mutableStateListOf(initialScreen)

    override fun push(item: T) {
        screens.add(item)
    }

    override fun pop(): Boolean {
        if (screens.size > 1) {
            screens.removeLast()
            return true
        }
        return false
    }
}

@Composable
fun <T : Screen> ManagerNavigator(
    navigationController: ManagerNavigationController<T>,
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