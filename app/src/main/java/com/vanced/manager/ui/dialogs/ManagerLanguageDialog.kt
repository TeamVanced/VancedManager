package com.vanced.manager.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.content.edit
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.vanced.manager.BuildConfig.MANAGER_LANGUAGES
import com.vanced.manager.core.ui.base.BindingBottomSheetDialogFragment
import com.vanced.manager.databinding.DialogManagerLanguageBinding
import com.vanced.manager.ui.core.ThemedMaterialRadioButton
import com.vanced.manager.utils.getCheckedButtonTag
import com.vanced.manager.utils.getLanguageFormat

class ManagerLanguageDialog : BindingBottomSheetDialogFragment<DialogManagerLanguageBinding>() {

    companion object {

        fun newInstance(): ManagerLanguageDialog = ManagerLanguageDialog().apply {
            arguments = Bundle()
        }
    }

    private val prefs by lazy { getDefaultSharedPreferences(requireActivity()) }

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DialogManagerLanguageBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        bindData()
    }

    private fun bindData() {
        with(binding) {
            addRadioButtons().forEach { mrb ->
                languageRadiogroup.addView(mrb, MATCH_PARENT, WRAP_CONTENT)
            }
            val language = prefs.getString("manager_lang", "System Default")
            root.findViewWithTag<ThemedMaterialRadioButton>(language)?.isChecked = true
            languageSave.setOnClickListener {
                val newPref = binding.languageRadiogroup.getCheckedButtonTag()
                if (language != newPref) {
                    prefs.edit { putString("manager_lang", newPref) }
                    dismiss()
                    requireActivity().recreate()
                } else {
                    dismiss()
                }
            }
        }
    }

    private fun addRadioButtons() =
        (arrayOf("System Default") + MANAGER_LANGUAGES).map { lang ->
            ThemedMaterialRadioButton(requireActivity()).apply {
                text = getLanguageFormat(requireActivity(), lang)
                textSize = 18f
                tag = lang
            }
        }
}