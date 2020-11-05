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
import com.vanced.manager.core.App
import com.vanced.manager.utils.LanguageHelper.getDefaultVancedLanguages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class VancedLanguageSelectionDialog : BottomSheetDialogFragment() {

    private lateinit var langs: MutableList<String>
    private val prefs by lazy { requireActivity().getSharedPreferences("installPrefs", Context.MODE_PRIVATE) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vanced_language_selection, container, false)
    }

    @ExperimentalStdlibApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val app = activity?.application as App
        langs = app.vanced.get()?.array<String>("langs")?.value ?: mutableListOf("null")
        loadBoxes(view.findViewById(R.id.lang_button_ll))
        view.findViewById<MaterialButton>(R.id.vanced_install_finish).setOnClickListener {
            val chosenLangs = mutableListOf<String>()
            if (!langs.contains("null"))
                langs.forEach { lang ->
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

    @ExperimentalStdlibApi
    private fun loadBoxes(ll: LinearLayout) {
        CoroutineScope(Dispatchers.Main).launch {
            val langPrefs = prefs.getString("lang", getDefaultVancedLanguages(requireActivity()))
            if (this@VancedLanguageSelectionDialog::langs.isInitialized) {
                if (!langs.contains("null")) {
                    langs.forEach { lang ->
                        val loc = Locale(lang)
                        val box: MaterialCheckBox = MaterialCheckBox(requireActivity()).apply {
                            tag = lang
                            isChecked = langPrefs!!.contains(lang)
                            text = loc.getDisplayLanguage(loc).capitalize(Locale.ROOT)
                            textSize = 18F
                            typeface = ResourcesCompat.getFont(requireActivity(), R.font.exo_bold)
                        }
                        ll.addView(box, MATCH_PARENT, WRAP_CONTENT)
                    }
                }
            } else
                loadBoxes(ll)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        VancedPreferencesDialog().show(requireActivity().supportFragmentManager, "")
    }

}
