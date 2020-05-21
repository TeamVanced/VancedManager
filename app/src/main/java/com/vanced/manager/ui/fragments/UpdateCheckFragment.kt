package com.vanced.manager.ui.fragments

import android.annotation.SuppressLint
import android.app.DownloadManager
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
import androidx.fragment.app.DialogFragment
import com.dezlum.codelabs.getjson.GetJson
import androidx.core.content.FileProvider
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.FetchConfiguration
import com.tonyodev.fetch2.NetworkType
import com.tonyodev.fetch2.Priority
import com.vanced.manager.BuildConfig

import com.vanced.manager.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.Request
import okio.Okio
import zlc.season.rxdownload4.download
import zlc.season.rxdownload4.file
import zlc.season.rxdownload4.request.okHttpClient
import java.io.File

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

        closebtn.setOnClickListener { dismiss() }

        if (GetJson().isConnected(requireContext())) {
            val checkUrl = GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/manager.json")
            val remoteVersion = checkUrl.get("versionCode").asInt

            if (remoteVersion > BuildConfig.VERSION_CODE) {

                recheckbtn.visibility = View.GONE
                checkingTxt.text = "Update Found!"

                updatebtn.setOnClickListener {

                    val apkUrl = GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/manager.json")
                    val dwnldUrl = apkUrl.get("url").asString

                    disposable = dwnldUrl.download()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                            onNext = {
                                //loadBar.visibility = View.VISIBLE
                                //loadBar.progress = (progress.downloadSize / progress.totalSize).toInt()
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
                            onError = { throwable ->
                               checkingTxt.text = throwable.toString()
                                Log.e("Error", throwable.toString())
                            }
                        )
                }

            } else {
                checkingTxt.text = "No updates found"
            }

        } else checkingTxt.text = "No connection"

    }

}
