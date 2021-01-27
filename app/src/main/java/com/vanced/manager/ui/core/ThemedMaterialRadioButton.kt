package com.vanced.manager.ui.core

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.google.android.material.radiobutton.MaterialRadioButton
import com.vanced.manager.R
import com.vanced.manager.utils.defPrefs
import com.vanced.manager.utils.managerAccent

class ThemedMaterialRadioButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
) : MaterialRadioButton(context, attributeSet, R.attr.radioButtonStyle) {
    init {
        buttonTintList = ColorStateList.valueOf(context.defPrefs.managerAccent)
    }
}