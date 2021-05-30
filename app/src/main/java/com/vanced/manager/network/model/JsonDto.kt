package com.vanced.manager.network.model

import com.google.gson.annotations.SerializedName

data class JsonDto(
    @SerializedName("is_microg_broken") var isMicrogBroken: Boolean,
    @SerializedName("manager") var manager: AppDto,
    @SerializedName("vanced") var vanced: AppDto,
    @SerializedName("music") var music: AppDto,
    @SerializedName("microg") var microg: AppDto
)