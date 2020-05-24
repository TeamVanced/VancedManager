package com.vanced.manager.core.fragments

import android.os.Bundle
import android.view.View
import android.widget.*
import com.vanced.manager.R
import com.vanced.manager.core.base.BaseFragment

open class ThemeInstall : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nextButton = view.findViewById<Button>(R.id.vanced_next_to_variant)
        val loadBar = view.findViewById<ProgressBar>(R.id.vantheme_progress)
        val themeGroup = view.findViewById<RadioGroup>(R.id.theme_radiogroup)


        nextButton.setOnClickListener {
            val selectedThemeId = themeGroup.checkedRadioButtonId
            val selectedButton = view.findViewById<RadioButton>(selectedThemeId)
            downloadSplit("theme", selectedButton.tag.toString(), false, loadBar, R.id.toInstallVariantFragment)
        }
    }

}