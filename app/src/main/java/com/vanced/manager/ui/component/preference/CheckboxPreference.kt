package com.vanced.manager.ui.component.preference

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.vanced.manager.ui.widget.checkbox.ManagerAnimatedCheckbox

@Composable
fun CheckboxPreference(
    preferenceTitle: String,
    preferenceDescription: String? = null,
    isChecked: Boolean,
    onCheckedChange: (isChecked: Boolean) -> Unit = {}
) {
    val onClick: () -> Unit = {
        onCheckedChange(!isChecked)
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