package com.vanced.manager.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.vanced.manager.R
import com.vanced.manager.domain.model.InstallationOption
import com.vanced.manager.ui.component.*
import com.vanced.manager.ui.resource.managerString
import com.vanced.manager.ui.theme.LargeShape
import com.vanced.manager.ui.util.DefaultContentPaddingHorizontal
import com.vanced.manager.ui.util.DefaultContentPaddingVertical
import com.vanced.manager.ui.viewmodel.ConfigurationViewModel
import org.koin.androidx.compose.getViewModel

private const val enterDuration = 300
private const val exitDuration = 250

@Composable
fun ConfigurationScreen(
    installationOptions: List<InstallationOption>,
    onToolbarBackButtonClick: () -> Unit,
    onFinishClick: () -> Unit,
) {
    val viewModel: ConfigurationViewModel = getViewModel()
    Scaffold(
        topBar = {
            ManagerSmallTopAppBar(
                title = {
                    ManagerText(managerString(R.string.toolbar_installation_preferences))
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onToolbarBackButtonClick()
                            viewModel.reset()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            ConfigurationBottomBar(
                modifier = Modifier
                    .padding(
                        horizontal = DefaultContentPaddingHorizontal,
                        vertical = DefaultContentPaddingVertical
                    ),
                lastIndex = installationOptions.lastIndex,
                currentIndex = viewModel.currentIndex,
                onBackClick = {
                    viewModel.back()
                },
                onNextClick = {
                    viewModel.next()
                },
                onFinishClick = {
                    onFinishClick()
                    viewModel.reset()
                }
            )
        }
    ) { paddingValues ->
        ConfigurationBody(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            currentIndex = viewModel.currentIndex,
            installationOptions = installationOptions
        )
    }
}

@Composable
private fun ConfigurationBody(
    currentIndex: Int,
    installationOptions: List<InstallationOption>,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        modifier = modifier,
        targetState = currentIndex,
        transitionSpec = {
            slideAnimationSpec(
                if (targetState > initialState) {
                    AnimatedContentScope.SlideDirection.Start
                } else {
                    AnimatedContentScope.SlideDirection.End
                }
            )
        }
    ) { optionIndex ->
        val installationOption = installationOptions[optionIndex]
        ManagerLazyColumn {
            managerCategory(categoryName = {
                managerString(installationOption.titleId)
            }) {
                when (installationOption) {
                    is InstallationOption.SingleSelect -> {
                        items(installationOption.items) { item ->
                            val preference = installationOption.getOption()
                            ConfigurationItem(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = item.displayText(item.key),
                                onClick = {
                                    installationOption.setOption(item.key)
                                },
                                trailing = {
                                    RadioButton(
                                        selected = preference == item.key,
                                        onClick = null
                                    )
                                }
                            )
                        }
                    }
                    is InstallationOption.MultiSelect -> {
                        items(installationOption.items) { item ->
                            val preference = installationOption.getOption()
                            ConfigurationItem(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = item.displayText(item.key),
                                onClick = {
                                    if (preference.contains(item.key)) {
                                        installationOption.removeOption(item.key)
                                    } else {
                                        installationOption.addOption(item.key)
                                    }
                                },
                                trailing = {
                                    Checkbox(
                                        checked = preference.contains(item.key),
                                        onCheckedChange = null
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ConfigurationBottomBar(
    currentIndex: Int,
    lastIndex: Int,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    onFinishClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        AnimatedVisibility(
            modifier = Modifier
                .wrapContentWidth(Alignment.Start)
                .weight(1f),
            visible = currentIndex > 0,
            enter = fadeIn(tween(enterDuration)) +
                    expandHorizontally(tween(enterDuration)) +
                    scaleIn(tween(enterDuration)),
            exit = fadeOut(tween(exitDuration)) +
                    shrinkHorizontally(tween(exitDuration)) +
                    scaleOut(tween(exitDuration)),

            ) {
            TextButton(onClick = onBackClick) {
                ManagerText(text = "Back")
            }
        }
        AnimatedContent(
            modifier = Modifier
                .wrapContentWidth(Alignment.End)
                .weight(1f),
            targetState = currentIndex == lastIndex,
            transitionSpec = {
                slideAnimationSpec(
                    if (initialState && !targetState) {
                        AnimatedContentScope.SlideDirection.Up
                    } else {
                        AnimatedContentScope.SlideDirection.Down
                    }
                )
            }
        ) { isLastIndex ->
            if (isLastIndex) {
                ElevatedButton(onClick = onFinishClick) {
                    ManagerText(text = "Finish")
                }
            } else {
                OutlinedButton(onClick = onNextClick) {
                    ManagerText(text = "Next")
                }
            }
        }
    }
}

@Composable
private fun ConfigurationItem(
    text: String,
    onClick: () -> Unit,
    trailing: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    ManagerElevatedCard(
        modifier = modifier,
        shape = LargeShape,
        onClick = onClick
    ) {
        ManagerListItem(
            modifier = Modifier.padding(
                horizontal = DefaultContentPaddingHorizontal
            ),
            title = {
                ManagerText(
                    text = text,
                    textStyle = MaterialTheme.typography.titleSmall
                )
            },
            trailing = trailing,
        )
    }
}

@ExperimentalAnimationApi
private fun <S> AnimatedContentScope<S>.slideAnimationSpec(
    slideDirection: AnimatedContentScope.SlideDirection
) = slideIntoContainer(
    towards = slideDirection,
    animationSpec = tween(enterDuration)
) + fadeIn(
    animationSpec = tween(enterDuration)
) with slideOutOfContainer(
    towards = slideDirection,
    animationSpec = tween(exitDuration)
) + fadeOut(
    animationSpec = tween(exitDuration)
)