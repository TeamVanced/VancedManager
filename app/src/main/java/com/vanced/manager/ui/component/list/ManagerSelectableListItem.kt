package com.vanced.manager.ui.component.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ManagerSelectableListItem(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    trailing: @Composable (() -> Unit)
) {
    Row(
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 6.dp)
                .align(Alignment.CenterVertically),
        ) {
            title()
        }
        Box(
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.CenterVertically),
            contentAlignment = Alignment.Center,
        ) {
            trailing()
        }
    }
}