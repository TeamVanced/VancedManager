package com.vanced.manager.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import com.vanced.manager.domain.model.InstallationOption
import com.vanced.manager.ui.component.card.ManagerClickableThemedCard
import com.vanced.manager.ui.component.layout.ManagerScrollableColumn
import com.vanced.manager.ui.component.text.ManagerText
import com.vanced.manager.ui.resources.managerString
import com.vanced.manager.ui.util.DefaultContentPaddingHorizontal
import com.vanced.manager.ui.util.DefaultContentPaddingVertical
import com.vanced.manager.ui.widget.list.CheckboxItem
import com.vanced.manager.ui.widget.list.RadiobuttonItem

@Composable
fun InstallPreferencesScreen(
    installationOptions: List<InstallationOption>,
    onDoneClick: () -> Unit
) {
    var selectedOptionIndex by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onDoneClick
            ) {
                Icon(
                    imageVector = Icons.Rounded.Done,
                    contentDescription = "Done"
                )
            }
        }
    ) { paddingValues ->
        ManagerScrollableColumn(
            modifier = Modifier.padding(paddingValues),
            itemSpacing = DefaultContentPaddingVertical
        ) {
            installationOptions.fastForEachIndexed { index, installationOption ->
                ManagerClickableThemedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DefaultContentPaddingHorizontal),
                    onClick = {
                        selectedOptionIndex = index
                    }
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        ManagerText(
                            modifier = Modifier.fillMaxWidth(),
                            text = managerString(installationOption.itemTitleId),
                            textStyle = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            textAlign = TextAlign.Center
                        )
                        AnimatedVisibility(
                            visible = index == selectedOptionIndex
                        ) {
                            ManagerScrollableColumn(
                                modifier = Modifier.sizeIn(
                                    maxHeight = 400.dp
                                )
                            ) {
                                when (installationOption) {
                                    is InstallationOption.MultiSelect -> {
                                        installationOption.items.fastForEach { item ->
                                            val preference = installationOption.getOption()
                                            CheckboxItem(
                                                modifier = Modifier.fillMaxWidth(),
                                                text = item.displayText(item.key),
                                                isChecked = preference.contains(item.key),
                                                onCheck = {
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
                                        installationOption.items.fastForEach { item ->
                                            val preference = installationOption.getOption()
                                            RadiobuttonItem(
                                                modifier = Modifier.fillMaxWidth(),
                                                text = item.displayText(item.key),
                                                isSelected = preference == item.key,
                                                onSelect = {
                                                    installationOption.setOption(item.key)
                                                },
                                                tag = item.key
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
    }


}