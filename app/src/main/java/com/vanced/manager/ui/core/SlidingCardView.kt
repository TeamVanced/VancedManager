package com.vanced.manager.ui.core

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.card.MaterialCardView

open class SlidingCardView: MaterialCardView {

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