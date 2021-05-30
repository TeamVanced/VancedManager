package com.vanced.manager.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.vanced.manager.downloader.base.BaseDownloader
import dev.burnoo.compose.rememberpreference.rememberStringPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class InstallationOptionKey(
    val keyName: String,
    val keyDefaultValue: String = "",
    val keyInitialValue: String = keyDefaultValue
)

data class InstallationOption(
    val title: String,
    val descriptionKey: InstallationOptionKey,
    val dialog: @Composable (show: MutableState<Boolean>, preference: MutableState<String>) -> Unit
) {

    @Composable
    fun Content() {
        val showDialog = remember { mutableStateOf(false) }
        val preference = rememberStringPreference(
            keyName = descriptionKey.keyName,
            defaultValue = descriptionKey.keyDefaultValue,
            initialValue = descriptionKey.keyInitialValue
        )
        ManagerListItem(
            modifier = Modifier.clickable {
                showDialog.value = true
            },
            title = title,
            description = preference.value,
            trailing = {
                Icon(imageVector = Icons.Default.ArrowForwardIos, contentDescription = null)
            }
        )
        if (showDialog.value) {
            dialog(showDialog, preference)
        }
    }
}

@Composable
fun AppDownloadDialogButtons(
    showProgress: MutableState<Boolean>,
    showDialog: MutableState<Boolean>,
    downloader: BaseDownloader,
    coroutineScope: CoroutineScope
) {
    when(showProgress.value) {
        true -> ManagerThemedButton(onClick = {
            downloader.cancelDownload()
            showDialog.value = false
            showProgress.value = false
        }) {
            Text(text = "Cancel")
        }
        false -> ManagerThemedButton(onClick = {
            coroutineScope.launch {
                showProgress.value = true
                downloader.download()
            }
        }) {
            Text(text = "Download")
        }
    }
}

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