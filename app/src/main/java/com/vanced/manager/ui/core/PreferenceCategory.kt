package com.vanced.manager.ui.core

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.vanced.manager.R
import kotlinx.android.synthetic.main.view_preference_category.view.*

class PreferenceCategory @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
) : LinearLayout(context, attrs, defStyle) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_preference_category, this, true)
        initAttrs(context, attrs)
        setPadding(0, 4, 0, 0)
        orientation = VERTICAL
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        attrs.let { mAttrs ->
            val typedArray = context.obtainStyledAttributes(mAttrs, R.styleable.PreferenceCategory, 0, 0)
            val title = typedArray.getText(R.styleable.PreferenceCategory_category_title)

            category_title.text = title
            typedArray.recycle()
        }
    }

}