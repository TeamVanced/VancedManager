package com.vanced.manager.ui.core

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.vanced.manager.R
import com.vanced.manager.databinding.ViewPreferenceBinding

class EmptyPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyle, defStyleRes) {

    private var _binding: ViewPreferenceBinding? = null

    val binding: ViewPreferenceBinding
        get() = requireNotNull(_binding)

    init {
        _binding = ViewPreferenceBinding.inflate(LayoutInflater.from(context), this, true)
        initAttrs(context, attrs)
    }

    fun setTitle(newTitle: String) {
        binding.preferenceTitle.text = newTitle
    }

    fun setSummary(newSummary: String) {
        with(binding) {
            preferenceSummary.text = newSummary
            preferenceSummary.isVisible = true
            preferenceTitle.setPadding(0, 0, 0, 0)
        }
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        attrs?.let { mAttrs ->
            val typedArray =
                context.obtainStyledAttributes(mAttrs, R.styleable.EmptyPreference, 0, 0)
            val title = typedArray.getText(R.styleable.EmptyPreference_preference_title)
            val summary = typedArray.getText(R.styleable.EmptyPreference_preference_summary)
            with(binding) {
                if (summary != null) {
                    preferenceSummary.text = summary
                } else {
                    preferenceSummary.isGone = true
                    preferenceTitle.setPadding(0, 12, 0, 12)
                }
                preferenceTitle.text = title
            }
            typedArray.recycle()
        }

    }

}