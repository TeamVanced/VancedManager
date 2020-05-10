package com.vanced.manager.ui.core

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

open class SlidingLinearLayout: LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    )

    var yFraction: Float
        get() {
            val height = height
            return if (height != 0)
                y / height
            else
                y
        }
        set(yFraction) {
            val height = height
            val newHeight =
                if (height > 0)
                    yFraction * height
                else
                    (-9999).toFloat()
            y = newHeight
        }

}