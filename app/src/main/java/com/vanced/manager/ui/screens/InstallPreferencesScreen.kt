package com.vanced.manager.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import com.vanced.manager.domain.model.InstallationOption
import com.vanced.manager.ui.component.card.ManagerCard
import com.vanced.manager.ui.component.layout.ManagerLazyColumn
import com.vanced.manager.ui.component.layout.ManagerScrollableColumn
import com.vanced.manager.ui.component.text.ManagerText
import com.vanced.manager.ui.resources.managerString
import com.vanced.manager.ui.widget.list.CheckboxItem
import com.vanced.manager.ui.widget.list.RadiobuttonItem

@Composable
fun InstallPreferencesScreen(
    installationOptions: List<InstallationOption>
) {
    var selectedOptionIndex by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        floatingActionButton = {

        }
    ) { paddingValues ->
        ManagerScrollableColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            installationOptions.fastForEachIndexed { index, installationOption ->
                ManagerCard(onClick = {
                    selectedOptionIndex = index
                }) {
                    Column {
                        ManagerText(
                            text = managerString(installationOption.itemTitleId),
                            textStyle = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        AnimatedVisibility(
                            visible = index == selectedOptionIndex
                        ) {
                            ManagerLazyColumn(
                                modifier = Modifier.sizeIn(
                                    minHeight = 400.dp,
                                    maxHeight = 400.dp
                                )
                            ) {
                                when (installationOption) {
                                    is InstallationOption.MultiSelect -> {
                                        items(installationOption.items) { item ->
                                            val preference = installationOption.getOption()
                                            CheckboxItem(
                                                modifier = Modifier.fillMaxWidth(),
                                                text = item.displayText,
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
                                        items(installationOption.items) { item ->
                                            val preference = installationOption.getOption()
                                            RadiobuttonItem(
                                                modifier = Modifier.fillMaxWidth(),
                                                text = item.displayText,
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