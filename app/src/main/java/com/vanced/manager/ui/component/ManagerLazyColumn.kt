package com.vanced.manager.ui.component

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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

inline fun LazyListScope.managerCategory(
    noinline categoryName: @Composable () -> String,
    content: LazyListScope.() -> Unit,
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
