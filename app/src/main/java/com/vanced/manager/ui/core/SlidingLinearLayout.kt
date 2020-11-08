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
                    (1).toFloat()
            y = newHeight
        }

    var xFraction: Float
        get() {
            val width = width
            return if (width != 0)
                x / getWidth()
            else
                x
        }
        set(xFraction) {
            val width = width
            val newWidth =
                    if (width > 0)
                        xFraction * width
                    else
                        (1).toFloat()
            x = newWidth
        }

}