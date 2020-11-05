package com.vanced.manager.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.radiobutton.MaterialRadioButton
import com.vanced.manager.R
import com.vanced.manager.databinding.DialogManagerAccentColorBinding
import com.vanced.manager.utils.Extensions.getCheckedButtonTag

class ManagerAccentColorDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogManagerAccentColorBinding
    private val prefs by lazy { getDefaultSharedPreferences(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_manager_accent_color, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val accent = prefs.getString("manager_accent", "Blue")
        view.findViewWithTag<MaterialRadioButton>(accent).isChecked = true
        binding.accentSave.setOnClickListener {
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