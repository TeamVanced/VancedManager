package com.vanced.manager.core.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.google.android.material.button.MaterialButton
import com.vanced.manager.R
import com.vanced.manager.core.base.BaseFragment
import com.vanced.manager.ui.MainActivity
import com.vanced.manager.utils.MiuiHelper

open class Home : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Damn that's a lot of buttons
        val microginstallbtn = view.findViewById<MaterialButton>(R.id.microg_installbtn)
        val vancedinstallbtn = view.findViewById<MaterialButton>(R.id.vanced_installbtn)

        //val bravebtn = view.findViewById<Button>(R.id.brave_button)
        //val websitebtn = view.findViewById<Button>(R.id.website_button)
        //val discordbtn = view.findViewById<Button>(R.id.discordbtn)
        //val telegrambtn = view.findViewById<Button>(R.id.tgbtn)
        //val twitterbtn = view.findViewById<Button>(R.id.twitterbtn)
        //val redditbtn = view.findViewById<Button>(R.id.redditbtn)

        val microgProgress = view.findViewById<ProgressBar>(R.id.microg_progress)
        val prefs = activity?.getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
        val isVancedDownloading: Boolean? = prefs?.getBoolean("isVancedDownloading", false)
        val isMicrogDownloading: Boolean? = prefs?.getBoolean("isMicrogDownloading", false)

        vancedinstallbtn.setOnClickListener {
            if (!isVancedDownloading!!) {
                val mainActivity = (activity as MainActivity)
                if (PreferenceManager.getDefaultSharedPreferences(activity).getString("vanced_variant", "Nonroot") == "Root") {
                    if (MiuiHelper.isMiui()) {
                        mainActivity.secondMiuiDialog()
                    } else
                        mainActivity.rootModeDetected()
                } else {
                    if (MiuiHelper.isMiui()) {
                        mainActivity.secondMiuiDialog()
                    }
                }
                try {
                    activity?.cacheDir?.deleteRecursively()
                } catch (e: Exception) {
                    Log.d("VMCache", "Unable to delete cacheDir")
                }
                if (prefs.getBoolean("valuesModified", false)) {
                    val loadBar = view.findViewById<ProgressBar>(R.id.vanced_progress)
                    val dlText = view.findViewById<TextView>(R.id.vanced_downloading)
                    val loadCircle = view.findViewById<ProgressBar>(R.id.vanced_installing)
                    downloadArch(loadBar!!, dlText!!, loadCircle!!)
                    prefs.edit().putBoolean("isInstalling", false).apply()
                } else
                    view.findNavController().navigate(R.id.toInstallThemeFragment)
            } else {
                Toast.makeText(activity, "Please wait until installation finishes", Toast.LENGTH_SHORT).show()
            }

        }

        microginstallbtn.setOnClickListener {
            if (!isMicrogDownloading!!) {
                val dlText = view.findViewById<TextView>(R.id.microg_downloading)
                try {
                    installApk(
                        "https://x1nto.github.io/VancedFiles/microg.json",
                        microgProgress,
                        dlText
                    )
                } catch (e: Exception) {
                    Toast.makeText(activity, "Unable to start installation", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(activity, "Please wait until installation finishes", Toast.LENGTH_SHORT).show()
            }
        }

        /*
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
         */

    }

    override fun onResume() {
        super.onResume()
        val loadBar = view?.findViewById<ProgressBar>(R.id.vanced_progress)
        val dlText = view?.findViewById<TextView>(R.id.vanced_downloading)
        val loadCircle = view?.findViewById<ProgressBar>(R.id.vanced_installing)
        val prefs = activity?.getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
        val isInstalling = prefs?.getBoolean("isInstalling", false)
        if (isInstalling!!) {
            downloadArch(loadBar!!, dlText!!, loadCircle!!)
            prefs.edit().putBoolean("isInstalling", false).apply()
        }
    }

}