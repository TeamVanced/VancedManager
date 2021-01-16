package com.vanced.manager.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.google.android.material.radiobutton.MaterialRadioButton
import com.topjohnwu.superuser.Shell
import com.vanced.manager.core.ui.base.BindingBottomSheetDialogFragment
import com.vanced.manager.databinding.DialogManagerVariantBinding
import com.vanced.manager.utils.getCheckedButtonTag

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
            val variant = prefs.getString("vanced_variant", "nonroot")
            root.findViewWithTag<MaterialRadioButton>(variant).isChecked = true
            variantSave.setOnClickListener {
                val newPref = variantRadiogroup.getCheckedButtonTag()
                if (variant != newPref) {
                    prefs.edit {
                        if (newPref == "root" && Shell.rootAccess()) {
                            putString("vanced_variant", "root")
                        } else {
                            putString("vanced_variant", "nonroot")
                        }
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