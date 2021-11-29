package com.vanced.manager.network.model

import com.google.gson.annotations.SerializedName

data class AppDto(
    @SerializedName("name") val name: String,
    @SerializedName("version") val version: String,
    @SerializedName("version_code") val versionCode: Int,
    @SerializedName("changelog") val changelog: String,
    @SerializedName("icon_url") val iconUrl: String,
    @SerializedName("package_name") val packageName: String,
    @SerializedName("url") val url: String? = null,
    @SerializedName("versions") val versions: List<String>? = null,
    @SerializedName("themes") val themes: List<String>? = null,
    @SerializedName("langs") val languages: List<String>? = null,
)
