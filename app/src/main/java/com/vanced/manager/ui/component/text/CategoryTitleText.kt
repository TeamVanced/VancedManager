package com.vanced.manager.ui.component.text

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vanced.manager.ui.component.color.managerAnimatedColor
import com.vanced.manager.ui.util.defaultContentPaddingHorizontal

@Composable
fun CategoryTitleText(
    text: String
) {
    ManagerText(
        modifier = Modifier.padding(start = defaultContentPaddingHorizontal),
        text = text,
        textStyle = MaterialTheme.typography.h2,
        color = managerAnimatedColor(MaterialTheme.colors.onSurface)
    )
}