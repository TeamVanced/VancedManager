package com.vanced.manager.ui.widget.settings

import androidx.compose.runtime.*
import com.vanced.manager.R
import com.vanced.manager.core.preferences.RadioButtonPreference
import com.vanced.manager.core.preferences.holder.managerVariantPref
import com.vanced.manager.core.util.isMagiskInstalled
import com.vanced.manager.ui.component.preference.SingleSelectDialogPreference
import com.vanced.manager.ui.resources.managerString

@Composable
fun SettingsManagerVariantItem() {
    var showDialog by remember { mutableStateOf(false) }
    var selectedKey by remember { mutableStateOf(managerVariantPref) }
    SingleSelectDialogPreference(
        preferenceTitle = managerString(
            stringId = R.string.settings_preference_variant_title
        ),
        preferenceDescription = managerVariantPref,
        isDialogVisible = showDialog,
        currentSelectedKey = selectedKey,
        buttons = listOf(
            RadioButtonPreference(
                title = "nonroot",
                key = "nonroot"
            ),
            RadioButtonPreference(
                title = "root",
                key = "root"
            ),
        ),
        onPreferenceClick = {
            showDialog = true
        },
        onDismissRequest = {
            showDialog = false
            selectedKey = managerVariantPref
        },
        onItemClick = {
            if (it == "root" && !isMagiskInstalled)
                return@SingleSelectDialogPreference

            selectedKey = it
        },
        onSave = {
            managerVariantPref = selectedKey
            showDialog = false
        }
    )
}