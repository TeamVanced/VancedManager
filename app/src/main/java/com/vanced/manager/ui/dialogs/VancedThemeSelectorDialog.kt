package com.vanced.manager.ui.dialogs

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.vanced.manager.R
import com.vanced.manager.core.ui.base.BindingBottomSheetDialogFragment
import com.vanced.manager.core.ui.ext.showDialog
import com.vanced.manager.databinding.DialogBottomRadioButtonBinding
import com.vanced.manager.ui.core.ThemedMaterialRadioButton
import com.vanced.manager.utils.checkedButtonTag
import com.vanced.manager.utils.convertToAppTheme
import com.vanced.manager.utils.theme
import com.vanced.manager.utils.vanced

class VancedThemeSelectorDialog :
    BindingBottomSheetDialogFragment<DialogBottomRadioButtonBinding>() {

    companion object {

        fun newInstance(): VancedThemeSelectorDialog = VancedThemeSelectorDialog().apply {
            arguments = Bundle()
        }
    }

    private val prefs by lazy {
        requireActivity().getSharedPreferences(
            "installPrefs",
            Context.MODE_PRIVATE
        )
    }

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DialogBottomRadioButtonBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        bindData()
    }

    private fun bindData() {
        with(binding) {
            loadButtons()?.forEach { mrb ->
                dialogRadiogroup.addView(
                    mrb,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            dialogTitle.text = requireActivity().getString(R.string.theme)
            val tag = root.findViewWithTag<ThemedMaterialRadioButton>(prefs.theme)
            if (tag != null) {
                tag.isChecked = true
            }
            dialogSave.setOnClickListener {
                val checkedTag = binding.dialogRadiogroup.checkedButtonTag
                if (checkedTag != null) {
                    prefs.theme = checkedTag
                }
                dismiss()
            }
        }
    }

    private fun loadButtons() = vanced.value?.array<String>("themes")?.value?.map { theme ->
        ThemedMaterialRadioButton(requireActivity()).apply {
            text = theme.convertToAppTheme(requireActivity())
            tag = theme
            textSize = 18f
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        showDialog(VancedPreferencesDialog())
    }
}