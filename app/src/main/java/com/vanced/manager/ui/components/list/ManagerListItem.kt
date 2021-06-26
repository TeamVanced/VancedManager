package com.vanced.manager.ui.components.list

import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ManagerListItem(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    description: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null
) {
    Row(modifier = modifier) {
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
                    start = if (icon != null) 12.dp else 0.dp,
                    top = if (description != null) 6.dp else 12.dp,
                    bottom = if (description != null) 6.dp else 12.dp,
                    end = if (trailing != null) 12.dp else 0.dp,
                )
                .align(Alignment.CenterVertically)
        ) {
            title()
            if (description != null) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    description()
                }
            }
        }
        if (trailing != null) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .align(Alignment.CenterVertically),
                contentAlignment = Alignment.Center,
            ) {
                trailing()
            }
        }
    }
}