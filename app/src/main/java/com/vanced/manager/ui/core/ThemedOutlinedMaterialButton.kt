package com.vanced.manager.ui.core

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.core.graphics.ColorUtils
import com.google.android.material.button.MaterialButton
import com.vanced.manager.utils.accentColor
import com.vanced.manager.utils.lifecycleOwner

class ThemedOutlinedMaterialButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialButton(context, attributeSet, defStyleAttr) {
    init {
        context.lifecycleOwner?.let { owner ->
            accentColor.observe(owner) { color ->
                applyAccent(color.toInt())
            }
        }
    }

    private fun applyAccent(color: Int) {
        setTextColor(color)
        rippleColor = ColorStateList(
            arrayOf(intArrayOf()),
            intArrayOf(ColorUtils.setAlphaComponent(color, 50))
        )
    }
}