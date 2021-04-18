package com.vanced.manager.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.google.android.material.radiobutton.MaterialRadioButton
import com.vanced.manager.core.ui.base.BindingBottomSheetDialogFragment
import com.vanced.manager.databinding.DialogManagerStorageBinding
import com.vanced.manager.utils.*

class ManagerStorageDialog : BindingBottomSheetDialogFragment<DialogManagerStorageBinding>() {

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
        if (results.all { it.value == true }) {
            prefs.managerStorage = externalPath
        } else {
            prefs.managerStorage = internalPath
        }
        dismiss()
    }

    companion object {

        fun newInstance(): ManagerStorageDialog = ManagerStorageDialog().apply {
            arguments = Bundle()
        }
    }

    private val prefs by lazy { getDefaultSharedPreferences(requireActivity()) }

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DialogManagerStorageBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        bindData()
    }

    private fun bindData() {
        with(binding) {
            val storage = prefs.managerStorage
            root.findViewWithTag<MaterialRadioButton>(storage).isChecked = true
            storageSave.setOnClickListener {
                val newPref = storageRadiogroup.checkedButtonTag
                if (storage != newPref) {
                    if (newPref == externalPath) {
                        if (!canAccessStorage(requireActivity())) {
                            return@setOnClickListener requestStoragePerms(requireActivity(), permissionLauncher)
                        }

                        prefs.managerStorage = externalPath
                    } else {
                        prefs.managerStorage = internalPath
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