package com.vanced.manager.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.google.android.material.radiobutton.MaterialRadioButton
import com.topjohnwu.superuser.Shell
import com.vanced.manager.core.ui.base.BindingBottomSheetDialogFragment
import com.vanced.manager.databinding.DialogManagerVariantBinding
import com.vanced.manager.utils.checkedButtonTag
import com.vanced.manager.utils.managerVariant

class ManagerVariantDialog : BindingBottomSheetDialogFragment<DialogManagerVariantBinding>() {

    companion object {

        fun newInstance(): ManagerVariantDialog = ManagerVariantDialog().apply {
            arguments = Bundle()
        }
    }

    private val prefs by lazy { getDefaultSharedPreferences(requireActivity()) }

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DialogManagerVariantBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        bindData()
    }

    private fun bindData() {
        with(binding) {
            val variant = prefs.managerVariant
            root.findViewWithTag<MaterialRadioButton>(variant).isChecked = true
            variantSave.setOnClickListener {
                val newPref = variantRadiogroup.checkedButtonTag
                if (variant != newPref) {
                    prefs.managerVariant =
                        if (newPref == "root" && Shell.rootAccess()) {
                            "root"
                        } else {
                            "nonroot"
                        }
                    dismiss()
                    requireActivity().recreate()
                } else {
                    dismiss()
                }
            }
        }
    }
}