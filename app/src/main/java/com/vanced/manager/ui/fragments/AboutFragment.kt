package com.vanced.manager.ui.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat

import com.vanced.manager.R

/**
 * A simple [Fragment] subclass.
 */
class AboutFragment : Fragment() {

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

    private fun openUrl(Url: String, color: Int) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(requireContext(), color))
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(Url))
    }

}
