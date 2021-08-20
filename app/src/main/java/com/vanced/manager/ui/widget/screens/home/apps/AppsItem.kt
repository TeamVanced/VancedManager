package com.vanced.manager.ui.widget.screens.home.apps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.vanced.manager.ui.viewmodel.MainViewModel
import com.vanced.manager.ui.widget.screens.home.apps.card.AppCard

@Composable
fun HomeAppsItem(
    viewModel: MainViewModel
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        viewModel.apps.fastForEach { app ->
            val observedApp by app.collectAsState()
            AppCard(observedApp, viewModel)
        }
    }
}