package com.vanced.manager.ui.component.card

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.github.zsoltk.compose.backpress.LocalBackPressHandler
import com.vanced.manager.ui.component.text.ManagerText
import com.vanced.manager.ui.util.DefaultContentPaddingHorizontal
import com.vanced.manager.ui.util.DefaultContentPaddingVertical

private val cardModifier = Modifier.sizeIn(
    minHeight = 100.dp,
    minWidth = 100.dp
)

@Composable
fun ManagerItemCard(
    title: String,
    @DrawableRes icon: Int? = null
) {
    LocalBackPressHandler.current.handle()
    ManagerCard(
        modifier = cardModifier,
    ) {
        ManagerItemCardContent(title, icon)
    }
}

@Composable
fun ManagerItemCard(
    title: String,
    @DrawableRes icon: Int? = null,
    onClick: () -> Unit
) {
    ManagerTonalCard(
        modifier = cardModifier,
        onClick = onClick
    ) {
        ManagerItemCardContent(title, icon)
    }
}

@Composable
private fun ManagerItemCardContent(
    title: String,
    @DrawableRes icon: Int? = null,
) {
    Column(
        modifier = Modifier.padding(
            horizontal = DefaultContentPaddingHorizontal,
            vertical = DefaultContentPaddingVertical
        ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (icon != null) {
            Icon(
                modifier = Modifier
                    .size(32.dp),
                painter = painterResource(id = icon),
                contentDescription = title,
            )
        }
        ManagerText(
            text = title,
            textStyle = MaterialTheme.typography.labelLarge
        )
    }
}