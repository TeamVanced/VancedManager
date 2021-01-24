package com.vanced.manager.ui.core

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.vanced.manager.utils.accentColor
import com.vanced.manager.utils.defPrefs
import com.vanced.manager.utils.lifecycleOwner
import com.vanced.manager.utils.managerAccent

class ThemedTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attributeSet, defStyleAttr) {
    init {
        setTextColor(context.defPrefs.managerAccent)
        context.lifecycleOwner()?.let { owner ->
            accentColor.observe(owner) { color ->
                setTextColor(color.toInt())
            }
        }
    }
}