package com.vanced.manager.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.vanced.manager.ui.theme.managerAccentColor
import java.util.*

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

@Composable
fun Header(
    headerName: String,
    headerPadding: Dp = 11.dp,
) {
    Text(
        headerName.uppercase(Locale.ROOT),
        letterSpacing = 0.15.em,
        color = MaterialTheme.colors.managerAccentColor,
        modifier = Modifier.padding(horizontal = headerPadding),
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp
    )
}