package com.vanced.manager.ui.widget.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

@Composable
fun SettingsCategoryLayout(
    categoryName: String,
    content: @Composable () -> Unit
) {
    CategoryLayout(
        categoryName = categoryName,
    ) {
        Column {
            content()
        }
    }
}