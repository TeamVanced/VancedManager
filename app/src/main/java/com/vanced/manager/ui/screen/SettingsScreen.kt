package com.vanced.manager.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vanced.manager.R
import com.vanced.manager.repository.ManagerMode
import com.vanced.manager.repository.ManagerTheme
import com.vanced.manager.ui.component.*
import com.vanced.manager.ui.resource.managerString
import com.vanced.manager.ui.util.Screen
import com.vanced.manager.ui.viewmodel.SettingsViewModel
import com.vanced.manager.util.isMagiskInstalled
import org.koin.androidx.compose.viewModel

@ExperimentalMaterial3Api
@Composable
fun SettingsScreen(
    onToolbarBackButtonClick: () -> Unit,
    onThemeChange: (ManagerTheme) -> Unit,
) {
    val viewModel: SettingsViewModel by viewModel()
    ManagerScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ManagerSmallTopAppBar(
                title = {
                    ManagerText(managerString(Screen.Settings.displayName))
                },
                navigationIcon = {
                    IconButton(onClick = onToolbarBackButtonClick) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        ManagerLazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            managerCategory(categoryName = {
                managerString(R.string.settings_category_behaviour)
            }) {
                item {
                    ManagerSwitchPreference(
                        preferenceTitle = stringResource(id = R.string.settings_preference_use_custom_tabs_title),
                        preferenceDescription = stringResource(id = R.string.settings_preference_use_custom_tabs_summary),
                        isChecked = viewModel.managerUseCustomTabs,
                        onCheckedChange = {
                            viewModel.saveManagerUseCustomTabs(it)
                        }
                    )
                }
                item {
                    var showDialog by remember { mutableStateOf(false) }
                    var selectedMode by remember { mutableStateOf(EntryValue(viewModel.managerMode.value)) }
                    ManagerSingleSelectDialogPreference(
                        preferenceTitle = managerString(
                            stringId = R.string.settings_preference_variant_title
                        ),
                        preferenceDescription = selectedMode.value,
                        showDialog = showDialog,
                        selected = selectedMode,
                        entries = mapOf(
                            EntryText("nonroot") to EntryValue("nonroot"),
                            EntryText("root") to EntryValue("root"),
                        ),
                        onClick = {
                            showDialog = true
                        },
                        onDismissRequest = {
                            showDialog = false
                            selectedMode = EntryValue(viewModel.managerMode.value)
                        },
                        onEntrySelect = {
                            if (it.value == "root" && !isMagiskInstalled)
                                return@ManagerSingleSelectDialogPreference

                            selectedMode = it
                        },
                        onSave = {
                            viewModel.saveManagerMode(ManagerMode.fromValue(selectedMode.value))
                            showDialog = false
                        }
                    )
                }
            }
            managerCategory(categoryName = {
                managerString(R.string.settings_category_appearance)
            }) {
                item {
                    var showDialog by remember { mutableStateOf(false) }
                    var selectedTheme by remember { mutableStateOf(EntryValue(viewModel.managerTheme.value)) }
                    ManagerSingleSelectDialogPreference(
                        preferenceTitle = managerString(stringId = R.string.settings_preference_theme_title),
                        preferenceDescription = managerString(
                            stringId = viewModel.getThemeStringId(
                                ManagerTheme.fromValue(selectedTheme.value)
                            )
                        ),
                        showDialog = showDialog,
                        selected = selectedTheme,
                        entries = mapOf(
                            EntryText(managerString(viewModel.getThemeStringId(ManagerTheme.LIGHT)))
                                    to EntryValue(ManagerTheme.LIGHT.value),
                            EntryText(managerString(viewModel.getThemeStringId(ManagerTheme.DARK)))
                                    to EntryValue(ManagerTheme.DARK.value),
                            EntryText(managerString(viewModel.getThemeStringId(ManagerTheme.SYSTEM_DEFAULT)))
                                    to EntryValue(ManagerTheme.SYSTEM_DEFAULT.value),
                        ),
                        onClick = {
                            showDialog = true
                        },
                        onDismissRequest = {
                            showDialog = false
                            selectedTheme = EntryValue(viewModel.managerTheme.value)
                        },
                        onEntrySelect = {
                            selectedTheme = it
                        },
                        onSave = {
                            showDialog = false
                            viewModel.saveManagerTheme(ManagerTheme.fromValue(selectedTheme.value))
                            onThemeChange(ManagerTheme.fromValue(selectedTheme.value))
                        }
                    )
                }
            }
        }
    }
}