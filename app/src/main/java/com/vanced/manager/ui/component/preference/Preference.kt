package com.vanced.manager.ui.component.preference

import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.vanced.manager.ui.component.color.managerAnimatedColor
import com.vanced.manager.ui.component.list.ManagerListItem
import com.vanced.manager.ui.component.modifier.managerClickable
import com.vanced.manager.ui.component.text.ManagerText
import com.vanced.manager.ui.util.defaultContentPaddingHorizontal

@Composable
fun Preference(
    preferenceTitle: String,
    preferenceDescription: String? = null,
    trailing: @Composable () -> Unit = {},
    onClick: () -> Unit
) {
    Preference(
        preferenceTitle = { ManagerText(text = preferenceTitle) },
        preferenceDescription = if (preferenceDescription != null) {
            {
                ManagerText(text = preferenceDescription)
            }
        } else null,
        trailing = trailing,
        onClick = onClick
    )
}

@Composable
fun Preference(
    preferenceTitle: @Composable () -> Unit,
    preferenceDescription: @Composable (() -> Unit)? = null,
    trailing: @Composable () -> Unit = {},
    onClick: () -> Unit
) {
    val color = managerAnimatedColor(color = MaterialTheme.colors.onSurface)
    ManagerListItem(
        modifier = Modifier
            .managerClickable(onClick = onClick)
            .padding(horizontal = defaultContentPaddingHorizontal),
        title = {
            CompositionLocalProvider(
                LocalContentColor provides color,
                LocalTextStyle provides MaterialTheme.typography.h6
            ) {
                preferenceTitle()
            }
        },
        description = if (preferenceDescription != null) {
            {
                CompositionLocalProvider(
                    LocalContentColor provides color,
                    LocalTextStyle provides MaterialTheme.typography.subtitle1
                ) {
                    preferenceDescription()
                }
            }
        } else null,
        trailing = trailing,
    )
}
