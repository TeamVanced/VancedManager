package com.vanced.manager.ui.core

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import com.vanced.manager.utils.accentColor

class ThemedImageButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageButton(context, attributeSet, defStyleAttr) {

    init {
        imageTintList = ColorStateList.valueOf(accentColor.value!!)
    }

}