package com.vanced.manager.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanced.manager.ui.util.DefaultContentPaddingHorizontal
import com.vanced.manager.ui.util.DefaultContentPaddingVertical

@Composable
fun ManagerListItem(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    description: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(DefaultContentPaddingHorizontal)
    ) {
        if (icon != null) {
            Box(
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                icon()
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    vertical =
                    if (description != null) DefaultContentPaddingVertical - 4.dp else DefaultContentPaddingVertical,
                )
                .align(Alignment.CenterVertically)
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.titleSmall
            ) {
                title()
            }
            if (description != null) {
                CompositionLocalProvider(
                    LocalContentAlpha provides ContentAlpha.medium,
                    LocalTextStyle provides MaterialTheme.typography.bodySmall
                ) {
                    description()
                }
            }
        }
        if (trailing != null) {
            Box(
                modifier = Modifier.align(Alignment.CenterVertically),
                contentAlignment = Alignment.Center,
            ) {
                trailing()
            }
        }
    }
}