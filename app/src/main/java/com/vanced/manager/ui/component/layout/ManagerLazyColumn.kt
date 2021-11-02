package com.vanced.manager.ui.component.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vanced.manager.ui.util.DefaultContentPaddingHorizontal

@Composable
fun ManagerLazyColumn(
    modifier: Modifier = Modifier,
    itemSpacing: Dp = 0.dp,
    contentPadding: PaddingValues = PaddingValues(12.dp),
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(itemSpacing),
        content = content
    )
}
