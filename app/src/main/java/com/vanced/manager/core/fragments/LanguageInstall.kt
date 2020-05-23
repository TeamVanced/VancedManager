package com.vanced.manager.core.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioGroup
import com.vanced.manager.R
import com.vanced.manager.core.base.BaseFragment

open class LanguageInstall : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val finishButton = view.findViewById<Button>(R.id.vanced_install_finish)
        val loadBar = view.findViewById<ProgressBar>(R.id.vanlang_progress)
        val langGroup = view.findViewById<RadioGroup>(R.id.lang_radiogroup)
        val selectedLangId = langGroup.checkedRadioButtonId

        finishButton.setOnClickListener {
            if (selectedLangId.toString() != "en")
                downloadEn()
            downloadSplit("lang", selectedLangId.toString(), true, loadBar, R.id.action_installTo_homeFragment)
        }

    }

}