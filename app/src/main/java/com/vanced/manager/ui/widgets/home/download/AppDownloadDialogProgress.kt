package com.vanced.manager.ui.widgets.home.download

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AppDownloadDialogProgress(
    progress: Float,
    file: String,
    showProgress: Boolean,
    installing: Boolean
) {
    if (showProgress) {
        when (installing) {
            true -> LinearProgressIndicator(color = MaterialTheme.colors.primary)
            false -> LinearProgressIndicator(
                progress = progress,
                color = MaterialTheme.colors.primary
            )
        }
        Row {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start),
                text = "Downloading $file"
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.End),
                text = "$progress"
            )
        }
    }
}