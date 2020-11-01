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
import com.topjohnwu.superuser.Shell
import com.vanced.manager.R
import com.vanced.manager.databinding.DialogManagerVariantBinding
import com.vanced.manager.utils.Extensions.getCheckedButtonTag

class ManagerVariantDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogManagerVariantBinding
    private val prefs by lazy { getDefaultSharedPreferences(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_manager_variant, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val variant = prefs.getString("vanced_variant", "nonroot")
        view.findViewWithTag<MaterialRadioButton>(variant).isChecked = true
        binding.variantSave.setOnClickListener {
            val newPref = binding.variantRadiogroup.getCheckedButtonTag()
            if (variant != newPref) {
                if (newPref == "root" && Shell.rootAccess())
                    prefs.edit { putString("vanced_variant", "root") }
                else
                    prefs.edit { putString("vanced_variant", "nonroot") }

                dismiss()
                requireActivity().recreate()
            } else {
                dismiss()
            }
        }
    }
}