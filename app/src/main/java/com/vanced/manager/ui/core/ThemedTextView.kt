package com.vanced.manager.ui.core

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.vanced.manager.utils.Extensions.getDefaultPrefs
import com.vanced.manager.utils.Extensions.lifecycleOwner
import com.vanced.manager.utils.ThemeHelper.accentColor
import com.vanced.manager.utils.ThemeHelper.defAccentColor

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