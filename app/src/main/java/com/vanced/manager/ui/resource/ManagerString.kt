package com.vanced.manager.ui.resource

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.vanced.manager.R

@Composable
fun managerString(
    @StringRes stringId: Int?
) = stringResource(id = stringId ?: R.string.dummy_placeholder_text)

@Composable
fun managerString(
    @StringRes stringId: Int?,
    vararg formatArgs: Any
) = stringResource(id = stringId ?: R.string.dummy_placeholder_text, *formatArgs)