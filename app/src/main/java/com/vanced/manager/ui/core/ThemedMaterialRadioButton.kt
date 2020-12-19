package com.vanced.manager.ui.core

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.google.android.material.radiobutton.MaterialRadioButton
import com.vanced.manager.R
import com.vanced.manager.utils.Extensions.getDefaultPrefs
import com.vanced.manager.utils.Extensions.lifecycleOwner
import com.vanced.manager.utils.ThemeHelper.accentColor
import com.vanced.manager.utils.ThemeHelper.defAccentColor

class ThemedMaterialRadioButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
) : MaterialRadioButton(context, attributeSet, R.attr.radioButtonStyle) {
    init {
        buttonTintList = ColorStateList.valueOf(context.getDefaultPrefs().getInt("manager_accent", defAccentColor))
        context.lifecycleOwner()?.let { owner ->
            accentColor.observe(owner) { color ->
                buttonTintList = ColorStateList.valueOf(color.toInt())
            }
        }
    }
}