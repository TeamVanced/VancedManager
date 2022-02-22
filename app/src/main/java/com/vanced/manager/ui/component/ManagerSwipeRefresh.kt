package com.vanced.manager.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState

@Composable
fun ManagerSwipeRefresh(
    swipeRefreshState: SwipeRefreshState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    swipeEnabled: Boolean = true,
    refreshTriggerDistance: Dp = 80.dp,
    indicatorAlignment: Alignment = Alignment.TopCenter,
    indicatorPadding: PaddingValues = PaddingValues(0.dp),
    clipIndicatorToPadding: Boolean = true,
    content: @Composable () -> Unit,
) {
    SwipeRefresh(
        modifier = modifier,
        state = swipeRefreshState,
        onRefresh = onRefresh,
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = trigger,
                scale = true,
                contentColor = MaterialTheme.colorScheme.primary,
                backgroundColor = MaterialTheme.colorScheme.surface
            )
        },
        swipeEnabled = swipeEnabled,
        refreshTriggerDistance = refreshTriggerDistance,
        indicatorAlignment = indicatorAlignment,
        indicatorPadding = indicatorPadding,
        clipIndicatorToPadding = clipIndicatorToPadding,
        content = content
    )
}