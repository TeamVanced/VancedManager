package com.vanced.manager.ui.composables

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.vanced.manager.ui.theme.cardColor
import com.vanced.manager.ui.theme.managerAccentColor
import java.util.*

@Composable
fun managerShape() = RoundedCornerShape(12.dp)

@Composable
fun ManagerCard(
    modifier: Modifier = Modifier,
    shape: Shape = managerShape(),
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = shape,
        backgroundColor = managerCardColor(),
        elevation = 0.dp,
        content = content
    )
}

@Composable
fun ManagerThemedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    val accentColor = managerAccentColor()
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = managerShape(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = managerAccentColor()
        ),
        elevation = ButtonDefaults.elevation(0.dp)
    ) {
        CompositionLocalProvider(LocalContentColor provides if (accentColor.luminance() > 0.7) Color.Black else Color.White) {
            content()
        }
    }
}

@Composable
fun ManagerDialog(
    title: String,
    isShown: MutableState<Boolean>,
    buttons: @Composable ColumnScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(
        onDismissRequest = { isShown.value = false },
        content = {
            ManagerCard {
                Column(
                    modifier = Modifier.padding(all = 8.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = title,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold,
                        color = managerTextColor().copy(alpha = 0.8f),
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    content()
                    Spacer(Modifier.height(8.dp))
                    buttons()
                }
            }
        }
    )
}

@Composable
fun ManagerSurface(
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = managerSurfaceColor(),
        content = content
    )
}

@Composable
fun ManagerLazyColumn(
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        content = content
    )
}

@Composable
fun ManagerScrollableColumn(
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content
    )
}

@Composable
fun ManagerDropdownMenuItem(
    isMenuExpanded: MutableState<Boolean>,
    title: String,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        onClick = {
            isMenuExpanded.value = false
            onClick()
        },
    ) {
        Text(text = title)
    }
}

@Composable
fun animateManagerColor(
    color: Color
): State<Color> {
    return animateColorAsState(
        targetValue = color,
        animationSpec = tween(500)
    )
}

@Composable
fun HomeHeaderView(
    modifier: Modifier = Modifier,
    headerName: String,
    content: @Composable () -> Unit
) {
    HeaderView(
        modifier = modifier,
        headerName = headerName,
        headerPadding = 4.dp
    ) {
        ManagerHomeHeaderSeparator()
        content()
    }
}

@Composable
fun managerAnimatedColor(
    color: Color
): Color {
    val animColor by animateColorAsState(
        targetValue = color,
        animationSpec = tween(500)
    )
    return animColor
}

@Composable
fun managerAnimatedText(
    @StringRes stringId: Int
): String {
    var text by remember { mutableStateOf("") }
    CompositionLocalProvider(
        LocalConfiguration provides LocalConfiguration.current.apply {
            setLocale(Locale("ru"))
        }
    ) {
        text = stringResource(id = stringId)
    }
    return text
}

@Composable
fun ManagerCardSeparator() {
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun ManagerHomeHeaderSeparator() {
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
fun managerTextColor(): Color = managerAnimatedColor(color = MaterialTheme.colors.onSurface)

@Composable
fun managerSurfaceColor(): Color = managerAnimatedColor(color = MaterialTheme.colors.surface)

@Composable
fun managerCardColor(): Color = managerAnimatedColor(color = MaterialTheme.colors.cardColor)

@Composable
fun managerAccentColor(): Color = MaterialTheme.colors.managerAccentColor

@Composable
fun animatedManagerAccentColor(): Color = managerAnimatedColor(color = MaterialTheme.colors.managerAccentColor)