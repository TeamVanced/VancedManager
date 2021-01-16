package com.vanced.manager.ui.core

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.vanced.manager.utils.accentColor
import com.vanced.manager.utils.defAccentColor
import com.vanced.manager.utils.getDefaultPrefs
import com.vanced.manager.utils.lifecycleOwner

class ThemedTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attributeSet, defStyleAttr) {
    init {
        setTextColor(context.getDefaultPrefs().getInt("manager_accent_color", defAccentColor))
        context.lifecycleOwner()?.let { owner ->
            accentColor.observe(owner) { color ->
                setTextColor(color.toInt())
            }
        }
    }
}