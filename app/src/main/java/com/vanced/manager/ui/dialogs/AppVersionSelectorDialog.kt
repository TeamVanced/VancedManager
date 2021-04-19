package com.vanced.manager.ui.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.edit
import com.google.android.material.radiobutton.MaterialRadioButton
import com.vanced.manager.R
import com.vanced.manager.core.ui.base.BindingBottomSheetDialogFragment
import com.vanced.manager.core.ui.ext.showDialog
import com.vanced.manager.databinding.DialogBottomRadioButtonBinding
import com.vanced.manager.ui.core.ThemedMaterialRadioButton
import com.vanced.manager.utils.checkedButtonTag
import com.vanced.manager.utils.defPrefs
import com.vanced.manager.utils.formatVersion

class AppVersionSelectorDialog :
    BindingBottomSheetDialogFragment<DialogBottomRadioButtonBinding>() {

    private val prefs by lazy { requireActivity().defPrefs }

    companion object {

        private const val TAG_VERSIONS = "TAG_VERSIONS"
        private const val TAG_APP = "TAG_APP"

        fun newInstance(
            versions: List<String>?,
            app: String
        ): AppVersionSelectorDialog = AppVersionSelectorDialog().apply {
            arguments = Bundle().apply {
                val arrayList = arrayListOf<String>()
                versions?.let { arrayList.addAll(it) }
                putStringArrayList(TAG_VERSIONS, arrayList)
                putString(TAG_APP, app)
            }
        }
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
            loadBoxes()?.forEach { mrb ->
                dialogRadiogroup.addView(
                    mrb,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            val tag = root.findViewWithTag<MaterialRadioButton>(
                prefs.getString("${arguments?.getString(TAG_APP)}_version", "latest")
            )
            if (tag != null) {
                tag.isChecked = true
            }
            dialogTitle.text = getString(R.string.version)
            dialogSave.setOnClickListener {
                val checkedTag = dialogRadiogroup.checkedButtonTag
                if (checkedTag != null) {
                    prefs.edit { putString("${arguments?.getString(TAG_APP)}_version", checkedTag) }
                }
                dismiss()
            }
        }
    }

    private fun loadBoxes() =
        arguments?.getStringArrayList(TAG_VERSIONS)?.map { version ->
            ThemedMaterialRadioButton(requireActivity()).apply {
                text = version.formatVersion(requireActivity())
                tag = version
                textSize = 18f
            }
        }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (arguments?.getString(TAG_APP) == "vanced") {
            showDialog(VancedPreferencesDialog())
        } else {
            showDialog(MusicPreferencesDialog())
        }
    }
}