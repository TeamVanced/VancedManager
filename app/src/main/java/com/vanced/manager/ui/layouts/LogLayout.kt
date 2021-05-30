package com.vanced.manager.ui.layouts

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vanced.manager.ui.composables.ManagerLazyColumn
import com.vanced.manager.ui.composables.managerAccentColor
import com.vanced.manager.ui.composables.managerSurfaceColor
import com.vanced.manager.util.logs

@Composable
fun LogLayout() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                backgroundColor = managerAccentColor()
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "share"
                )
            }
        },
        backgroundColor = managerSurfaceColor()
    ) {
        ManagerLazyColumn {
            items(logs) { log ->
                Text(text = log.body)
            }
        }
    }
}