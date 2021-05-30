package com.vanced.manager.network.model

import com.google.gson.annotations.SerializedName

data class AppDto(
    @SerializedName("name") val name: String? = null,
    @SerializedName("version") val version: String? = null,
    @SerializedName("versionCode") val versionCode: Int? = null,
    @SerializedName("changelog") val changelog: String? = null,
    @SerializedName("package_name") val packageName: String? = null,
    @SerializedName("package_name_root") val packageNameRoot: String? = null,
    @SerializedName("icon_url") val iconUrl: String? = null,
    @SerializedName("url") val url: String? = null,
    @SerializedName("themes") val themes: List<String>? = null,
    @SerializedName("languages") val languages: List<String>? = null
)
