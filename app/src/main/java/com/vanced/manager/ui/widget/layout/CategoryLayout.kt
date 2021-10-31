package com.vanced.manager.ui.widget.layout

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.vanced.manager.ui.component.text.CategoryTitleText
import com.vanced.manager.ui.util.DefaultContentPaddingHorizontal
import com.vanced.manager.ui.util.DefaultContentPaddingVertical

@Composable
fun CategoryLayout(
    categoryName: String,
    contentPaddingHorizontal: Dp = DefaultContentPaddingHorizontal,
    categoryNameSpacing: Dp = DefaultContentPaddingVertical,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(categoryNameSpacing),
    ) {
        CategoryTitleText(text = categoryName)
        Box(
            modifier = Modifier.padding(horizontal = contentPaddingHorizontal)
        ) {
            content()
        }
    }
}