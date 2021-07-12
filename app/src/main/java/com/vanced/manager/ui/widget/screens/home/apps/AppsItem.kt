package com.vanced.manager.ui.widget.screens.home.apps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.vanced.manager.domain.model.App
import com.vanced.manager.ui.viewmodel.HomeViewModel
import com.vanced.manager.ui.widget.screens.home.apps.card.AppCard

@Composable
fun HomeAppsItem(
    viewModel: HomeViewModel
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        viewModel.apps.fastForEach { app ->
            val observedApp by app.observeAsState(initial = App())
            AppCard(observedApp, viewModel)
        }
    }
}