package com.vanced.manager.core.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import com.vanced.manager.R
import com.vanced.manager.core.base.BaseFragment

open class LanguageInstall : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val finishButton = view.findViewById<Button>(R.id.vanced_install_finish)
        val loadBar = view.findViewById<ProgressBar>(R.id.vanlang_progress)
        val langGroup = view.findViewById<RadioGroup>(R.id.lang_radiogroup)

        finishButton.setOnClickListener {
            val selectedLangId = langGroup.checkedRadioButtonId
            val selectedButton = view.findViewById<RadioButton>(selectedLangId)
            prefs?.edit()?.putString("lang", selectedButton.tag.toString())?.apply()
            prefs?.edit()?.putBoolean("isInstalling", true)?.apply()
        }

    }

}