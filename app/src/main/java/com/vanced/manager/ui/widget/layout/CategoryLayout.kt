package com.vanced.manager.ui.widget.layout

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.vanced.manager.ui.component.text.CategoryTitleText
import com.vanced.manager.ui.util.defaultContentPaddingHorizontal
import com.vanced.manager.ui.util.defaultContentPaddingVertical

@Composable
fun CategoryLayout(
    categoryName: String,
    contentPaddingHorizontal: Dp = defaultContentPaddingHorizontal,
    categoryNameSpacing: Dp = defaultContentPaddingVertical,
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