package com.vanced.manager.ui.core

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.vanced.manager.utils.accentColor

class ThemedIconButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialButton(context, attributeSet, defStyleAttr) {

    init {
        iconTint = ColorStateList.valueOf(accentColor.value!!)
        setOnLongClickListener {
            Toast.makeText(context, contentDescription, Toast.LENGTH_SHORT).show()
            true
        }
    }

}