package com.vanced.manager.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.vanced.manager.R
import com.vanced.manager.ui.component.*
import com.vanced.manager.ui.resource.managerString
import com.vanced.manager.ui.util.DefaultContentPaddingHorizontal
import com.vanced.manager.ui.viewmodel.InstallViewModel

@Composable
fun InstallScreen(
    appName: String,
    appVersions: List<String>?,
    viewModel: InstallViewModel,
    onFinishClick: () -> Unit
) {
    var startedProcess by rememberSaveable { mutableStateOf(false) }

    val logs = viewModel.logs
    val status = viewModel.status

    // I don't know why, I don't know how,
    // but it works as intended
    LaunchedEffect(startedProcess) {
        if (!startedProcess) {
            startedProcess = true
            viewModel.startAppProcess(appName, appVersions)
        }
    }

    ManagerScaffold(
        topBar = {
            Column {
                ManagerSmallTopAppBar(
                    title = {
                        ManagerText(managerString(R.string.toolbar_install))
                    },
                )
                when (status) {
                    is InstallViewModel.Status.Progress -> {
                        ManagerProgressIndicator(status.progress)
                    }
                    is InstallViewModel.Status.Installing -> {
                        ManagerProgressIndicator()
                    }
                    else -> {}
                }
            }
        },
        floatingActionButton = {
            if (status is InstallViewModel.Status.Installed) {
                ExtendedFloatingActionButton(
                    text = { ManagerText("Finish") },
                    icon = {
                        Icon(Icons.Rounded.Done, null)
                    },
                    onClick = onFinishClick,
                )
            }
        }
    ) { paddingValues ->
        ManagerLazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            items(logs) { log ->
                when (log) {
                    is InstallViewModel.Log.Success -> {
                        ManagerText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = DefaultContentPaddingHorizontal),
                            text = log.successText,
                            textStyle = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.tertiary
                            ),
                        )
                    }
                    is InstallViewModel.Log.Info -> {
                        ManagerText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = DefaultContentPaddingHorizontal),
                            text = log.infoText,
                            textStyle = TextStyle(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                        )
                    }
                    is InstallViewModel.Log.Error -> {
                        var visible by remember { mutableStateOf(false) }
                        val iconRotation by animateFloatAsState(if (visible) -90f else 0f)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    visible = !visible
                                }
                                .padding(horizontal = DefaultContentPaddingHorizontal),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ManagerText(
                                    text = buildAnnotatedString {
                                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.error)) {
                                            append(log.displayText)
                                        }
                                    },
                                    textStyle = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                    ),
                                )
                                Icon(
                                    modifier = Modifier.rotate(iconRotation),
                                    imageVector = Icons.Rounded.ArrowDropDown,
                                    contentDescription = "expand",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                            AnimatedVisibility(visible) {
                                ManagerText(
                                    text = log.stacktrace,
                                    textStyle = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
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