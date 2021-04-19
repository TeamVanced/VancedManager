package com.vanced.manager.ui.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.material.checkbox.MaterialCheckBox
import com.vanced.manager.R
import com.vanced.manager.core.ui.base.BindingBottomSheetDialogFragment
import com.vanced.manager.core.ui.ext.showDialog
import com.vanced.manager.databinding.DialogVancedLanguageSelectionBinding
import com.vanced.manager.ui.core.ThemedMaterialCheckbox
import com.vanced.manager.utils.installPrefs
import com.vanced.manager.utils.lang
import com.vanced.manager.utils.vanced
import java.util.*

class VancedLanguageSelectionDialog :
    BindingBottomSheetDialogFragment<DialogVancedLanguageSelectionBinding>() {

    companion object {

        fun newInstance(): VancedLanguageSelectionDialog = VancedLanguageSelectionDialog().apply {
            arguments = Bundle()
        }
    }

    private val langs = vanced.value?.array<String>("langs")?.value
    private val prefs by lazy { requireActivity().installPrefs }

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DialogVancedLanguageSelectionBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        bindData()
    }

    private fun bindData() {
        with(binding) {
            langButtonLl.loadBoxes()
            vancedInstallFinish.setOnClickListener {
                val chosenLangs = mutableListOf<String>()
                langs?.forEach { lang ->
                    if (root.findViewWithTag<MaterialCheckBox>(lang).isChecked) {
                        chosenLangs.add(lang)
                    }
                }
                if (chosenLangs.isEmpty()) {
                    Toast.makeText(
                        requireActivity(),
                        R.string.select_at_least_one_lang,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                prefs.lang = chosenLangs.joinToString()
                dismiss()
            }
        }
    }

    private fun LinearLayout.loadBoxes() {
        val langPrefs = prefs.lang
        langs?.forEach { lang ->
            val loc = Locale(lang)
            val box = ThemedMaterialCheckbox(requireActivity()).apply {
                tag = lang
                isChecked = langPrefs?.contains(lang) ?: false
                text = loc.getDisplayLanguage(loc).capitalize(Locale.ROOT)
                textSize = 18F
            }
            addView(box, MATCH_PARENT, WRAP_CONTENT)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        showDialog(VancedPreferencesDialog())
    }
}
