package com.vanced.manager.ui.core

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import com.google.android.material.button.MaterialButton
import com.vanced.manager.R
import com.vanced.manager.utils.accentColor
import com.vanced.manager.utils.lifecycleOwner

class ThemedMaterialButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialButton(context, attributeSet, defStyleAttr) {

    init {
        context.lifecycleOwner?.let { owner ->
            accentColor.observe(owner) { color ->
                setBgColor(color.toInt())
            }
        }
    }

    private fun setBgColor(color: Int) {
        setBackgroundColor(color)
        if (ColorUtils.calculateLuminance(color) < 0.7) {
            setTextColor(ResourcesCompat.getColor(resources, R.color.White, null))
        } else {
            setTextColor(ResourcesCompat.getColor(resources, R.color.Black, null))
        }
    }
}