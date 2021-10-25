package com.vanced.manager.model

import androidx.annotation.DrawableRes
import com.vanced.manager.R

enum class ButtonTag(@DrawableRes val image: Int) {
    INSTALL(R.drawable.ic_app_download),
    UPDATE(R.drawable.ic_app_update),
    REINSTALL(R.drawable.ic_app_reinstall)
}