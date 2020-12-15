package com.vanced.manager.ui.dialogs

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.edit
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.madrapps.pikolo.listeners.OnColorSelectionListener
import com.vanced.manager.R
import com.vanced.manager.core.ui.base.BindingDialogFragment
import com.vanced.manager.databinding.DialogManagerAccentColorBinding
import com.vanced.manager.utils.Extensions.toHex
import com.vanced.manager.utils.ThemeHelper.accentColor
import com.vanced.manager.utils.ThemeHelper.defAccentColor
import com.vanced.manager.utils.ThemeHelper.mutableAccentColor

class ManagerAccentColorDialog : BindingDialogFragment<DialogManagerAccentColorBinding>() {

    companion object {
        fun newInstance(): ManagerAccentColorDialog = ManagerAccentColorDialog().apply {
            arguments = Bundle()
        }
    }

    private val prefs by lazy { getDefaultSharedPreferences(requireActivity()) }

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DialogManagerAccentColorBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        bindData()
        isCancelable = false
    }

    private fun bindData() {
        with(binding) {
            val accent = prefs.getInt("manager_accent", defAccentColor)
            hexEdittext.apply {
                setText(accent.toHex(), TextView.BufferType.EDITABLE)
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        if (length() == 0) {
                            setText("#")
                            setSelection(1)

                        }

                        if (accentColor.value?.toHex() != text.toString() && length() == 7) {
                            try {
                                val colorFromEditText = Color.parseColor(text.toString())
                                accentPicker.setColor(colorFromEditText)
                                mutableAccentColor.value = colorFromEditText
                            } catch (e: java.lang.IllegalArgumentException) { }
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {}

                })
            }
            accentPicker.apply {
                setColor(accent)
                setColorSelectionListener(object : OnColorSelectionListener {
                    override fun onColorSelected(color: Int) {
                        mutableAccentColor.value = color
                        hexEdittext.setText(color.toHex(), TextView.BufferType.EDITABLE)
                    }

                    override fun onColorSelectionEnd(color: Int) {}

                    override fun onColorSelectionStart(color: Int) {}

                })
            }
            accentCancel.setOnClickListener {
                mutableAccentColor.value = accent
                dismiss()
            }
            accentSave.setOnClickListener {
                try {
                    val colorFromEditText = Color.parseColor(hexEdittext.text.toString())
                    mutableAccentColor.value = colorFromEditText
                    prefs.edit { putInt("manager_accent", colorFromEditText) }
                } catch (e: IllegalArgumentException) {
                    Log.d("VMTheme", getString(R.string.failed_accent))
                    Toast.makeText(requireActivity(), getString(R.string.failed_accent), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                dismiss()
            }
            accentReset.setOnClickListener {
                prefs.edit { putInt("manager_accent", defAccentColor) }
                mutableAccentColor.value = defAccentColor
                dismiss()
            }
        }
    }
}