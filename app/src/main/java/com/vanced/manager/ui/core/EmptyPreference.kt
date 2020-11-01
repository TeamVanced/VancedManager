package com.vanced.manager.ui.core

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.vanced.manager.R
import kotlinx.android.synthetic.main.view_preference.view.*

class EmptyPreference @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
        defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyle, defStyleRes) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_preference, this, true)
        initAttrs(context, attrs)
    }

    fun setTitle(newTitle: String) {
        preference_title.text = newTitle
    }

    fun setSummary(newSummary: String) {
        preference_summary.text = newSummary
        preference_summary.visibility = View.VISIBLE
        preference_title.setPadding(0, 0, 0, 0)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        attrs?.let { mAttrs ->
            val typedArray = context.obtainStyledAttributes(mAttrs, R.styleable.EmptyPreference, 0, 0)
            val title = typedArray.getText(R.styleable.EmptyPreference_preference_title)
            val summary = typedArray.getText(R.styleable.EmptyPreference_preference_summary)
            if (summary != null) {
                preference_summary.text = summary
            } else {
                preference_summary.visibility = View.GONE
                preference_title.setPadding(0, 12, 0, 12)
            }
            preference_title.text = title
            typedArray.recycle()
        }

    }

}