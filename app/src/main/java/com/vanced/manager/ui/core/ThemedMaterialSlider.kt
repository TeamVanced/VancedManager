package com.vanced.manager.ui.core

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.google.android.material.slider.Slider
import com.vanced.manager.utils.defPrefs
import com.vanced.manager.utils.managerAccent

class ThemedMaterialSlider @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : Slider(context, attributeSet, defStyleAttr) {

    init {
        val accentValue = ColorStateList.valueOf(context.defPrefs.managerAccent)
        thumbTintList = accentValue
        trackActiveTintList = accentValue
        trackInactiveTintList = accentValue.withAlpha(70)
        haloTintList = accentValue.withAlpha(60)
    }

}