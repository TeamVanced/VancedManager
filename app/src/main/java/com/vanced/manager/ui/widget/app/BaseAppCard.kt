package com.vanced.manager.ui.widget.app

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vanced.manager.R
import com.vanced.manager.ui.component.card.ManagerTonalCard
import com.vanced.manager.ui.component.list.ManagerListItem
import com.vanced.manager.ui.component.text.ManagerText
import com.vanced.manager.ui.theme.LargeShape
import com.vanced.manager.ui.theme.MediumShape
import com.vanced.manager.ui.util.DefaultContentPaddingHorizontal
import com.vanced.manager.ui.util.DefaultContentPaddingVertical

@Composable
fun BaseAppCard(
    appTitle: @Composable () -> Unit,
    appIcon: @Composable () -> Unit,
    appTrailing: @Composable () -> Unit,
    appVersionsColumn: @Composable ColumnScope.() -> Unit,
    appActionsRow: @Composable RowScope.() -> Unit,
) {
    ManagerTonalCard(
        modifier = Modifier.fillMaxWidth(),
        shape = LargeShape,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = DefaultContentPaddingHorizontal,
                    vertical = DefaultContentPaddingVertical
                ),
            verticalArrangement = Arrangement
                .spacedBy(DefaultContentPaddingVertical)
        ) {
            ManagerListItem(
                modifier = Modifier.fillMaxWidth(),
                title = appTitle,
                icon = appIcon,
                trailing = appTrailing
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MediumShape),
                thickness = 2.dp,
                color = LocalContentColor.current.copy(alpha = 0.12f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.wrapContentWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    ManagerText(
                        text = stringResource(id = R.string.app_versions),
                        textStyle = MaterialTheme.typography.bodyMedium
                    )
                    appVersionsColumn()
                }
                Row(
                    modifier = Modifier.wrapContentWidth(),
                    content = appActionsRow,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                )
            }
        }
    }
}