package com.vanced.manager.ui.core

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CompoundButton
import android.widget.FrameLayout
import androidx.core.content.edit
import com.vanced.manager.R
import com.vanced.manager.databinding.ViewPreferenceSwitchBinding
import com.vanced.manager.utils.defPrefs

class PreferenceSwitch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyle, defStyleRes) {

    fun interface OnCheckedListener {
        fun onChecked(buttonView: CompoundButton, isChecked: Boolean)
    }

    private val prefs by lazy { context.defPrefs }

    var prefKey: String = ""
        private set

    var defValue: Boolean = false
        private set

    private var mListener: OnCheckedListener? = null

    private val prefListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == prefKey) {
                binding.preferenceSwitch.isChecked = sharedPreferences.getBoolean(key, defValue)
            }
        }

    private var _binding: ViewPreferenceSwitchBinding? = null

    val binding: ViewPreferenceSwitchBinding
        get() = requireNotNull(_binding)

    init {
        _binding = ViewPreferenceSwitchBinding.inflate(LayoutInflater.from(context), this, true)
        prefs.registerOnSharedPreferenceChangeListener(prefListener)
        attrs?.let { mAttrs ->
            with(context.obtainStyledAttributes(mAttrs, R.styleable.PreferenceSwitch, 0, 0)) {
                val title = getText(R.styleable.PreferenceSwitch_switch_title)
                val summary = getText(R.styleable.PreferenceSwitch_switch_summary)
                val key = getText(R.styleable.PreferenceSwitch_switch_key)
                val defValue = getBoolean(R.styleable.PreferenceSwitch_switch_def_value, false)
                setKey(key)
                setDefaultValue(defValue)
                setTitle(title)
                setSummary(summary)
                recycle()
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setOnClickListener {
            binding.preferenceSwitch.isChecked = !binding.preferenceSwitch.isChecked
        }
        binding.preferenceSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            prefs.edit { putBoolean(prefKey, isChecked) }
            mListener?.onChecked(buttonView, isChecked)
        }
    }

    fun setOnCheckedListener(listener: OnCheckedListener) {
        mListener = listener
    }

    fun setTitle(title: CharSequence?) {
        binding.preferenceSwitchTitle.text = title
    }

    fun setSummary(summary: CharSequence?) {
        binding.preferenceSwitchSummary.text = summary
    }

    fun setKey(key: CharSequence?) {
        prefKey = key.toString()
        binding.preferenceSwitch.isChecked = prefs.getBoolean(key.toString(), defValue)
    }

    fun setDefaultValue(newVal: Boolean) {
        defValue = newVal
        binding.preferenceSwitch.isChecked = prefs.getBoolean(prefKey, newVal)
    }

    fun setChecked(checked: Boolean) {
        binding.preferenceSwitch.isChecked = checked
    }
}