package com.vanced.manager.ui.dialogs

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.edit
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.vanced.manager.R
import com.vanced.manager.utils.Extensions.show
import com.vanced.manager.utils.InternetTools.vanced
import com.vanced.manager.utils.LanguageHelper.getDefaultVancedLanguages
import java.util.*

class VancedLanguageSelectionDialog : BottomSheetDialogFragment() {

    private val langs = vanced.get()?.array<String>("langs")?.value
    private val prefs by lazy { requireActivity().getSharedPreferences("installPrefs", Context.MODE_PRIVATE) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_vanced_language_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadBoxes(view.findViewById(R.id.lang_button_ll))
        view.findViewById<MaterialButton>(R.id.vanced_install_finish).setOnClickListener {
            val chosenLangs = mutableListOf<String>()
            langs?.forEach { lang ->
                if (view.findViewWithTag<MaterialCheckBox>(lang).isChecked) {
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

    private fun loadBoxes(ll: LinearLayout) {
        requireActivity().runOnUiThread {
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
                ll.addView(box, MATCH_PARENT, WRAP_CONTENT)
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        VancedPreferencesDialog().show(requireActivity())
    }

}
