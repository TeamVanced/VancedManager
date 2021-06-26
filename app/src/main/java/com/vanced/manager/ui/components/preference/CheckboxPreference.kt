package com.vanced.manager.ui.components.preference

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.vanced.manager.ui.components.checkbox.ManagerCheckbox
import com.vanced.manager.ui.preferences.ManagerPreference
import kotlinx.coroutines.launch

@Composable
fun CheckboxPreference(
    @StringRes preferenceTitle: Int,
    @StringRes preferenceDescription: Int? = null,
    preference: ManagerPreference<Boolean>,
    onCheckedChange: (isChecked: Boolean) -> Unit = {}
) {
    val isChecked by preference
    val coroutineScope = rememberCoroutineScope()

    val onClick: () -> Unit = {
        coroutineScope.launch {
            preference.save(!isChecked)
            onCheckedChange(isChecked)
        }
    }

    Preference(
        preferenceTitleId = preferenceTitle,
        preferenceDescriptionId = preferenceDescription,
        onClick = onClick,
        trailing = {
            ManagerCheckbox(
                isChecked = isChecked,
                onCheckedChange = { onClick() }
            )
        }
    )
}

@Composable
fun CheckboxPreference(
    preferenceTitle: String,
    preferenceDescription: String? = null,
    preference: ManagerPreference<Boolean>,
    onCheckedChange: (isChecked: Boolean) -> Unit = {}
) {
    val isChecked by preference
    val coroutineScope = rememberCoroutineScope()

    val onClick: () -> Unit = {
        coroutineScope.launch {
            preference.save(!isChecked)
            onCheckedChange(isChecked)
        }
    }

    Preference(
        preferenceTitle = preferenceTitle,
        preferenceDescription = preferenceDescription,
        onClick = onClick,
        trailing = {
            ManagerCheckbox(
                isChecked = isChecked,
                onCheckedChange = { onClick() }
            )
        }
    )
}