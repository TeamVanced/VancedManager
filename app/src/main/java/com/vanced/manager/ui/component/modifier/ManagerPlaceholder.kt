package com.vanced.manager.ui.component.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

fun Modifier.managerPlaceholder(
    visible: Boolean
) = composed {
    then(
        placeholder(
            visible = visible,
            highlight = PlaceholderHighlight.shimmer()
        )
    )
}