package com.vanced.manager.ui.widgets.home.download

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.vanced.manager.ui.components.color.managerAccentColor

@Composable
fun AppDownloadDialogProgress(
    progress: Float,
    file: String,
    showProgress: Boolean,
    installing: Boolean
) {
    if (showProgress) {
        when (installing) {
            true -> LinearProgressIndicator(color = managerAccentColor())
            false -> LinearProgressIndicator(
                progress = progress,
                color = managerAccentColor()
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