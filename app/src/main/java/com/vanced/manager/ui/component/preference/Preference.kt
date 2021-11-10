package com.vanced.manager.ui.component.preference

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanced.manager.ui.component.card.ManagerTonalCard
import com.vanced.manager.ui.component.color.managerAnimatedColor
import com.vanced.manager.ui.component.list.ManagerListItem
import com.vanced.manager.ui.component.modifier.managerClickable
import com.vanced.manager.ui.component.text.ManagerText
import com.vanced.manager.ui.theme.LargeShape
import com.vanced.manager.ui.util.DefaultContentPaddingHorizontal
import com.vanced.manager.ui.util.DefaultContentPaddingVertical

@Composable
fun Preference(
    preferenceTitle: String,
    preferenceDescription: String? = null,
    trailing: @Composable () -> Unit = {},
    onClick: () -> Unit
) {
    val color = managerAnimatedColor(color = MaterialTheme.colorScheme.onSurface)
    ManagerTonalCard(
        shape = LargeShape,
        onClick = onClick
    ) {
        ManagerListItem(
            modifier = Modifier
                .padding(
                    horizontal = DefaultContentPaddingHorizontal,
                    vertical = 8.dp
                ),
            title = {
                CompositionLocalProvider(
                    LocalContentColor provides color,
                    LocalTextStyle provides MaterialTheme.typography.titleSmall
                ) {
                    ManagerText(text = preferenceTitle)
                }
            },
            description = if (preferenceDescription != null) {
                {
                    CompositionLocalProvider(
                        LocalContentColor provides color,
                        LocalTextStyle provides MaterialTheme.typography.bodySmall
                    ) {
                        ManagerText(text = preferenceDescription)
                    }
                }
            } else null,
            trailing = trailing,
        )
    }
}