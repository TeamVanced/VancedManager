package com.vanced.manager.ui.widget.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.vanced.manager.ui.component.modifier.managerPlaceholder
import com.vanced.manager.ui.component.text.AppVersionText
import com.vanced.manager.ui.component.text.ManagerText
import com.vanced.manager.ui.theme.MediumShape
import com.vanced.manager.ui.theme.SmallShape

@Composable
fun AppCardPlaceholder() {
    BaseAppCard(
        appTitle = {
            ManagerText(
                modifier = Modifier
                    .clip(MediumShape)
                    .managerPlaceholder(true),
                text = " ".repeat(40),
                textStyle = MaterialTheme.typography.titleMedium
            )
        },
        appIcon = {
            Box(
                Modifier
                    .clip(MediumShape)
                    .managerPlaceholder(true)
                    .size(48.dp)
            )
        },
        appTrailing = {
            Box(
                Modifier
                    .clip(CircleShape)
                    .managerPlaceholder(true)
                    .size(24.dp)
            )
        },
        appVersionsColumn = {
            AppVersionText(
                modifier = Modifier
                    .managerPlaceholder(true)
                    .clip(SmallShape),
                text = " ".repeat(30)
            )
            AppVersionText(
                modifier = Modifier
                    .managerPlaceholder(true)
                    .clip(SmallShape),
                text = " ".repeat(30)
            )
        },
        appActionsRow = {
            Box(
                Modifier
                    .clip(MediumShape)
                    .fillMaxWidth(0.8f)
                    .height(36.dp)
                    .managerPlaceholder(true)
            )
        }
    )
}