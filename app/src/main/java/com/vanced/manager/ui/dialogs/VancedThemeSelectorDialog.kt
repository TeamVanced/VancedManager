package com.vanced.manager.ui.dialogs

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.radiobutton.MaterialRadioButton
import com.vanced.manager.R
import com.vanced.manager.databinding.DialogBottomRadioButtonBinding
import com.vanced.manager.utils.Extensions.convertToAppTheme
import com.vanced.manager.utils.Extensions.getCheckedButtonTag
import com.vanced.manager.utils.Extensions.show
import com.vanced.manager.utils.InternetTools.vanced

class VancedThemeSelectorDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogBottomRadioButtonBinding
    private val prefs by lazy { requireActivity().getSharedPreferences("installPrefs", Context.MODE_PRIVATE) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_bottom_radio_button, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadButtons()
        binding.dialogTitle.text = requireActivity().getString(R.string.theme)
        view.findViewWithTag<MaterialRadioButton>(prefs.getString("theme", "dark")).isChecked = true
        binding.dialogSave.setOnClickListener {
            prefs.edit { putString("theme", binding.dialogRadiogroup.getCheckedButtonTag()) }
            dismiss()
            VancedPreferencesDialog().show(requireActivity())
        }
    }

    private fun loadButtons() {
        requireActivity().runOnUiThread {
            vanced.get()?.array<String>("themes")?.value?.forEach { theme ->
                val rb = MaterialRadioButton(requireActivity()).apply {
                    text = theme.convertToAppTheme(requireActivity())
                    tag = theme
                    textSize = 18f
                }
                binding.dialogRadiogroup.addView(rb, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        VancedPreferencesDialog().show(requireActivity())
    }

}