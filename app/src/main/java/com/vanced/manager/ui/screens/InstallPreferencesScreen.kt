package com.vanced.manager.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.NavigateBefore
import androidx.compose.material.icons.rounded.NavigateNext
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanced.manager.R
import com.vanced.manager.domain.model.InstallationOption
import com.vanced.manager.ui.component.card.ManagerTonalCard
import com.vanced.manager.ui.component.layout.ManagerLazyColumn
import com.vanced.manager.ui.component.layout.ManagerScaffold
import com.vanced.manager.ui.component.text.ManagerText
import com.vanced.manager.ui.component.topappbar.ManagerTopAppBar
import com.vanced.manager.ui.resources.managerString
import com.vanced.manager.ui.theme.LargeShape
import com.vanced.manager.ui.util.DefaultContentPaddingHorizontal
import com.vanced.manager.ui.util.DefaultContentPaddingVertical
import com.vanced.manager.ui.widget.list.CheckboxItem
import com.vanced.manager.ui.widget.list.RadiobuttonItem

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstallPreferencesScreen(
    installationOptions: List<InstallationOption>,
    onToolbarBackButtonClick: () -> Unit,
    onDoneClick: () -> Unit,
) {
    var currentOptionIndex by rememberSaveable { mutableStateOf(0) }

    ManagerScaffold(
        topBar = {
            ManagerTopAppBar(
                title = managerString(R.string.toolbar_installation_preferences),
                navigationIcon = {
                    IconButton(
                        onClick = onToolbarBackButtonClick
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            AnimatedContent(
                modifier = Modifier
                    .padding(
                        horizontal = DefaultContentPaddingHorizontal,
                        vertical = DefaultContentPaddingVertical
                    )
                    .weight(1f),
                targetState = currentOptionIndex,
                transitionSpec = {
                    getSlideAnimationSpec(
                        if (targetState > initialState) {
                            AnimatedContentScope.SlideDirection.Start
                        } else {
                            AnimatedContentScope.SlideDirection.End
                        }
                    )
                }
            ) { optionIndex ->
                val installationOption = installationOptions[optionIndex]
                ManagerTonalCard(
                    modifier = Modifier
                        .wrapContentHeight(
                            align = Alignment.Top
                        ),
                    shape = LargeShape
                ) {
                    Column {
                        ManagerText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = DefaultContentPaddingHorizontal,
                                    vertical = DefaultContentPaddingVertical
                                ),
                            text = managerString(installationOption.itemTitleId),
                            textStyle = MaterialTheme.typography.titleLarge,
                        )
                        ManagerLazyColumn(
                            contentPadding = PaddingValues(
                                start = 4.dp,
                                end = 4.dp,
                                bottom = DefaultContentPaddingVertical
                            )
                        ) {
                            when (installationOption) {
                                is InstallationOption.MultiSelect -> {
                                    items(installationOption.items) { item ->
                                        val preference = installationOption.getOption()
                                        CheckboxItem(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = item.displayText(item.key),
                                            checked = preference.contains(item.key),
                                            onCheckedChange = {
                                                if (it) {
                                                    installationOption.addOption(item.key)
                                                } else {
                                                    installationOption.removeOption(item.key)
                                                }
                                            }
                                        )
                                    }
                                }
                                is InstallationOption.SingleSelect -> {
                                    items(installationOption.items) { item ->
                                        val preference = installationOption.getOption()
                                        RadiobuttonItem(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = item.displayText(item.key),
                                            selected = preference == item.key,
                                            onClick = {
                                                installationOption.setOption(item.key)
                                            },
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = DefaultContentPaddingHorizontal,
                        vertical = DefaultContentPaddingVertical
                    ),
            ) {
                AnimatedVisibility(
                    visible = currentOptionIndex > 0
                ) {
                    FloatingActionButton(
                        onClick = {
                            currentOptionIndex--
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.NavigateBefore,
                            contentDescription = "Back"
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                AnimatedContent(
                    targetState = currentOptionIndex == installationOptions.lastIndex,
                    transitionSpec = {

                        getSlideAnimationSpec(
                            if (initialState && !targetState) {
                                AnimatedContentScope.SlideDirection.Up
                            } else {
                                AnimatedContentScope.SlideDirection.Down
                            }
                        )
                    }
                ) { lastIndex ->
                    if (lastIndex) {
                        FloatingActionButton(
                            onClick = onDoneClick
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Done,
                                contentDescription = "Done"
                            )
                        }
                    } else {
                        FloatingActionButton(
                            onClick = {
                                currentOptionIndex++
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.NavigateNext,
                                contentDescription = "Next"
                            )
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalAnimationApi
private fun <S> AnimatedContentScope<S>.getSlideAnimationSpec(
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