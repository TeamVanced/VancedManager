package com.vanced.manager.ui.fragments

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.content.IntentFilter
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
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.google.android.material.button.MaterialButton
import com.vanced.manager.R
import com.vanced.manager.utils.InternetTools
import com.vanced.manager.utils.InternetTools.isUpdateAvailable
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

class UpdateCheckFragment : DialogFragment() {

    private var downloadId: Long = 0

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
        activity?.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        checkUpdates()
        view.findViewById<Button>(R.id.update_center_dismiss).setOnClickListener { dismiss() }
        view.findViewById<MaterialButton>(R.id.update_center_recheck).setOnClickListener{ checkUpdates() }
    }

    override fun onPause() {
        super.onPause()
        activity?.unregisterReceiver(receiver)
    }

    private fun checkUpdates() {
        val updatebtn = view?.findViewById<Button>(R.id.update_center_update)
        val checkingTxt = view?.findViewById<TextView>(R.id.update_center_checking)

        runBlocking {
            if (isUpdateAvailable()) {
                view?.findViewById<Button>(R.id.update_center_recheck)?.visibility = View.GONE
                checkingTxt?.text = getString(R.string.update_found)

                updatebtn?.setOnClickListener {
                    upgradeManager()
                }
            } else checkingTxt?.text = getString(R.string.update_notfound)
        }
    }

    private fun upgradeManager() {
        runBlocking {
            launch {
                val changelogTxt = view?.findViewById<TextView>(R.id.microg_changelog)

                val dwnldUrl = InternetTools.getObjectFromJson("https://x1nto.github.io/VancedFiles/manager.json", "url");
                //val loadBar = view?.findViewById<ProgressBar>(R.id.update_center_progressbar)

                val request = DownloadManager.Request(Uri.parse(dwnldUrl))
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                request.setTitle(activity?.getString(R.string.downloading_file, "Manager"))
                request.setDestinationUri(Uri.fromFile(File("${activity?.filesDir?.path}/manager.apk")))

                val downloadManager = activity?.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                downloadId = downloadManager.enqueue(request)
            }
        }
    }

        /*
        PRDownloader.download(dwnldUrl, activity?.filesDir?.path, "manager.apk")
            .build()
            .setOnProgressListener { progress ->
                val mProgress = progress.currentBytes * 100 / progress.totalBytes
                loadBar?.visibility = View.VISIBLE
                loadBar?.progress = mProgress.toInt()

            }
            .start(object : OnDownloadListener{
                override fun onDownloadComplete() {
                    activity?.let {
                        val apk = File("${activity?.filesDir?.path}/manager.apk")
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

                override fun onError(error: Error?) {
                    Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT).show()
                    Log.e("VMUpgrade", error.toString())
                }
            })

         */

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) == downloadId) {
                activity?.let {
                    val apk = File("${activity?.filesDir?.path}/manager.apk")
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
