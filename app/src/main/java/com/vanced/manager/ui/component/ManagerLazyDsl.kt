package com.vanced.manager.ui.component

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanced.manager.ui.util.EdgeToEdgeContentPadding

@Composable
fun ManagerLazyColumn(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(
        start = EdgeToEdgeContentPadding,
        end = EdgeToEdgeContentPadding,
        bottom = 8.dp
    ),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        Arrangement.spacedBy(8.dp, if (!reverseLayout) Alignment.Top else Alignment.Bottom),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        reverseLayout = reverseLayout,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        flingBehavior = flingBehavior,
        content = content
    )
}

@Composable
fun ManagerLazyRow(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(
        start = EdgeToEdgeContentPadding,
        end = EdgeToEdgeContentPadding,
    ),
    reverseLayout: Boolean = false,
    horizontalArrangement: Arrangement.Horizontal =
        Arrangement.spacedBy(8.dp, if (!reverseLayout) Alignment.Start else Alignment.End),
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    content: LazyListScope.() -> Unit
) {
    LazyRow(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        reverseLayout = reverseLayout,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        flingBehavior = flingBehavior,
        content = content
    )
}

inline fun LazyListScope.managerCategory(
    crossinline categoryName: @Composable () -> String,
    content: LazyListScope.() -> Unit
) {
    item {
        ManagerText(
            modifier = Modifier
                .padding(
                    start = 8.dp,
                    top = 4.dp
                ),
            text = categoryName(),
            textStyle = MaterialTheme.typography.headlineSmall,
        )
    }
    content()
}

inline fun <K, V> LazyListScope.items(
    items: Map<K, V>,
    noinline key: ((key: K, value: V) -> Any)? = null,
    crossinline itemContent: @Composable LazyItemScope.(key: K, value: V) -> Unit
) = items(
    count = items.size,
    key = if (key != null) { index ->
        key(items.keys.elementAt(index), items.values.elementAt(index))
    } else null
) { index ->
    itemContent(items.keys.elementAt(index), items.values.elementAt(index))
}

inline fun <K, V> LazyListScope.itemsIndexed(
    items: Map<K, V>,
    noinline key: ((index: Int, key: K, value: V) -> Any)? = null,
    crossinline itemContent: @Composable LazyItemScope.(index: Int, key: K, value: V) -> Unit
) = items(
    count = items.size,
    key = if (key != null) { index ->
        key(index, items.keys.elementAt(index), items.values.elementAt(index))
    } else null
) { index ->
    itemContent(index, items.keys.elementAt(index), items.values.elementAt(index))
}
