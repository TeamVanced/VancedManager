package com.vanced.manager.ui.core

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.google.android.material.slider.Slider
import com.vanced.manager.utils.Extensions.getDefaultPrefs
import com.vanced.manager.utils.ThemeHelper

class ThemedMaterialSlider@JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : Slider(context, attributeSet, defStyleAttr) {

    init {
        thumbStrokeColor = ColorStateList.valueOf(context.getDefaultPrefs().getInt("manager_accent_color", ThemeHelper.defAccentColor))
    }

}