package com.vanced.manager.ui.fragments

import android.content.Intent
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
import androidx.fragment.app.DialogFragment
import com.dezlum.codelabs.getjson.GetJson
import androidx.core.content.FileProvider
import androidx.preference.PreferenceManager
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.vanced.manager.BuildConfig

import com.vanced.manager.R
import io.reactivex.disposables.Disposable
import java.io.File

class UpdateCheckFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return inflater.inflate(R.layout.fragment_update_check, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val closebtn = view.findViewById<Button>(R.id.update_center_dismiss)
        val updatebtn = view.findViewById<Button>(R.id.update_center_update)
        val recheckbtn = view.findViewById<Button>(R.id.update_center_recheck)
        val checkingTxt = view.findViewById<TextView>(R.id.update_center_checking)
        val loadBar = view.findViewById<ProgressBar>(R.id.update_center_progressbar)

        closebtn.setOnClickListener { dismiss() }

        if (GetJson().isConnected(requireContext())) {
            val checkUrl = GetJson().AsJSONObject("https://vanced.app/api/v1/manager.json")
            val remoteVersion = checkUrl.get("versionCode").asInt

            if (remoteVersion > BuildConfig.VERSION_CODE) {

                recheckbtn.visibility = View.GONE
                checkingTxt.text = getString(R.string.update_found)

                updatebtn.setOnClickListener {
                        upgradeManager(loadBar)
                }

            } else {
                checkingTxt.text = getString(R.string.update_notfound)
            }

        } else {
            checkingTxt.text = getString(R.string.network_error)
        }

    }

    private fun upgradeManager(loadBar: ProgressBar) {
        val apkUrl = GetJson().AsJSONObject("https://vanced.app/api/v1/manager.json")
        val dwnldUrl = apkUrl.get("url").asString

        PRDownloader.download(dwnldUrl, activity?.filesDir?.path, "manager.apk")
            .build()
            .setOnProgressListener { progress ->
                val mProgress = progress.currentBytes * 100 / progress.totalBytes
                loadBar.visibility = View.VISIBLE
                loadBar.progress = mProgress.toInt()

            }
            .start(object : OnDownloadListener{
                override fun onDownloadComplete() {
                    val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
                    prefs.getBoolean("isUpgrading", false)
                    prefs.edit().putBoolean("isUpgrading", true).apply()

                    val pn = activity?.packageName
                    val apk = File(activity?.filesDir?.path, "manager.apk")
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
                }

                override fun onError(error: Error?) {
                    Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT).show()
                    Log.e("VMUpgrade", error.toString())
                }
            })

    }



}
