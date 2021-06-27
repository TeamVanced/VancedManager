package com.vanced.manager.ui.components.text

import androidx.annotation.StringRes
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.vanced.manager.R
import com.vanced.manager.ui.components.lifecycle.managerString

@Composable
fun AppVersionText(
    @StringRes stringId: Int,
    version: String?
) {
    ManagerText(
        version ?: managerString(stringId = R.string.app_content_unavailable),
        stringId = stringId,
        textStyle = MaterialTheme.typography.body2,
    )
}