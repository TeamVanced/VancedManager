package com.vanced.manager.ui.core

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.CompoundButton
import android.widget.FrameLayout
import androidx.core.content.edit
import androidx.databinding.BindingAdapter
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.vanced.manager.R
import kotlinx.android.synthetic.main.view_preference_switch.view.*

class PreferenceSwitch @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
        defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyle, defStyleRes) {

    private val prefs by lazy { getDefaultSharedPreferences(context) }
    var prefKey: String = ""
    var defValue: Boolean = false
    private var mListener: OnCheckedListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_preference_switch, this, true)
        initAttrs(context, attrs)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        preference_switch.isChecked = prefs.getBoolean(prefKey, defValue)
        setOnClickListener {
            preference_switch.isChecked = !preference_switch.isChecked
            Log.d("clickTest", "clicked")
        }
        preference_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            prefs.edit { putBoolean(prefKey, isChecked) }
            mListener?.onChecked(buttonView, isChecked)
        }
    }

    fun setOnCheckedListener(method: (buttonView: CompoundButton, isChecked: Boolean) -> Unit) {
        mListener = object : OnCheckedListener{
            override fun onChecked(buttonView: CompoundButton, isChecked: Boolean) {
                method(buttonView, isChecked)
            }
        }
    }

    fun setOnCheckedListener(listener: OnCheckedListener?) {
        mListener = listener
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        attrs?.let { mAttrs ->
            val typedArray = context.obtainStyledAttributes(mAttrs, R.styleable.PreferenceSwitch, 0, 0)
            val title = typedArray.getText(R.styleable.PreferenceSwitch_switch_title)
            val summary = typedArray.getText(R.styleable.PreferenceSwitch_switch_summary)
            val key = typedArray.getText(R.styleable.PreferenceSwitch_switch_key)
            val value = typedArray.getBoolean(R.styleable.PreferenceSwitch_switch_def_value, false)

            if (key != null)
                prefKey = key.toString()

            defValue = value
            preference_switch_title.text = title

            if (summary != null) {
                preference_switch_summary.text = summary
            }

            typedArray.recycle()
        }
    }

    interface OnCheckedListener {
        fun onChecked(buttonView: CompoundButton, isChecked: Boolean)
    }

    companion object {

        @JvmStatic
        @BindingAdapter("app:switch_title")
        fun setTitle(view: PreferenceSwitch, newTitle: String) {
            view.preference_switch_title.text = newTitle
        }

        @JvmStatic
        @BindingAdapter("app:switch_summary")
        fun setSummary(view: PreferenceSwitch, newSummary: String) {
            view.preference_switch_summary.text = newSummary
        }

        @JvmStatic
        @BindingAdapter("app:switch_key")
        fun setKey(view: PreferenceSwitch, newKey: String) {
            view.prefKey = newKey
            view.preference_switch.isChecked = view.prefs.getBoolean(view.prefKey, view.defValue)
        }

        @JvmStatic
        @BindingAdapter("app:switch_def_value")
        fun setDefaultValue(view: PreferenceSwitch, newVal: Boolean) {
            view.defValue = newVal
            view.preference_switch.isChecked = view.prefs.getBoolean(view.prefKey, view.defValue)
        }

    }

}