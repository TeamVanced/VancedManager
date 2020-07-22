package com.vanced.manager.ui.fragments

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.vanced.manager.R
import com.vanced.manager.ui.MainActivity
import com.vanced.manager.utils.DownloadHelper.download
import com.vanced.manager.utils.InternetTools.getObjectFromJson
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

class DevSettingsFragment: PreferenceFragmentCompat() {

    private var downloadId: Long = 0

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.dev_settings, rootKey)
        activity?.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

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
        val archPref: Preference? = findPreference("device_arch")
        val supportedAbis: Array<String> = Build.SUPPORTED_ABIS

        if (supportedAbis.contains("arm64-v8a") || supportedAbis.contains("x86_64")) {
            archPref?.summary = "64bit"
        } else {
            archPref?.summary = "32bit"
        }

        val forceUpdate: Preference? = findPreference("force_update")
        forceUpdate?.setOnPreferenceClickListener {
            runBlocking {
                launch {
                    val url = getObjectFromJson("https://x1nto.github.io/VancedFiles/manager.json", "url")
                    downloadId = activity?.let { download(url, "apk", "manager.apk", it) }!!
                }
            }
            true
        }

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) == downloadId) {
                activity?.let {
                    val apk = File("${activity?.getExternalFilesDir("apk")}/manager.apk")
                    val uri =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            FileProvider.getUriForFile(activity!!, "${activity?.packageName}.provider", apk)
                        else
                            Uri.fromFile(apk)

                    val mIntent = Intent(Intent.ACTION_VIEW)
                    mIntent.setDataAndType(uri, "application/vnd.android.package-archive")
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    mIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    startActivity(mIntent)
                }
            }
        }

    }

}