package com.vanced.manager.ui.widget.screens.home.installation

import androidx.annotation.StringRes
import com.vanced.manager.preferences.ManagerPreference
import com.vanced.manager.preferences.RadioButtonPreference
import com.vanced.manager.ui.component.preference.RadiobuttonDialogPreference
import com.vanced.manager.ui.resources.managerString

data class RadiobuttonInstallationOption(
    @StringRes val titleId: Int,
    val preference: ManagerPreference<String>,
    val buttons: List<RadioButtonPreference>
) : InstallationOption(
    item = {
        RadiobuttonDialogPreference(
            preferenceTitle = managerString(stringId = titleId),
            preference = preference,
            buttons = buttons
        )
    }
)