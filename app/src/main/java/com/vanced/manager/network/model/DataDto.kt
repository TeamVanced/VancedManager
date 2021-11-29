package com.vanced.manager.network.model

import com.google.gson.annotations.SerializedName

data class DataDto(
    @SerializedName("manager") val manager: AppDto,
    @SerializedName("apps") val apps: DataAppDto
)

data class DataAppDto(
    @SerializedName("nonroot") val nonroot: List<AppDto>,
    @SerializedName("root") val root: List<AppDto>,
)