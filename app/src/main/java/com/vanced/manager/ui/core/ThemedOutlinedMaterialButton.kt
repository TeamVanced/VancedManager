package com.vanced.manager.ui.core

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.core.graphics.ColorUtils
import com.google.android.material.button.MaterialButton
import com.vanced.manager.utils.Extensions.getDefaultPrefs
import com.vanced.manager.utils.Extensions.lifecycleOwner
import com.vanced.manager.utils.ThemeHelper.accentColor
import com.vanced.manager.utils.ThemeHelper.defAccentColor


class ThemedOutlinedMaterialButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialButton(context, attributeSet, defStyleAttr) {
    init {
        applyAccent(context.getDefaultPrefs().getInt("manager_accent", defAccentColor))
        context.lifecycleOwner()?.let { owner ->
            accentColor.observe(owner) { color ->
                applyAccent(color.toInt())
            }
        }
    }

    private fun applyAccent(color: Int) {
        setTextColor(color)
        rippleColor = ColorStateList(arrayOf(intArrayOf()), intArrayOf(ColorUtils.setAlphaComponent(color, 50)))
    }
}