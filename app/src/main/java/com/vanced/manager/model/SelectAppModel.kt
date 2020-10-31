package com.vanced.manager.model

data class SelectAppModel(
    val appName: String,
    val appDescription: String,
    val tag: String,
    var isChecked: Boolean
)