package com.vanced.manager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList

@Composable
fun <T> rememberNavigationController(
    initialScreen: T
) = remember {
    NavigationControllerImpl(initialScreen)
}

interface NavigationController<T> {

    val screens: SnapshotStateList<T>

    fun push(item: T)

    fun pop(): Boolean

}

class NavigationControllerImpl<T>(
    initialScreen: T
) : NavigationController<T> {

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