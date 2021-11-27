package com.vanced.manager.ui.component.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> ScrollableItemRow(
    items: List<T>,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    val state = rememberLazyListState()
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        state = state
    ) {
        item {
            Spacer(Modifier.width(8.dp))
        }
        items(items) { item ->
            content(item)
        }
        item {
            Spacer(Modifier.width(8.dp))
        }
    }
}