package com.vanced.manager.core.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.google.android.material.button.MaterialButton
import com.vanced.manager.R
import com.vanced.manager.core.base.BaseFragment
import com.vanced.manager.ui.MainActivity
import com.vanced.manager.utils.MiuiHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import zlc.season.rxdownload4.download
import zlc.season.rxdownload4.file
import zlc.season.rxdownload4.task.Task
import zlc.season.rxdownload4.utils.getFileNameFromUrl

open class Home : BaseFragment() {

    private var disposable: Disposable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val microginstallbtn = view.findViewById<MaterialButton>(R.id.microg_installbtn)
        val vancedinstallbtn = view.findViewById<MaterialButton>(R.id.vanced_installbtn)
        val signaturebtn = view.findViewById<MaterialButton>(R.id.signature_button)

        val microgProgress = view.findViewById<ProgressBar>(R.id.microg_progress)
        val prefs = activity?.getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
        val isVancedDownloading: Boolean? = prefs?.getBoolean("isVancedDownloading", false)
        val isMicrogDownloading: Boolean? = prefs?.getBoolean("isMicrogDownloading", false)

        vancedinstallbtn.setOnClickListener {
            if (!isVancedDownloading!!) {
                val mainActivity = (activity as MainActivity)
                if (PreferenceManager.getDefaultSharedPreferences(activity).getString("vanced_variant", "Nonroot") == "Root") {
                    if (MiuiHelper.isMiui()) {
                        mainActivity.secondMiuiDialog()
                    } else
                        mainActivity.rootModeDetected()
                } else {
                    if (MiuiHelper.isMiui()) {
                        mainActivity.secondMiuiDialog()
                    }
                }
                try {
                    activity?.cacheDir?.deleteRecursively()
                } catch (e: Exception) {
                    Log.d("VMCache", "Unable to delete cacheDir")
                }
                if (prefs.getBoolean("valuesModified", false)) {
                    val loadBar = view.findViewById<ProgressBar>(R.id.vanced_progress)
                    val dlText = view.findViewById<TextView>(R.id.vanced_downloading)
                    val loadCircle = view.findViewById<ProgressBar>(R.id.vanced_installing)
                    downloadArch(loadBar!!, dlText!!, loadCircle!!)
                    prefs.edit().putBoolean("isInstalling", false).apply()
                } else
                    view.findNavController().navigate(R.id.toInstallThemeFragment)
            } else {
                Toast.makeText(activity, "Please wait until installation finishes", Toast.LENGTH_SHORT).show()
            }

        }

        microginstallbtn.setOnClickListener {
            if (!isMicrogDownloading!!) {
                val dlText = view.findViewById<TextView>(R.id.microg_downloading)
                try {
                    installApk(
                        "https://x1nto.github.io/VancedFiles/microg.json",
                        microgProgress,
                        dlText
                    )
                } catch (e: Exception) {
                    Toast.makeText(activity, "Unable to start installation", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(activity, "Please wait until installation finishes", Toast.LENGTH_SHORT).show()
            }
        }

        signaturebtn.setOnClickListener {
            val loadCircle = view.findViewById<ProgressBar>(R.id.signature_loading)
            activity?.filesDir?.delete()

            val dwnldUrl = "https://x1nto.github.io/VancedFiles/stub.apk"
            val task = activity?.filesDir?.path?.let {
                Task(
                    url = dwnldUrl,
                    saveName = getFileNameFromUrl(dwnldUrl),
                    savePath = it
                )
            }

            if (task?.file()?.exists()!!)
                task.file().delete()

            disposable = task
                .download()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {
                        loadCircle.visibility = View.VISIBLE
                    },
                    onComplete = {
                    },
                    onError = { throwable ->
                        Toast.makeText(requireContext(), throwable.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                )
        }

    }

    override fun onResume() {
        super.onResume()
        val loadBar = view?.findViewById<ProgressBar>(R.id.vanced_progress)
        val dlText = view?.findViewById<TextView>(R.id.vanced_downloading)
        val loadCircle = view?.findViewById<ProgressBar>(R.id.vanced_installing)
        val prefs = activity?.getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
        val isInstalling = prefs?.getBoolean("isInstalling", false)
        if (isInstalling!!) {
            downloadArch(loadBar!!, dlText!!, loadCircle!!)
            prefs.edit().putBoolean("isInstalling", false).apply()
        }
    }

}