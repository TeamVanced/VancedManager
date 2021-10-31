package com.vanced.manager.ui.widget.app

import androidx.compose.foundation.layout.*
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vanced.manager.R
import com.vanced.manager.ui.component.card.ManagerCard
import com.vanced.manager.ui.component.card.ManagerThemedCard
import com.vanced.manager.ui.component.list.ManagerListItem
import com.vanced.manager.ui.component.text.ManagerText
import com.vanced.manager.ui.util.DefaultContentPaddingHorizontal
import com.vanced.manager.ui.util.DefaultContentPaddingVertical

@Composable
fun BaseAppCard(
    appTitle: @Composable () -> Unit,
    appIcon: @Composable () -> Unit,
    appVersionsColumn: @Composable ColumnScope.() -> Unit,
    appActionsRow: @Composable RowScope.() -> Unit,
) {
    ManagerThemedCard {
        Column {
            ManagerCard {
                ManagerListItem(
                    modifier = Modifier.padding(
                        horizontal = DefaultContentPaddingHorizontal,
                        vertical = DefaultContentPaddingVertical
                    ),
                    title = appTitle,
                    icon = appIcon
                )
            }
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colors.onSurface
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = DefaultContentPaddingHorizontal,
                        vertical = 8.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth(Alignment.Start),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        ManagerText(stringResource(id = R.string.app_versions))
                        appVersionsColumn()
                    }
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                            .wrapContentWidth(Alignment.End),
                        content = appActionsRow
                    )
                }
            }
        }
    }
}