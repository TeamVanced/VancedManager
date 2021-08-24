package com.vanced.manager.ui.component.preference

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import com.vanced.manager.preferences.ManagerPreference
import com.vanced.manager.ui.widget.checkbox.ManagerAnimatedCheckbox
import kotlinx.coroutines.launch

@Composable
fun CheckboxPreference(
    preferenceTitle: String,
    preferenceDescription: String? = null,
    preference: ManagerPreference<Boolean>,
    onCheckedChange: (isChecked: Boolean) -> Unit = {}
) {
    var isChecked by preference
    val coroutineScope = rememberCoroutineScope()

    val onClick: () -> Unit = {
        coroutineScope.launch {
            isChecked = !isChecked
            onCheckedChange(isChecked)
        }
    }
    Preference(
        preferenceTitle = preferenceTitle,
        preferenceDescription = preferenceDescription,
        onClick = onClick,
        trailing = {
            ManagerAnimatedCheckbox(
                isChecked = isChecked,
                onCheckedChange = { onClick() },
                size = 40.dp
            )
        }
    )
}