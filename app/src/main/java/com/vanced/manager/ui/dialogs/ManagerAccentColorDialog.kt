package com.vanced.manager.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.google.android.material.radiobutton.MaterialRadioButton
import com.vanced.manager.core.ui.base.BindingBottomSheetDialogFragment
import com.vanced.manager.databinding.DialogManagerAccentColorBinding
import com.vanced.manager.utils.Extensions.getCheckedButtonTag

class ManagerAccentColorDialog : BindingBottomSheetDialogFragment<DialogManagerAccentColorBinding>() {

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
    }

    private fun bindData() {
        with(binding) {
            val accent = prefs.getString("manager_accent", "Blue")
            root.findViewWithTag<MaterialRadioButton>(accent).isChecked = true
            accentSave.setOnClickListener {
                val newPref = binding.accentRadiogroup.getCheckedButtonTag()
                if (accent != newPref) {
                    prefs.edit { putString("manager_accent", newPref) }
                    dismiss()
                    requireActivity().recreate()
                } else {
                    dismiss()
                }
            }
        }
    }
}