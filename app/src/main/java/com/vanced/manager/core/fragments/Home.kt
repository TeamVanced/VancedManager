package com.vanced.manager.core.fragments

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.findNavController
import com.dezlum.codelabs.getjson.GetJson
import com.google.gson.JsonObject
import com.vanced.manager.BuildConfig
import com.vanced.manager.R
import com.vanced.manager.core.base.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import zlc.season.rxdownload4.download
import zlc.season.rxdownload4.file

open class Home : BaseFragment() {

    private var disposable: Disposable? = null

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

        val vancedLatestTxt = view.findViewById<TextView>(R.id.vanced_latest_version)
        val microgLatestTxt = view.findViewById<TextView>(R.id.microg_latest_version)

        if (GetJson().isConnected(requireContext())) {
            val vancedVer: JsonObject = GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/vanced.json")
            val microgVer: JsonObject = GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/microg.json")
            vancedLatestTxt.text = vancedVer.get("version").asString
            microgLatestTxt.text = microgVer.get("version").asString

        } else {
            vancedLatestTxt.text = getString(R.string.unavailable)
            microgLatestTxt.text = getString(R.string.unavailable)
        }

        vancedinstallbtn.setOnClickListener {
            view.findNavController().navigate(R.id.toInstallThemeFragment)
        }

        microginstallbtn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(requireContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    420)

            } else {
                installApk("https://x1nto.github.io/VancedFiles/microg.json")
            }

        }

        val microgVerText = view.findViewById<TextView>(R.id.microg_installed_version)
        if (microgStatus!!) {
            val microgVer = pm.getPackageInfo("com.mgoogle.android.gms", 0).versionName
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
            val vancedVer = pm.getPackageInfo("com.vanced.android.youtube", 0).versionName
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            420 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    installApk("https://x1nto.github.io/VancedFiles/microg.json")
                    Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
                }
                else
                    Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun installApk(url: String) {
        val apkUrl = GetJson().AsJSONObject(url)
        val dwnldUrl = apkUrl.get("url").asString

        if (dwnldUrl.file().exists())
            dwnldUrl.file().delete()

        disposable = dwnldUrl.download()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                },
                onComplete = {
                    val pn = activity?.packageName
                    val apk = dwnldUrl.file()
                    val uri =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            FileProvider.getUriForFile(requireContext(), "$pn.provider", apk)
                        } else
                            Uri.fromFile(apk)
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(uri, "application/vnd.android.package-archive")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    startActivity(intent)
                },
                onError = {
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            )
    }

}