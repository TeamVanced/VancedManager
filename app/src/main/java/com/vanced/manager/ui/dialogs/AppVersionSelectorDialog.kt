package com.vanced.manager.ui.dialogs

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
import com.vanced.manager.utils.Extensions.getCheckedButtonTag
import com.vanced.manager.utils.Extensions.getDefaultPrefs
import com.vanced.manager.utils.Extensions.show

class AppVersionSelectorDialog(
    private val versions: List<String>,
    private val app: String
) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogBottomRadioButtonBinding
    private val prefs by lazy { requireActivity().getDefaultPrefs() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_bottom_radio_button, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadBoxes()
        val tag = view.findViewWithTag<MaterialRadioButton>(prefs.getString("${app}_version", "latest"))
        if (tag != null) {
            tag.isChecked = true
        }
        binding.dialogTitle.text = requireActivity().getString(R.string.version)
        binding.dialogSave.setOnClickListener {
            prefs.edit {
                putString("${app}_version", binding.dialogRadiogroup.getCheckedButtonTag())
            }
            dismiss()
        }
    }

    private fun loadBoxes() {
        requireActivity().runOnUiThread {
            for (i in versions.indices) {
                val rb = MaterialRadioButton(requireActivity()).apply {
                    text = versions[i]
                    tag = versions[i]
                    textSize = 18f
                }
                binding.dialogRadiogroup.addView(rb, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (app == "vanced")
            VancedPreferencesDialog().show(requireActivity())
        else
            MusicPreferencesDialog().show(requireActivity())
    }

}