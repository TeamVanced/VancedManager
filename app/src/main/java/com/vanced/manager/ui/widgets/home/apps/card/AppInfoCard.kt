package com.vanced.manager.ui.widgets.home.apps.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.imageloading.ImageLoadState
import com.google.accompanist.imageloading.LoadPainter
import com.vanced.manager.ui.components.card.ManagerCard
import com.vanced.manager.ui.components.list.ManagerListItem
import com.vanced.manager.ui.components.placeholder.managerPlaceholder
import com.vanced.manager.ui.components.text.ManagerText
import com.vanced.manager.ui.utils.defaultContentPaddingHorizontal

@Composable
fun AppInfoCard(
    appName: String,
    icon: LoadPainter<Any>,
    fetching: Boolean
) {
    ManagerCard {
        ManagerListItem(
            modifier = Modifier.padding(
                horizontal = defaultContentPaddingHorizontal,
                vertical = 12.dp
            ),
            title = {
                ManagerText(
                    modifier = Modifier.managerPlaceholder(fetching),
                    text = appName,
                    textStyle = MaterialTheme.typography.h5
                )
            },
            icon = {
                Image(
                    painter = icon,
                    contentDescription = "",
                    modifier = Modifier
                        .size(48.dp, 48.dp)
                        .managerPlaceholder(icon.loadState is ImageLoadState.Loading)
                )
            }
        )
    }
}