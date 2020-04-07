package com.vanced.manager

import android.content.ComponentName
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.content.pm.PackageManager
import android.content.Intent
import android.content.res.ColorStateList
import androidx.browser.customtabs.CustomTabsIntent
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.vanced.manager.ui.MainActivity

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val homefragment = inflater.inflate(R.layout.fragment_home, container, false)

        return homefragment


    }

    private fun isMicrogInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).supportActionBar?.title = getString(R.string.title_home)

        super.onViewCreated(view, savedInstanceState)

        val builder = CustomTabsIntent.Builder()
        val pm = activity?.packageManager

        val bravebtn = getView()?.findViewById(R.id.brave_button) as Button
        val websitebtn = getView()?.findViewById(R.id.website_button) as Button
        val microgsettingsbtn = getView()?.findViewById(R.id.microg_settingsbtn) as Button
        val discordbtn = getView()?.findViewById(R.id.discordbtn) as Button
        val telegrambtn = getView()?.findViewById(R.id.tgbtn) as Button
        val twitterbtn = getView()?.findViewById(R.id.twitterbtn) as Button
        val redditbtn = getView()?.findViewById(R.id.redditbtn) as Button
        val git1btn = getView()?.findViewById(R.id.github_managerbtn) as Button
        val git2btn = getView()?.findViewById(R.id.github_botbtn) as Button
        val git3btn = getView()?.findViewById(R.id.github_websitebtn) as Button

        val microgStatus = pm?.let { isMicrogInstalled("com.mgoogle.android.gms", it) }

        if (microgStatus == true) {
            microgsettingsbtn.setOnClickListener {
                val intent = Intent()
                intent.component = ComponentName(
                    "com.mgoogle.android.gms",
                    "org.microg.gms.ui.SettingsActivity"
                )
                startActivity(intent)
            }
        }
        else {
            microgsettingsbtn.visibility = View.INVISIBLE
        }

         bravebtn.setOnClickListener {
            val braveurl = "https://brave.com/van874"
            builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.Brave))
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(braveurl))
        }
        websitebtn.setOnClickListener {
            val vancedurl = "https://vanced.app"
            builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.Vanced))
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(vancedurl))
        }
        discordbtn.setOnClickListener {
            val discordurl = "https://discord.gg/TUVd7rd"
            builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.Discord))
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(discordurl))
        }
        telegrambtn.setOnClickListener {
            val telegramurl = "https://t.me/joinchat/AAAAAEHf-pi4jH1SDIAL4w"
            builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.Telegram))
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(telegramurl))
        }
        twitterbtn.setOnClickListener {
            val twitterurl = "https://twitter.com/YTVanced"
            builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.Twitter))
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(twitterurl))
        }
        redditbtn.setOnClickListener {
            val redditurl = "https://reddit.com/r/vanced"
            builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.Reddit))
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(redditurl))
        }
        git1btn.setOnClickListener {
            val gitmanagerurl = "https://github.com/YTVanced/VancedInstaller"
            builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.GitHub))
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(gitmanagerurl))
        }
        git2btn.setOnClickListener {
            val gitboturl = "https://github.com/YTVanced/VancedHelper"
            builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.GitHub))
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(gitboturl))
        }
        git3btn.setOnClickListener {
            val gitwebsiteurl = "https://github.com/YTVanced/VancedWebsite"
            builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.GitHub))
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(gitwebsiteurl))
        }


    }

}

