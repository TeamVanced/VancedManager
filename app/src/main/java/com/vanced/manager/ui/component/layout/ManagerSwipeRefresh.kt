package com.vanced.manager.ui.component.layout

import androidx.compose.runtime.Composable
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.vanced.manager.ui.component.color.managerAccentColor

@Composable
fun ManagerSwipeRefresh(
    refreshState: SwipeRefreshState,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    SwipeRefresh(
        state = refreshState,
        onRefresh = onRefresh,
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = trigger,
                scale = true,
                contentColor = managerAccentColor()
            )
        },
        content = content
    )
}