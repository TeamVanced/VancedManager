package com.vanced.manager.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.vanced.manager.ui.theme.managerAccentColor

@ExperimentalStdlibApi
@Composable
fun HeaderCard(
    headerName: String,
    headerPadding: Dp = 11.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    ManagerCard(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {
            Spacer(modifier = Modifier.size(height = 4.dp, width = 0.dp))
            HeaderView(
                headerName = headerName,
                content = content,
                headerPadding = headerPadding
            )
        }
    }
}

@Composable
fun HeaderView(
    modifier: Modifier = Modifier,
    headerName: String,
    headerPadding: Dp = 11.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Header(headerName = headerName, headerPadding = headerPadding)
        content()
    }
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun Header(
    headerName: String,
    headerPadding: Dp = 11.dp,
) {
    Text(
        headerName.uppercase(),
        letterSpacing = 0.15.em,
        color = MaterialTheme.colors.managerAccentColor,
        modifier = Modifier.padding(horizontal = headerPadding),
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp
    )
}

@Composable
fun IconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val buttonSize = 36.dp
    val enabled = true
    Box(
        modifier = Modifier
            .clickable(
                onClick = onClick,
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = false, radius = 18.dp)
            )
            .size(buttonSize),
        contentAlignment = Alignment.Center
    ) {
        val contentAlpha = if (enabled) LocalContentAlpha.current else ContentAlpha.disabled
        CompositionLocalProvider(LocalContentAlpha provides contentAlpha) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription
            )
        }
    }
}

@Composable
fun RadiobuttonItem(
    currentSelection: MutableState<String?>,
    text: String,
    preferenceValue: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                currentSelection.value = preferenceValue
            }
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = preferenceValue == currentSelection.value,
            onClick = {
                currentSelection.value = preferenceValue
            },
            colors = RadioButtonDefaults.colors(
                MaterialTheme.colors.managerAccentColor,
                Color.LightGray
            )
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = text,
            color = managerTextColor(),
            fontSize = 18.sp
        )
    }
}