package com.vanced.manager.core.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.google.android.material.button.MaterialButton
import com.topjohnwu.superuser.Shell
import com.vanced.manager.R
import com.vanced.manager.core.base.BaseFragment
import com.vanced.manager.core.downloader.MicrogDownloadService
import com.vanced.manager.core.downloader.VancedDownloadService
import com.vanced.manager.ui.MainActivity
import com.vanced.manager.ui.dialogs.DialogContainer.showRootDialog
import com.vanced.manager.utils.PackageHelper.uninstallApk

open class Home : BaseFragment(), View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val microginstallbtn = view.findViewById<MaterialButton>(R.id.microg_installbtn)
        val vancedinstallbtn = view.findViewById<MaterialButton>(R.id.vanced_installbtn)
        val microguninstallbtn = view.findViewById<ImageView>(R.id.microg_uninstallbtn)
        val vanceduninstallbtn = view.findViewById<ImageView>(R.id.vanced_uninstallbtn)
        val rootswitch = view.findViewById<MaterialButton>(R.id.root_switch)
        val nonrootswitch = view.findViewById<MaterialButton>(R.id.nonroot_switch)

        vancedinstallbtn.setOnClickListener(this)
        microginstallbtn.setOnClickListener(this)
        microguninstallbtn.setOnClickListener(this)
        vanceduninstallbtn.setOnClickListener(this)
        rootswitch.setOnClickListener(this)
        nonrootswitch.setOnClickListener(this)

    }

    override fun onResume() {
        super.onResume()
        val prefs = activity?.getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
        val isInstalling = prefs?.getBoolean("isInstalling", false)
        if (isInstalling!!) {
            activity?.startService(Intent(activity, VancedDownloadService::class.java))
            prefs.edit().putBoolean("isInstalling", false).apply()
        }
    }

    override fun onClick(v: View?) {
        val prefs = activity?.getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
        val isVancedDownloading: Boolean? = prefs?.getBoolean("isVancedDownloading", false)
        val isMicrogDownloading: Boolean? = prefs?.getBoolean("isMicrogDownloading", false)
        val variant = getDefaultSharedPreferences(activity).getString("vanced_variant", "nonroot")
        val vancedPkgName =
            if (variant == "root") {
                "com.google.android.youtube"
            } else {
                "com.vanced.android.youtube"
            }

        when (v?.id) {
            R.id.vanced_installbtn -> {
                if (!isVancedDownloading!!) {
                    try {
                        activity?.cacheDir?.deleteRecursively()
                    } catch (e: Exception) {
                        Log.d("VMCache", "Unable to delete cacheDir")
                    }
                    if (prefs.getBoolean("valuesModified", false)) {
                        activity?.startService(
                            Intent(
                                activity,
                                VancedDownloadService::class.java
                            )
                        )
                    } else {
                        view?.findNavController()?.navigate(R.id.toInstallThemeFragment)
                    }
                } else {
                    Toast.makeText(activity, activity?.getString(R.string.installation_wait), Toast.LENGTH_SHORT).show()
                }
            }
            R.id.microg_installbtn -> {
                if (!isMicrogDownloading!!) {
                    activity?.startService(Intent(activity, MicrogDownloadService::class.java))
                } else {
                    Toast.makeText(activity, activity?.getString(R.string.installation_wait), Toast.LENGTH_SHORT).show()
                }
            }
            R.id.microg_uninstallbtn -> activity?.let { uninstallApk("com.mgoogle.android.gms", it) }
            R.id.vanced_uninstallbtn -> activity?.let { uninstallApk(vancedPkgName, it) }
            R.id.nonroot_switch -> writeToVariantPref("nonroot", R.anim.slide_in_left, R.anim.slide_out_right)
            R.id.root_switch ->
                if (Shell.rootAccess()) {
                    writeToVariantPref("root", R.anim.slide_in_right, R.anim.slide_out_left)
                } else {
                    writeToVariantPref("nonroot", R.anim.slide_in_left, R.anim.slide_out_right)
                    Toast.makeText(activity, activity?.getString(R.string.root_not_granted), Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun writeToVariantPref(variant: String, animIn: Int, animOut: Int) {
        val prefs = getDefaultSharedPreferences(activity)
        if (prefs.getString("vanced_variant", "nonroot") != variant) {
            prefs.edit().putString("vanced_variant", variant).apply()
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.overridePendingTransition(animIn, animOut)
            activity?.finish()
        } else
            Log.d("VMVariant", "$variant is already selected")
    }

}