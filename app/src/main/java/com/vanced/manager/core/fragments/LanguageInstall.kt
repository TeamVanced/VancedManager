package com.vanced.manager.core.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.navigation.findNavController
import com.vanced.manager.R
import com.vanced.manager.core.base.BaseFragment

open class LanguageInstall : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val finishButton = view.findViewById<Button>(R.id.vanced_install_finish)
        val langGroup = view.findViewById<RadioGroup>(R.id.lang_radiogroup)
        val prefs = activity?.getSharedPreferences("installPrefs", Context.MODE_PRIVATE)

        val langPref = prefs?.getString("lang", "en")
        val button = langGroup.findViewWithTag<RadioButton>(langPref)
        button.isChecked = true

        finishButton.setOnClickListener {
            val selectedLangId = langGroup.checkedRadioButtonId
            val selectedButton = view.findViewById<RadioButton>(selectedLangId)
            prefs?.edit()?.putString("lang", selectedButton.tag.toString())?.apply()
            prefs?.edit()?.putBoolean("isInstalling", true)?.apply()
            prefs?.edit()?.putBoolean("valuesModified", true)?.apply()
            view.findNavController().navigate(R.id.action_installTo_homeFragment)
        }

    }

}