package com.vanced.manager.core.fragments

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.navigation.findNavController
import com.vanced.manager.R
import com.vanced.manager.core.base.BaseFragment

open class Home : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pm = activity?.packageManager

        //Damn that's a lot of buttons
        val microginstallbtn = getView()?.findViewById(R.id.microg_installbtn) as Button
        val microguninstallbtn = getView()?.findViewById(R.id.microg_uninstallbtn) as Button
        val microgsettingsbtn = getView()?.findViewById(R.id.microg_settingsbtn) as Button
        val vancedinstallbtn = getView()?.findViewById(R.id.vanced_installbtn) as Button

        val bravebtn = getView()?.findViewById(R.id.brave_button) as Button
        val websitebtn = getView()?.findViewById(R.id.website_button) as Button
        val discordbtn = getView()?.findViewById(R.id.discordbtn) as Button
        val telegrambtn = getView()?.findViewById(R.id.tgbtn) as Button
        val twitterbtn = getView()?.findViewById(R.id.twitterbtn) as Button
        val redditbtn = getView()?.findViewById(R.id.redditbtn) as Button

        //we need to check whether these apps are installed or not
        val microgStatus = pm?.let { isPackageInstalled("com.mgoogle.android.gms", it) }
        val vancedStatus = pm?.let { isPackageInstalled("com.vanced.android.youtube", it) }

        vancedinstallbtn.setOnClickListener {
            view.findNavController().navigate(R.id.toInstallThemeFragment)
        }

        microginstallbtn.setOnClickListener {
            openUrl("https://youtu.be/dQw4w9WgXcQ", R.color.YT)
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
        } else {
            microgsettingsbtn.visibility = View.INVISIBLE
            microguninstallbtn.visibility = View.INVISIBLE
        }

        bravebtn.setOnClickListener {
            openUrl("https://brave.com/van874", R.color.Brave)

        }
        websitebtn.setOnClickListener {
            openUrl("https://vanced.app", R.color.Vanced)
        }
        discordbtn.setOnClickListener {
            openUrl("https://discord.gg/TUVd7rd", R.color.Discord)

        }
        telegrambtn.setOnClickListener {
            openUrl("https://t.me/joinchat/AAAAAEHf-pi4jH1SDlAL4w", R.color.Telegram)

        }
        twitterbtn.setOnClickListener {
            openUrl("https://twitter.com/YTVanced", R.color.Twitter)

        }
        redditbtn.setOnClickListener {
            openUrl("https://reddit.com/r/vanced", R.color.Reddit)
        }

    }

}