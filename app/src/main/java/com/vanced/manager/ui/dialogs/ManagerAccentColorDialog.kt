package com.vanced.manager.ui.dialogs

import android.graphics.Color
import android.os.Bundle
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
            hexEdittext.setText(accent.toHex(), TextView.BufferType.EDITABLE)
            accentPicker.apply {
                setColor(accent)
                setColorSelectionListener(object : OnColorSelectionListener {
                    override fun onColorSelected(color: Int) {
                        mutableAccentColor.value = color
                        hexEdittext.setText(color.toHex(), TextView.BufferType.EDITABLE)
                    }

                    override fun onColorSelectionEnd(color: Int) {
                        return
                    }

                    override fun onColorSelectionStart(color: Int) {
                        return
                    }

                })
            }
            accentSave.setOnClickListener {
                try {
                    val colorFromEdittext = Color.parseColor(hexEdittext.text.toString())
                    mutableAccentColor.value = colorFromEdittext
                    prefs.edit { putInt("manager_accent", colorFromEdittext) }
                } catch (e: IllegalArgumentException) {
                    Log.d("VMTheme", getString(R.string.failed_accent))
                    Toast.makeText(requireActivity(), getString(R.string.failed_accent), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                dismiss()
            }
            accentSave.setOnClickListener {
                mutableAccentColor.value = accent
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