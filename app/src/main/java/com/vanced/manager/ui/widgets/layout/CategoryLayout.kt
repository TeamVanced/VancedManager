package com.vanced.manager.ui.widgets.layout

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.vanced.manager.ui.utils.defaultContentPaddingHorizontal
import com.vanced.manager.ui.utils.defaultContentPaddingVertical
import com.vanced.manager.ui.widgets.text.CategoryTitleText

@Composable
fun CategoryLayout(
    @StringRes categoryNameId: Int,
    contentPaddingHorizontal: Dp = defaultContentPaddingHorizontal,
    categoryNameSpacing: Dp = defaultContentPaddingVertical,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(categoryNameSpacing),
    ) {
        CategoryTitleText(stringId = categoryNameId)
        Box(
            modifier = Modifier.padding(horizontal = contentPaddingHorizontal)
        ) {
            content()
        }
    }
}