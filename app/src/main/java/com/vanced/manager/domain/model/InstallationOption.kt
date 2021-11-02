package com.vanced.manager.domain.model

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

sealed class InstallationOption(
    @StringRes val itemTitleId: Int,
) : Parcelable {

    @Parcelize
    data class MultiSelect(
        @StringRes val titleId: Int,
        val items: List<InstallationOptionItem>,
        val getOption: () -> Set<String>,
        val addOption: (String) -> Unit,
        val removeOption: (String) -> Unit
    ) : InstallationOption(titleId)

    @Parcelize
    data class SingleSelect(
        @StringRes val titleId: Int,
        val items: List<InstallationOptionItem>,
        val getOption: () -> String,
        val setOption: (String) -> Unit,
    ) : InstallationOption(titleId)

}

@Parcelize
data class InstallationOptionItem(
    val key: String,
    val displayText: (key: String) -> String,
) : Parcelable