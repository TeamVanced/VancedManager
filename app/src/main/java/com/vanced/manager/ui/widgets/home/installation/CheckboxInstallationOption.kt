package com.vanced.manager.ui.widgets.home.installation

import androidx.annotation.StringRes
import com.vanced.manager.ui.components.preference.CheckboxDialogPreference
import com.vanced.manager.ui.preferences.CheckboxPreference
import com.vanced.manager.ui.preferences.ManagerPreference

data class CheckboxInstallationOption(
    @StringRes val title: Int,
    val preference: ManagerPreference<Set<String>>,
    val buttons: List<CheckboxPreference>
) : InstallationOption(
    item = {
        CheckboxDialogPreference(
            preferenceTitle = title,
            preference = preference,
            buttons = buttons
        )
    }
)