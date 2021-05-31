package com.vanced.manager.domain.model

import androidx.annotation.DrawableRes

data class Link(
    val title: String,
    val link: String,
    @DrawableRes val icon: Int
)
