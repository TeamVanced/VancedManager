package com.vanced.manager.core.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.google.android.material.button.MaterialButton
import com.topjohnwu.superuser.Shell
import com.vanced.manager.R
import com.vanced.manager.core.base.BaseFragment
import com.vanced.manager.core.downloader.MicrogDownloadService
import com.vanced.manager.core.downloader.VancedDownloadService
import com.vanced.manager.core.installer.StubInstaller
import com.vanced.manager.ui.MainActivity
import com.vanced.manager.ui.dialogs.DialogContainer.secondMiuiDialog
import com.vanced.manager.utils.MiuiHelper
import com.vanced.manager.utils.PackageHelper.uninstallApk

open class Home : BaseFragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val microginstallbtn = view.findViewById<MaterialButton>(R.id.microg_installbtn)
        val vancedinstallbtn = view.findViewById<MaterialButton>(R.id.vanced_installbtn)
        val signaturebtn = view.findViewById<MaterialButton>(R.id.signature_button)
        val microguninstallbtn = view.findViewById<ImageView>(R.id.microg_uninstallbtn)
        val vanceduninstallbtn = view.findViewById<ImageView>(R.id.vanced_uninstallbtn)
        val spinner: Spinner = view.findViewById(R.id.home_variant_selector)

        val variantPref = getDefaultSharedPreferences(activity).getString("vanced_variant", "nonroot")

        activity?.let {
            ArrayAdapter.createFromResource(it,
                R.array.vanced_variant,
                android.R.layout.simple_spinner_item).also { arrayAdapter ->
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = arrayAdapter
            }
        }

        vancedinstallbtn.setOnClickListener(this)
        microginstallbtn.setOnClickListener(this)
        signaturebtn.setOnClickListener(this)
        microguninstallbtn.setOnClickListener(this)
        vanceduninstallbtn.setOnClickListener(this)

        when (variantPref) {
            "nonroot" -> spinner.setSelection(0)
            "root" -> {
                if (Shell.rootAccess())
                    spinner.setSelection(1)
                else
                    spinner.setSelection(0)
            }
        }

        spinner.onItemSelectedListener = this


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
        val variant = getDefaultSharedPreferences(activity)
            .getString("vanced_variant", "nonroot")
        val vancedPkgName =
            if (variant == "root") {
                "com.google.android.youtube"
            } else {
                "com.vanced.android.youtube"
            }

        when (v?.id) {
            R.id.vanced_installbtn -> {
                if (!isVancedDownloading!!) {
                    if (MiuiHelper.isMiui()) {
                        activity?.let {
                            secondMiuiDialog(it)
                        }
                    }
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
                        prefs.edit().putBoolean("isInstalling", false).apply()
                    } else
                        view?.findNavController()?.navigate(R.id.toInstallThemeFragment)
                } else {
                    Toast.makeText(
                        activity,
                        "Please wait until installation finishes",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            R.id.microg_installbtn -> {
                if (!isMicrogDownloading!!) {
                    try {
                        activity?.startService(Intent(activity, MicrogDownloadService::class.java))

                    } catch (e: Exception) {
                        Toast.makeText(activity, "Unable to start installation", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(activity, "Please wait until installation finishes", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.signature_button -> {
                val loadCircle = view?.findViewById<ProgressBar>(R.id.signature_loading)
                loadCircle?.visibility = View.VISIBLE
                val intent = Intent(activity, StubInstaller::class.java)
                activity?.startService(intent)
            }
            R.id.microg_uninstallbtn -> activity?.let { uninstallApk("com.mgoogle.android.gms", it) }
            R.id.vanced_uninstallbtn -> activity?.let { uninstallApk(vancedPkgName, it) }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Ehh we don't need it")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            0 -> writeToVariantPref("nonroot")
            1 -> {
                if (Shell.rootAccess())
                    writeToVariantPref("root")
                else
                    writeToVariantPref("nonroot")
            }
        }
    }

    private fun writeToVariantPref(variant: String) {
        val prefs = getDefaultSharedPreferences(activity)
        if (prefs.getString("vanced_variant", "nonroot") != variant) {
            prefs.edit().putString("vanced_variant", variant).apply()
            restartActivity()
        } else Log.d("VMvariant", "$variant is already selected")
    }

    private fun restartActivity() {
        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()
    }

}