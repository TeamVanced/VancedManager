package com.vanced.manager.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

const val defaultPadding = 12

@Composable
fun ManagerListItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    startPadding: Dp = defaultPadding.dp,
    endPadding: Dp = defaultPadding.dp,
    topPadding: Dp = defaultPadding.dp,
    bottomPadding: Dp = defaultPadding.dp,
    icon: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null
) {
    val typography = MaterialTheme.typography
    val textColor by animateManagerColor(color = MaterialTheme.colors.onBackground)
    Box(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(top = topPadding, bottom = bottomPadding)
        ) {
            if (icon != null) {
                Box(
                    modifier = Modifier
                        .padding(start = startPadding)
                        .align(Alignment.CenterVertically)
                ) {
                    icon()
                }
            }
            Column(
                modifier = Modifier
                    .padding(
                        start = startPadding,
                        end = endPadding
                    )
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                if (description == null) {
                    Spacer(modifier = Modifier.size(6.dp))
                }
                Text(
                    text = title,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth(),
                    style = typography.subtitle1,
                    color = textColor
                )
                if (description != null) {
                    CompositionLocalProvider(
                        LocalContentAlpha provides ContentAlpha.medium,
                        LocalContentColor provides textColor
                    ) {
                        Text(
                            text = description,
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                if (description == null) {
                    Spacer(modifier = Modifier.size(6.dp))
                }

            }
            if (trailing != null) {
                Box(
                    modifier = Modifier
                        .padding(end = endPadding)
                        .align(Alignment.CenterVertically)
                ) {
                    CompositionLocalProvider(LocalContentColor provides textColor) {
                        trailing()
                    }
                }
            }
        }
    }
}