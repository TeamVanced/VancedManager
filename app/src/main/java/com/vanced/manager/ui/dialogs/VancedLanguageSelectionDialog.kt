package com.vanced.manager.ui.dialogs

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.edit
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.checkbox.MaterialCheckBox
import com.vanced.manager.R
import com.vanced.manager.databinding.DialogVancedLanguageSelectionBinding
import com.vanced.manager.ui.core.BindingBottomSheetDialogFragment
import com.vanced.manager.utils.Extensions.show
import com.vanced.manager.utils.InternetTools.vanced
import com.vanced.manager.utils.LanguageHelper.getDefaultVancedLanguages
import java.util.*

class VancedLanguageSelectionDialog : BindingBottomSheetDialogFragment<DialogVancedLanguageSelectionBinding>() {

    companion object {

        fun newInstance(): VancedLanguageSelectionDialog = VancedLanguageSelectionDialog().apply {
            arguments = Bundle()
        }
    }

    private val langs = vanced.get()?.array<String>("langs")?.value
    private val prefs by lazy { requireActivity().getSharedPreferences("installPrefs", Context.MODE_PRIVATE) }

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
                    Toast.makeText(requireActivity(), R.string.select_at_least_one_lang, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                prefs?.edit { putString("lang", chosenLangs.joinToString()) }
                dismiss()
            }
        }
    }

    private fun LinearLayout.loadBoxes() {
        val langPrefs = prefs.getString("lang", getDefaultVancedLanguages())
        langs?.forEach { lang ->
            val loc = Locale(lang)
            val box: MaterialCheckBox = MaterialCheckBox(requireActivity()).apply {
                tag = lang
                isChecked = langPrefs?.contains(lang) ?: false
                text = loc.getDisplayLanguage(loc).capitalize(Locale.ROOT)
                textSize = 18F
                typeface = ResourcesCompat.getFont(requireActivity(), R.font.exo_bold)
            }
            addView(box, MATCH_PARENT, WRAP_CONTENT)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        VancedPreferencesDialog().show(requireActivity())
    }
}
