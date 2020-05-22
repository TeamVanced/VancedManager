package com.vanced.manager.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.dezlum.codelabs.getjson.GetJson
import androidx.core.content.FileProvider
import androidx.preference.PreferenceManager
import com.vanced.manager.BuildConfig

import com.vanced.manager.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import zlc.season.rxdownload4.download
import zlc.season.rxdownload4.file
import java.util.jar.Manifest

class UpdateCheckFragment : DialogFragment() {

    private var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return inflater.inflate(R.layout.fragment_update_check, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val closebtn = view.findViewById<Button>(R.id.update_center_dismiss)
        val updatebtn = view.findViewById<Button>(R.id.update_center_update)
        val recheckbtn = view.findViewById<Button>(R.id.update_center_recheck)
        val checkingTxt = view.findViewById<TextView>(R.id.update_center_checking)
        val loadBar = view.findViewById<ProgressBar>(R.id.update_center_progressbar)
        val changelogTitle = view.findViewById<TextView>(R.id.update_center_changelog_title)
        val changelogTxt = view.findViewById<TextView>(R.id.update_center_changelog)

        closebtn.setOnClickListener { dismiss() }

        if (GetJson().isConnected(requireContext())) {
            val checkUrl = GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/manager.json")
            val remoteVersion = checkUrl.get("versionCode").asInt
            val changelog = checkUrl.get("changelog").asString

            changelogTxt.text = changelog

            if (remoteVersion > BuildConfig.VERSION_CODE) {

                recheckbtn.visibility = View.GONE
                checkingTxt.text = "Update Found!"

                updatebtn.setOnClickListener {

                    if (ContextCompat.checkSelfPermission(requireContext(),
                            android.Manifest.permission.READ_EXTERNAL_STORAGE) +
                        ContextCompat.checkSelfPermission(requireContext(),
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            69)

                    } else {
                        upgradeManager(loadBar, checkingTxt)
                    }


                }

            } else {
                checkingTxt.text = "No updates found"
            }

        } else {
            checkingTxt.text = "No connection"
            changelogTxt.visibility = View.GONE
            changelogTitle.visibility = View.GONE
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            69 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT)
                        .show()
                }
                else
                    Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun upgradeManager(loadBar: ProgressBar, checkingTxt: TextView) {
        val apkUrl = GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/manager.json")
        val dwnldUrl = apkUrl.get("url").asString

        if (dwnldUrl.file().exists())
            dwnldUrl.file().delete()

        disposable = dwnldUrl.download()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { progress ->
                    activity?.runOnUiThread {
                        loadBar.visibility = View.VISIBLE
                        //loadBar.progress = (progress.downloadSize / progress.totalSize).toInt()
                        loadBar.progress = progress.percent().toInt()
                    }

                },
                onComplete = {
                    val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
                    prefs.getBoolean("isUpgrading", false)
                    prefs.edit().putBoolean("isUpgrading", true).apply()

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
                onError = { throwable ->
                    checkingTxt.text = throwable.toString()
                    Log.e("Error", throwable.toString())
                }
            )
    }



}
