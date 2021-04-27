package com.vanced.manager.ui.core

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.SwitchCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.DrawableCompat
import com.vanced.manager.R
import com.vanced.manager.utils.accentColor
import com.vanced.manager.utils.lifecycleOwner

class ThemedSwitchCompat @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
) : SwitchCompat(context, attributeSet, R.attr.switchStyle) {

    private val states =
        arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked))

    init {
        context.lifecycleOwner?.let { owner ->
            accentColor.observe(owner) { color ->
                setSwitchColors(color.toInt())
            }
        }
    }

    private fun setSwitchColors(color: Int) {
        val thumbColors = intArrayOf(Color.LTGRAY, color)
        val trackColors = intArrayOf(Color.GRAY, ColorUtils.setAlphaComponent(color, 70))
        DrawableCompat.setTintList(
            DrawableCompat.wrap(thumbDrawable),
            ColorStateList(states, thumbColors)
        )
        DrawableCompat.setTintList(
            DrawableCompat.wrap(trackDrawable),
            ColorStateList(states, trackColors)
        )
    }
}