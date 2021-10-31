package com.vanced.manager.ui.widget.app

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.vanced.manager.ui.component.modifier.managerPlaceholder
import com.vanced.manager.ui.component.text.AppVersionText
import com.vanced.manager.ui.component.text.ManagerText

@Composable
fun AppCardPlaceholder() {
    BaseAppCard(
        appTitle = {
            ManagerText(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .managerPlaceholder(true),
                text = " ".repeat(40),
                textStyle = MaterialTheme.typography.h5
            )
        },
        appIcon = {
            Box(
                Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .managerPlaceholder(true)
                    .size(48.dp)
            )
        },
        appVersionsColumn = {
            AppVersionText(
                modifier = Modifier
                    .managerPlaceholder(true)
                    .clip(MaterialTheme.shapes.small),
                text = " ".repeat(30)
            )
            AppVersionText(
                modifier = Modifier
                    .managerPlaceholder(true)
                    .clip(MaterialTheme.shapes.small),
                text = " ".repeat(30)
            )
        },
        appActionsRow = {
            Box(
                Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .fillMaxWidth(0.8f)
                    .height(36.dp)
                    .managerPlaceholder(true)
            )
        }
    )
}