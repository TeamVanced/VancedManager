package com.vanced.manager.ui.component.topappbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vanced.manager.ui.component.color.managerAnimatedColor
import com.vanced.manager.ui.component.text.ManagerText

@Composable
fun ManagerTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
) {
    SmallTopAppBar(
        modifier = modifier,
        title = {
            ManagerText(
                text = title,
                textStyle = MaterialTheme.typography.headlineMedium,
                color = managerAnimatedColor(MaterialTheme.colorScheme.onSurface)
            )
        },
        actions = actions,
        navigationIcon = navigationIcon,
    )
}