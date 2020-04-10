package com.vanced.manager.ui.fragments

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
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.vanced.manager.Adapter.SectionPageAdapter
import com.vanced.manager.R
import com.vanced.manager.ui.VancedInstallActivity
import com.vanced.manager.ui.MainActivity

class HomeFragment : Fragment() {

    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
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

        viewPager = view.findViewById(R.id.viewpager)
        viewPager.adapter = SectionPageAdapter(FragmentActivity())

        val tabLayout = view.findViewById(R.id.tablayout) as TabLayout

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Vanced"
                1 -> tab.text = "MicroG"
            }
        }.attach()

        val builder = CustomTabsIntent.Builder()
        val pm = activity?.packageManager

        val microguninstallbtn = getView()?.findViewById(R.id.microg_uninstallbtn) as Button
        val microgsettingsbtn = getView()?.findViewById(R.id.microg_settingsbtn) as Button
        val vancedinstallbtn = getView()?.findViewById(R.id.vanced_installbtn) as Button

        val bravebtn = getView()?.findViewById(R.id.brave_button) as Button
        val websitebtn = getView()?.findViewById(R.id.website_button) as Button
        val discordbtn = getView()?.findViewById(R.id.discordbtn) as Button
        val telegrambtn = getView()?.findViewById(R.id.tgbtn) as Button
        val twitterbtn = getView()?.findViewById(R.id.twitterbtn) as Button
        val redditbtn = getView()?.findViewById(R.id.redditbtn) as Button

        val microgStatus = pm?.let { isPackageInstalled("com.mgoogle.android.gms", it) }
        val vancedStatus = pm?.let { isPackageInstalled("com.vanced.android.youtube", it)}

        vancedinstallbtn.setOnClickListener{
            val intent = Intent(activity, VancedInstallActivity::class.java)
            startActivity(intent)
        }

        if (microgStatus!!) {
            microguninstallbtn.setOnClickListener {
                val uri = Uri.parse("package:com.mgoogle.android.gms")
                val mgUninstall = Intent(Intent.ACTION_DELETE, uri)
                startActivity(mgUninstall)
            }

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
            microguninstallbtn.visibility = View.INVISIBLE
        }

         bravebtn.setOnClickListener {
            val braveurl = "https://brave.com/van874"
            builder.setToolbarColor(ContextCompat.getColor(requireContext(),
                R.color.Brave
            ))
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(braveurl))
        }
        websitebtn.setOnClickListener {
            val vancedurl = "https://vanced.app"
            builder.setToolbarColor(ContextCompat.getColor(requireContext(),
                R.color.Vanced
            ))
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(vancedurl))
        }
        discordbtn.setOnClickListener {
            val discordurl = "https://discord.gg/TUVd7rd"
            builder.setToolbarColor(ContextCompat.getColor(requireContext(),
                R.color.Discord
            ))
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(discordurl))
        }
        telegrambtn.setOnClickListener {
            val telegramurl = "https://t.me/joinchat/AAAAAEHf-pi4jH1SDIAL4w"
            builder.setToolbarColor(ContextCompat.getColor(requireContext(),
                R.color.Telegram
            ))
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(telegramurl))
        }
        twitterbtn.setOnClickListener {
            val twitterurl = "https://twitter.com/YTVanced"
            builder.setToolbarColor(ContextCompat.getColor(requireContext(),
                R.color.Twitter
            ))
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(twitterurl))
        }
        redditbtn.setOnClickListener {
            val redditurl = "https://reddit.com/r/vanced"
            builder.setToolbarColor(ContextCompat.getColor(requireContext(),
                R.color.Reddit
            ))
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(redditurl))
        }

    }

}

