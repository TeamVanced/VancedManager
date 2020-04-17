package com.vanced.manager.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.vanced.manager.R
import com.vanced.manager.ui.core.BaseFragment

/**
 * A simple [Fragment] subclass.
 */
class AboutFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val githubSource = getView()?.findViewById(R.id.about_github_button) as TextView
        val license = getView()?.findViewById(R.id.about_license_button) as TextView

        githubSource.setOnClickListener{
            openUrl("https://github.com/YTvanced/VancedInstaller", R.color.GitHub)
        }

        license.setOnClickListener{
            openUrl("https://raw.githubusercontent.com/YTVanced/VancedInstaller/dev/LICENSE", R.color.GitHub)

        }
    }
}
