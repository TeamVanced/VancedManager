package com.vanced.manager.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface InstallationOption : Parcelable {

    val titleId: Int
    val items: List<InstallationOptionItem>

    @Parcelize
    data class MultiSelect(
        override val titleId: Int,
        override val items: List<InstallationOptionItem>,
        val getOption: () -> Set<String>,
        val addOption: (String) -> Unit,
        val removeOption: (String) -> Unit
    ) : InstallationOption

    @Parcelize
    data class SingleSelect(
        override val titleId: Int,
        override val items: List<InstallationOptionItem>,
        val getOption: () -> String,
        val setOption: (String) -> Unit,
    ) : InstallationOption

}

@Parcelize
data class InstallationOptionItem(
    val key: String,
    val displayText: (key: String) -> String,
) : Parcelable