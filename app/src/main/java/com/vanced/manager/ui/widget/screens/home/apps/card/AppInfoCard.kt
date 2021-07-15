package com.vanced.manager.ui.widget.screens.home.apps.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import com.vanced.manager.ui.component.card.ManagerCard
import com.vanced.manager.ui.component.list.ManagerListItem
import com.vanced.manager.ui.component.modifier.managerPlaceholder
import com.vanced.manager.ui.component.text.ManagerText
import com.vanced.manager.ui.util.defaultContentPaddingHorizontal

@OptIn(ExperimentalCoilApi::class)
@Composable
fun AppInfoCard(
    appName: String?,
    icon: ImagePainter,
) {
    ManagerCard {
        ManagerListItem(
            modifier = Modifier.padding(
                horizontal = defaultContentPaddingHorizontal,
                vertical = 12.dp
            ),
            title = {
                ManagerText(
                    modifier = Modifier.managerPlaceholder(appName == null),
                    text = appName ?: "",
                    textStyle = MaterialTheme.typography.h5
                )
            },
            icon = {
                Image(
                    modifier = Modifier
                        .size(48.dp)
                        .managerPlaceholder(icon.state is ImagePainter.State.Loading),
                    painter = icon,
                    contentDescription = "",
                )
            }
        )
    }
}