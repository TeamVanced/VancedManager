package com.vanced.manager.ui.component.layout

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@ExperimentalMaterial3Api
@Composable
fun ManagerScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    content: @Composable (PaddingValues) -> Unit
) {
//    //M3 Scaffold doesn't support tonal elevation for Surface
//    val absoluteTonalElevation = LocalAbsoluteTonalElevation.current + 1.dp
//    CompositionLocalProvider(
//        LocalAbsoluteTonalElevation provides absoluteTonalElevation
//    ) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        content = content
    )
//    }
}