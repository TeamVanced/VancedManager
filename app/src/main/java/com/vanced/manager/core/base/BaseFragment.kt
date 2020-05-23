package com.vanced.manager.core.base

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import zlc.season.rxdownload4.download
import zlc.season.rxdownload4.file
import zlc.season.rxdownload4.task.Task
import zlc.season.rxdownload4.utils.getFileNameFromUrl

open class BaseFragment : Fragment() {

    fun openUrl(Url: String, color: Int) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(requireContext(), color))
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(Url))
    }

    fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    @SuppressLint("CheckResult")
    fun downloadSplit(apk: String, apkVar: String, isInstalling: Boolean, loadBar: ProgressBar, navigate: Int) {
        val baseurl = "https://x1nto.github.io/VancedFiles/Splits/"
        val url: String =
            when(apk) {
                "theme" -> baseurl + "Theme/" + apkVar + ".apk"
                "lang" -> baseurl + "Language/split_config." + apkVar + ".apk"
                "arch" -> baseurl + "Config/config." + apkVar + ".apk"
                else -> return
            }

        if (url.file().exists())
            url.file().delete()

        activity?.filesDir?.path?.let {
            Task(url = url, saveName = getFileNameFromUrl(url), savePath = it)
                .download()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = { progress ->
                        loadBar.visibility = View.VISIBLE
                        loadBar.progress = progress.percent().toInt()
                    },
                    onComplete = {
                        if (isInstalling) {
                            //So we should implement installation here.
                            //That will be done later
                        }
                        view?.findNavController()?.navigate(navigate)
                    },
                    onError = { throwable ->
                        Toast.makeText(requireContext(), throwable.toString(), Toast.LENGTH_SHORT).show()
                    }

                )
        }

    }
    fun downloadEn() {
        val url = "https://x1nto.github.io/VancedFiles/Splits/Language/split_config.en.apk"
        activity?.filesDir?.path?.let {
            Task(url = url, saveName = getFileNameFromUrl(url), savePath = it)
                .download()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {
                    },
                    onComplete = {
                    },
                    onError = { throwable ->
                        Toast.makeText(requireContext(), throwable.toString(), Toast.LENGTH_SHORT).show()
                    }

                )
        }
    }

}