package com.vanced.manager.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.radiobutton.MaterialRadioButton
import com.vanced.manager.R

class VancedThemeSelectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.install)
        return inflater.inflate(R.layout.fragment_vanced_theme_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nextButton = view.findViewById<Button>(R.id.vanced_next_to_variant)
        val themeGroup = view.findViewById<RadioGroup>(R.id.theme_radiogroup)
        val prefs = activity?.getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
        themeGroup.findViewWithTag<MaterialRadioButton>(prefs?.getString("theme", "dark")).isChecked = true

        nextButton.setOnClickListener {
            val selectedButton = view.findViewById<MaterialRadioButton>(themeGroup.checkedRadioButtonId)
            prefs?.edit()?.putString("theme", selectedButton.tag.toString())?.apply()
            view.findNavController().navigate(R.id.toInstallLanguageFragment)
        }
    }

}
