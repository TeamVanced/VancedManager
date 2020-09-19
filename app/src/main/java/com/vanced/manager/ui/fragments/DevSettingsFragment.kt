package com.vanced.manager.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.vanced.manager.R
import com.vanced.manager.ui.MainActivity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

class DevSettingsFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.dev_settings, rootKey)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        val ftSwitch: Preference? = findPreference("firststart_switch")

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        ftSwitch?.setOnPreferenceClickListener {

            AlertDialog.Builder(requireContext())
                .setTitle("FirstStart activated")
                .setMessage("boolean will be activated on next app start")
                .setPositiveButton("Restart") { _, _ ->
                    run {
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        activity?.finish()
                    }
                }
                .create()
                .show()

            prefs.edit().putBoolean("firstStart", true).apply()
            true

        }
        val supportedAbis: Array<String> = Build.SUPPORTED_ABIS

        findPreference<Preference>("device_arch")?.summary =
            if (supportedAbis.contains("arm64-v8a") || supportedAbis.contains("x86_64")) {
                "64bit"
            } else {
                "32bit"
            }

        findPreference<Preference>("device_os")?.summary = "${Build.VERSION.CODENAME} (API ${Build.VERSION.SDK_INT})"

        val forceUpdate: Preference? = findPreference("force_update")
        forceUpdate?.setOnPreferenceClickListener {
            runBlocking {
                launch {
                    val url = "https://github.com/YTVanced/VancedManager/releases/latest/download/manager.apk"
                    //downloadId = activity?.let { download(url, "apk", "manager.apk", it) }!!
                    PRDownloader.download(url, activity?.getExternalFilesDir("apk")?.path, "manager.apk")
                        .build()
                        .start(object : OnDownloadListener {
                            override fun onDownloadComplete() {
                                activity?.let {
                                    val apk = File("${activity?.getExternalFilesDir("apk")?.path}/manager.apk")
                                    val uri =
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                                            FileProvider.getUriForFile(activity!!, "${activity?.packageName}.provider", apk)
                                        else
                                            Uri.fromFile(apk)

                                    val intent = Intent(Intent.ACTION_VIEW)
                                    intent.setDataAndType(uri, "application/vnd.android.package-archive")
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    startActivity(intent)
                                }
                            }

                            override fun onError(error: com.downloader.Error?) {
                                Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT).show()
                                Log.e("VMUpgrade", error.toString())
                            }

                        })
                }
            }
            true
        }

    }

}