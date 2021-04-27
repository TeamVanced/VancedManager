package com.vanced.manager.ui.core

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.google.android.material.card.MaterialCardView
import com.vanced.manager.utils.accentColor

class ThemedAppCard @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attributeSet, defStyleAttr) {

    init {
        setCardBackgroundColor(ColorStateList.valueOf(accentColor.value!!).withAlpha(35))
    }

}