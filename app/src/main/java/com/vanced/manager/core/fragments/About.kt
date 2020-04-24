package com.vanced.manager.core.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.vanced.manager.R
import com.vanced.manager.core.base.BaseFragment

open class About : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val githubSource = getView()?.findViewById(R.id.about_github_button) as Button
        val license = getView()?.findViewById(R.id.about_license_button) as Button

        githubSource.setOnClickListener {
            openUrl("https://github.com/YTvanced/VancedInstaller", R.color.GitHub)
        }

        license.setOnClickListener {
            openUrl(
                "https://raw.githubusercontent.com/YTVanced/VancedInstaller/dev/LICENSE",
                R.color.GitHub
            )

        }
    }

}