package com.vanced.manager.core.fragments

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import com.vanced.manager.R
import com.vanced.manager.core.base.BaseFragment

open class Home : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pm = activity?.packageManager

        //Damn that's a lot of buttons
        val microginstallbtn = view.findViewById<Button>(R.id.microg_installbtn)
        val vancedinstallbtn = view.findViewById<Button>(R.id.vanced_installbtn)

        val bravebtn = view.findViewById<Button>(R.id.brave_button)
        val websitebtn = view.findViewById<Button>(R.id.website_button)
        val discordbtn = view.findViewById<Button>(R.id.discordbtn)
        val telegrambtn = view.findViewById<Button>(R.id.tgbtn)
        val twitterbtn = view.findViewById<Button>(R.id.twitterbtn)
        val redditbtn = view.findViewById<Button>(R.id.redditbtn)

        val microguninstallbtn = view.findViewById<ImageView>(R.id.microg_uninstallbtn)
        val microgsettingsbtn = view.findViewById<ImageView>(R.id.microg_settingsbtn)
        val vanceduninstallbtn = view.findViewById<ImageView>(R.id.vanced_uninstallbtn)

        //we need to check whether these apps are installed or not
        val microgStatus = pm?.let { isPackageInstalled("com.mgoogle.android.gms", it) }
        val vancedStatus = pm?.let { isPackageInstalled("com.vanced.android.youtube", it) }

        vancedinstallbtn.setOnClickListener {
            view.findNavController().navigate(R.id.toInstallThemeFragment)
        }

        microginstallbtn.setOnClickListener {
            openUrl("https://youtu.be/dQw4w9WgXcQ", R.color.YT)
        }

        val microgVerText = view.findViewById<TextView>(R.id.microg_installed_version)
        if (microgStatus!!) {
            val microgVer = pm.getPackageInfo("com.mgoogle.android.gms", 0)
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
            microgVerText.text = microgVer.toString()
        } else {
            microgsettingsbtn.visibility = View.INVISIBLE
            microguninstallbtn.visibility = View.INVISIBLE
            microgVerText.text = getString(R.string.unavailable)
        }

        val vancedVerText = view.findViewById<TextView>(R.id.vanced_installed_version)
        if (vancedStatus!!) {
            val vancedVer = pm.getPackageInfo("com.vanced.android.youtube", 0)
            vanceduninstallbtn.setOnClickListener {
                val uri = Uri.parse("package:com.vanced.android.youtube")
                val vanUninstall = Intent(Intent.ACTION_DELETE, uri)
                startActivity(vanUninstall)
            }
            vancedVerText.text = vancedVer.toString()
        } else {
            vanceduninstallbtn.visibility = View.INVISIBLE
            vancedVerText.text = getString(R.string.unavailable)
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