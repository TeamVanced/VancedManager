package com.vanced.manager.ui.core

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.vanced.manager.R
import com.vanced.manager.databinding.ViewPreferenceCategoryBinding

class PreferenceCategory @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private var _binding: ViewPreferenceCategoryBinding? = null

    val binding: ViewPreferenceCategoryBinding
        get() = requireNotNull(_binding)

    init {
        _binding = ViewPreferenceCategoryBinding.inflate(LayoutInflater.from(context), this, true)
        initAttrs(context, attrs)
        setPadding(0, 4, 0, 0)
        orientation = VERTICAL
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        attrs.let { mAttrs ->
            val typedArray =
                context.obtainStyledAttributes(mAttrs, R.styleable.PreferenceCategory, 0, 0)
            val title = typedArray.getText(R.styleable.PreferenceCategory_category_title)

            binding.categoryTitle.text = title
            typedArray.recycle()
        }
    }

}