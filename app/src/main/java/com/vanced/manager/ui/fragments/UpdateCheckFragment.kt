package com.vanced.manager.ui.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.dezlum.codelabs.getjson.GetJson
import com.vanced.manager.BuildConfig

import com.vanced.manager.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import zlc.season.rxdownload4.download
import zlc.season.rxdownload4.file

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
        val loadCircle = view.findViewById<ProgressBar>(R.id.update_center_loading)
        val loadBar = view.findViewById<ProgressBar>(R.id.update_center_progressbar)

        closebtn.setOnClickListener { dismiss() }

        if (GetJson().isConnected(requireContext())) {
            val checkUrl = GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/manager.json")
            val remoteVersion = checkUrl.get("versionCode").asInt

            if (remoteVersion > BuildConfig.VERSION_CODE) {

                recheckbtn.visibility = View.GONE
                checkingTxt.text = "Update Found!"
                loadCircle.visibility = View.GONE

                updatebtn.setOnClickListener {
                    val url =
                        "https://x1nto.github.io/VancedFiles/release.apk"
                    url.download()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                            onNext = { progress ->
                                loadBar.visibility = View.VISIBLE
                                loadBar.progress =
                                    "${progress.downloadSizeStr()}/${progress.totalSizeStr()}".toInt()
                            },
                            onComplete = {
                                val apk = url.file()
                                val uri = Uri.fromFile(apk)
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.setDataAndType(uri, "application/vnd.android.package-archive")
                                startActivity(intent)
                            },
                            onError = {
                                checkingTxt.text = "Something went wrong"
                            }
                        )
                }

            } else {
                checkingTxt.text = "No updates found"
            }

        } else checkingTxt.text = "No connection"

    }

}
