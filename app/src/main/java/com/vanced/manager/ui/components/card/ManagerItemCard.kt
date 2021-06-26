package com.vanced.manager.ui.components.card

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanced.manager.ui.components.color.ThemedItemContentColorProvider
import com.vanced.manager.ui.components.color.managerAccentColor

private val cardModifier = Modifier.sizeIn(
    minHeight = 95.dp,
    minWidth = 95.dp
)

@Composable
fun ManagerItemCard(
    title: String,
    @DrawableRes icon: Int? = null
) {
    ManagerThemedCard(
        modifier = cardModifier,
    ) {
        ManagerItemCardContent(title, icon)
    }
}

@Composable
fun ManagerClickableItemCard(
    title: String,
    @DrawableRes icon: Int? = null,
    onClick: () -> Unit
) {
    ManagerClickableThemedCard(
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
    Canvas(modifier = Modifier.requiredSize(72.dp)) {
        drawCircle(
            color = managerAccentColor(),
            center = Offset(32f,32f)
        )
    }
    Box(
        modifier = Modifier.padding(12.dp)
    ) {
        if (icon != null) {
            ThemedItemContentColorProvider {
                Icon(
                    modifier = Modifier
                        .size(28.dp)
                        .align(Alignment.TopStart),
                    painter = painterResource(id = icon),
                    contentDescription = title
                )
            }
        }
        Text(
            modifier = Modifier.align(Alignment.BottomStart),
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.onSurface
        )
    }
}