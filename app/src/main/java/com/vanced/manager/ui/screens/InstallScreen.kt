package com.vanced.manager.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanced.manager.core.downloader.impl.MicrogDownloader
import com.vanced.manager.core.downloader.impl.MusicDownloader
import com.vanced.manager.core.downloader.impl.VancedDownloader
import com.vanced.manager.core.downloader.util.DownloadStatus
import com.vanced.manager.core.installer.impl.MicrogInstaller
import com.vanced.manager.core.installer.impl.MusicInstaller
import com.vanced.manager.core.installer.impl.VancedInstaller
import com.vanced.manager.network.util.MICROG_NAME
import com.vanced.manager.network.util.MUSIC_NAME
import com.vanced.manager.network.util.VANCED_NAME
import com.vanced.manager.ui.component.layout.ManagerLazyColumn
import com.vanced.manager.ui.component.modifier.managerClickable
import com.vanced.manager.ui.component.progressindicator.ManagerProgressIndicator
import com.vanced.manager.ui.component.text.ManagerText
import com.vanced.manager.ui.viewmodel.MainViewModel
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

sealed class Log {
    data class Info(val infoText: String) : Log()
    data class Success(val successText: String) : Log()
    data class Error(
        val displayText: String,
        val stacktrace: String,
    ) : Log()
}

@Composable
fun InstallScreen(
    appName: String,
    appVersions: List<String>
) {
    val logs = rememberSaveable { mutableStateListOf<Log>() }

    var progress by rememberSaveable { mutableStateOf(0f) }
    var installing by rememberSaveable { mutableStateOf(false) }

    val viewModel: MainViewModel = getViewModel()

    val downloader = when (appName) {
        VANCED_NAME -> get<VancedDownloader>()
        MUSIC_NAME -> get<MusicDownloader>()
        MICROG_NAME -> get<MicrogDownloader>()
        else -> throw IllegalArgumentException("$appName is not a valid app")
    }

    val installer = when (appName) {
        VANCED_NAME -> get<VancedInstaller>()
        MUSIC_NAME -> get<MusicInstaller>()
        MICROG_NAME -> get<MicrogInstaller>()
        else -> throw IllegalArgumentException("$appName is not a valid app")
    }

    //FIXME this is absolutely bad, must move to WorkManager
    LaunchedEffect(true) {
        downloader.download(appVersions) { status ->
            when (status) {
                is DownloadStatus.File -> logs.add(Log.Info("Downloading ${status.fileName}"))
                is DownloadStatus.Error -> logs.add(Log.Error(
                    displayText = status.displayError,
                    stacktrace = status.stacktrace
                ))
                is DownloadStatus.Progress -> progress = status.progress
                is DownloadStatus.StartInstall -> {
                    installing = true
                    installer.install {
                        viewModel.fetch()
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            if (installing) {
                ManagerProgressIndicator()
            } else {
                ManagerProgressIndicator(progress)
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {

            }
        }
    ) { paddingValues ->
        ManagerLazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            items(logs) { log ->
                when (log) {
                    is Log.Success -> {
                        ManagerText(
                            text = log.successText,
                            textStyle = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.Green
                            ),
                        )
                    }
                    is Log.Info -> {
                        ManagerText(
                            text = log.infoText,
                            textStyle = TextStyle(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = Color.Green
                            ),
                        )
                    }
                    is Log.Error -> {
                        var visible by remember { mutableStateOf(false) }
                        val iconRotation by animateFloatAsState(if (visible) 0f else 90f)
                        Row(
                            modifier = Modifier.managerClickable {
                                visible = !visible
                            },
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                modifier = Modifier.rotate(iconRotation),
                                imageVector = Icons.Rounded.ArrowDropDown,
                                contentDescription = "expand",
                            )
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                ManagerText(
                                    text = buildAnnotatedString {
                                        withStyle(SpanStyle(color = MaterialTheme.colors.error)) {
                                            append(log.displayText)
                                            append(": ")
                                        }
                                        withStyle(SpanStyle(color = MaterialTheme.colors.error.copy(alpha = 0.7f))) {
                                            append(log.stacktrace)
                                        }
                                    },
                                    textStyle = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.Green
                                    ),
                                )
                                AnimatedVisibility(visible) {
                                    ManagerText(
                                        text = log.stacktrace,
                                        textStyle = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color.Green
                                        ),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}