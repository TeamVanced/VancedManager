package com.vanced.manager.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanced.manager.R
import com.vanced.manager.domain.model.InstallationOption
import com.vanced.manager.ui.component.card.ManagerTonalCard
import com.vanced.manager.ui.component.text.ManagerText
import com.vanced.manager.ui.component.topappbar.ManagerTopAppBar
import com.vanced.manager.ui.resources.managerString
import com.vanced.manager.ui.theme.LargeShape
import com.vanced.manager.ui.util.DefaultContentPaddingHorizontal
import com.vanced.manager.ui.util.DefaultContentPaddingVertical
import com.vanced.manager.ui.viewmodel.ConfigurationViewModel
import com.vanced.manager.ui.widget.layout.managerCategory
import org.koin.androidx.compose.getViewModel

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigurationScreen(
    installationOptions: List<InstallationOption>,
    onToolbarBackButtonClick: () -> Unit,
    onFinishClick: () -> Unit,
) {
    val viewModel: ConfigurationViewModel = getViewModel()
    Scaffold(
        topBar = {
            ManagerTopAppBar(
                title = managerString(R.string.toolbar_installation_preferences),
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
            ConfigurationButtons(
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

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
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
        val categoryName = managerString(installationOption.titleId)
        LazyColumn {
            when (installationOption) {
                is InstallationOption.SingleSelect -> {
                    managerCategory(
                        categoryName = categoryName,
                        items = installationOption.items
                    ) { item ->
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
                    managerCategory(
                        categoryName = categoryName,
                        items = installationOption.items
                    ) { item ->
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ConfigurationButtons(
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
            visible = currentIndex > 0) {
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ConfigurationItem(
    text: String,
    onClick: () -> Unit,
    trailing: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    ManagerTonalCard(
        modifier = modifier
            .padding(
                start = 6.dp,
                end = 6.dp,
                bottom = 8.dp
            ),
        shape = LargeShape,
        onClick = onClick
    ) {
        ListItem(
            text = {
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
    animationSpec = tween(400)
) + fadeIn(
    animationSpec = tween(400)
) with slideOutOfContainer(
    towards = slideDirection,
    animationSpec = tween(400)
) + fadeOut(
    animationSpec = tween(400)
)