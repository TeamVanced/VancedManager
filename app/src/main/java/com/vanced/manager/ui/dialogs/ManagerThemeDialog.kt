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
import com.vanced.manager.databinding.DialogManagerThemeBinding
import com.vanced.manager.utils.Extensions.getCheckedButtonTag

class ManagerThemeDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogManagerThemeBinding
    private val prefs by lazy { getDefaultSharedPreferences(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_manager_theme, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val theme = prefs.getString("manager_theme", "System Default")
        view.findViewWithTag<MaterialRadioButton>(theme).isChecked = true
        binding.themeSave.setOnClickListener {
            val newPref = binding.themeRadiogroup.getCheckedButtonTag()
            if (theme != newPref) {
                prefs.edit { putString("manager_theme", newPref) }
                dismiss()
                requireActivity().recreate()
            } else {
                dismiss()
            }
        }
    }
}