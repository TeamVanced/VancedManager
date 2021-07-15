package com.vanced.manager.ui.widget.screens.home.installation

import androidx.annotation.StringRes
import com.vanced.manager.preferences.CheckboxPreference
import com.vanced.manager.preferences.ManagerPreference
import com.vanced.manager.ui.component.preference.CheckboxDialogPreference
import com.vanced.manager.ui.resources.managerString

data class CheckboxInstallationOption(
    @StringRes val titleId: Int,
    val preference: ManagerPreference<Set<String>>,
    val buttons: List<CheckboxPreference>
) : InstallationOption(
    item = {
        CheckboxDialogPreference(
            preferenceTitle = managerString(stringId = titleId),
            preference = preference,
            buttons = buttons
        )
    }
)