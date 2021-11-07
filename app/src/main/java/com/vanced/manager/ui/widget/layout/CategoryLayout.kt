package com.vanced.manager.ui.widget.layout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vanced.manager.ui.component.color.managerAnimatedColor
import com.vanced.manager.ui.component.text.ManagerText
import com.vanced.manager.ui.util.DefaultContentPaddingHorizontal
import com.vanced.manager.ui.util.DefaultContentPaddingVertical

@Composable
fun CategoryLayout(
    categoryName: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(DefaultContentPaddingVertical)
    ) {
        ManagerText(
            modifier = Modifier.padding(start = DefaultContentPaddingHorizontal),
            text = categoryName,
            textStyle = MaterialTheme.typography.headlineSmall,
            color = managerAnimatedColor(MaterialTheme.colorScheme.onSurface)
        )
        content()
    }
}

fun <T> LazyListScope.managerCategory(
    categoryName: String,
    items: List<T>,
    itemContent: @Composable (T) -> Unit
) {
    item {
        ManagerText(
            modifier = Modifier.padding(
                horizontal = DefaultContentPaddingHorizontal,
                vertical = DefaultContentPaddingVertical
            ),
            text = categoryName,
            textStyle = MaterialTheme.typography.headlineSmall,
            color = managerAnimatedColor(MaterialTheme.colorScheme.onSurface)
        )
    }
    items(items) { item ->
        itemContent(item)
    }
}

fun LazyListScope.managerCategory(
    categoryName: String,
    content: @Composable LazyItemScope.() -> Unit,
) {
    item {
        ManagerText(
            modifier = Modifier
                .padding(
                    horizontal = DefaultContentPaddingHorizontal,
                    vertical = DefaultContentPaddingVertical
                ),
            text = categoryName,
            textStyle = MaterialTheme.typography.headlineSmall,
            color = managerAnimatedColor(MaterialTheme.colorScheme.onSurface)
        )
    }
    item(content = content)
}
