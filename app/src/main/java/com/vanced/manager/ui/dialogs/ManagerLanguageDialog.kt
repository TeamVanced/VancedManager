package com.vanced.manager.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.radiobutton.MaterialRadioButton
import com.vanced.manager.BuildConfig.MANAGER_LANGUAGES
import com.vanced.manager.R
import com.vanced.manager.databinding.DialogManagerLanguageBinding
import com.vanced.manager.utils.Extensions.getCheckedButtonTag
import com.vanced.manager.utils.LanguageHelper.getLanguageFormat
import java.util.*

class ManagerLanguageDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogManagerLanguageBinding
    private val prefs by lazy { getDefaultSharedPreferences(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_manager_language, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addRadioButtons()
        val language = prefs.getString("manager_lang", "System Default")
        view.findViewWithTag<MaterialRadioButton>(language).isChecked = true
        binding.languageSave.setOnClickListener {
            val newPref = binding.languageRadiogroup.getCheckedButtonTag()
            if (language != newPref) {
                prefs.edit().putString("manager_lang", newPref).apply()
                dismiss()
                requireActivity().recreate()
            } else {
                dismiss()
            }
        }
    }

    private fun addRadioButtons() {
        requireActivity().runOnUiThread {
            (arrayOf("System Default") + MANAGER_LANGUAGES).forEach { lang ->
                val button = MaterialRadioButton(requireActivity()).apply {
                    text = getLanguageFormat(requireActivity(), lang)
                    textSize = 18f
                    tag = lang
                }
                binding.languageRadiogroup.addView(button, MATCH_PARENT, WRAP_CONTENT)
            }
        }
    }

}