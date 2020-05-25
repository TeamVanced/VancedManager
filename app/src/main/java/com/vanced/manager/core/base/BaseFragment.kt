package com.vanced.manager.core.base

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.dezlum.codelabs.getjson.GetJson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import zlc.season.rxdownload4.delete
import zlc.season.rxdownload4.download
import zlc.season.rxdownload4.file
import zlc.season.rxdownload4.task.Task
import zlc.season.rxdownload4.utils.getFileNameFromUrl

open class BaseFragment : Fragment() {

    private var disposable: Disposable? = null

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

    fun downloadSplit(apk: String, apkVar: String, isInstalling: Boolean, loadBar: ProgressBar, navigate: Int) {
        val baseurl = "https://x1nto.github.io/VancedFiles/Splits/"
        val url: String =
            when(apk) {
                "theme" -> baseurl + "Theme/" + apkVar + ".apk"
                "lang" -> baseurl + "Language/split_config." + apkVar + ".apk"
                "arch" -> baseurl + "Config/config." + apkVar + ".apk"
                else -> return
            }

        val task = activity?.cacheDir?.path?.let {
            Task(
                url = url,
                saveName = getFileNameFromUrl(url),
                savePath = it
            )
        }

        if (task?.file()?.exists()!!)
            task.delete()

        disposable = task.download()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { progress ->
                    loadBar.visibility = View.VISIBLE
                    loadBar.progress = progress.percent().toInt()
                },
                onComplete = {
                    loadBar.visibility = View.GONE
                    view?.findNavController()?.navigate(navigate)
                    //if (isInstalling) {
                        //So we should implement installation here.
                        //That will be done later
                    //}
                },
                onError = { throwable ->
                    Toast.makeText(requireContext(), throwable.toString(), Toast.LENGTH_SHORT).show()
                }
            )
    }

    fun downloadEn() {
        val url = "https://x1nto.github.io/VancedFiles/Splits/Language/split_config.en.apk"

        val task = activity?.cacheDir?.path?.let {
            Task(
                url = url,
                saveName = getFileNameFromUrl(url),
                savePath = it
            )
        }
        if (task?.file()?.exists()!!)
            task.delete()

        disposable = task
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

    fun installApk(url: String, loadBar: ProgressBar) {
        val apkUrl = GetJson().AsJSONObject(url)
        val dwnldUrl = apkUrl.get("url").asString
        val task = activity?.filesDir?.path?.let {
            Task(
                url = dwnldUrl,
                saveName = getFileNameFromUrl(dwnldUrl),
                savePath = it
            )
        }

        if (task?.file()?.exists()!!)
            task.delete()

        disposable = task
            .download()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { progress ->
                    loadBar.visibility = View.VISIBLE
                    loadBar.progress = progress.percent().toInt()
                },
                onComplete = {
                    loadBar.visibility = View.GONE

                    val pn = activity?.packageName
                    val apk = task.file()
                    val uri =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            FileProvider.getUriForFile(requireContext(), "$pn.provider", apk)
                        } else
                            Uri.fromFile(apk)
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(uri, "application/vnd.android.package-archive")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    startActivityForResult(intent, 69)
                },
                onError = { throwable ->
                    Toast.makeText(requireContext(), throwable.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            )
    }

    fun uninstallApk(pkgUri: String) {
        try {
            val uri = Uri.parse(pkgUri)
            val uninstall = Intent(Intent.ACTION_DELETE, uri)
            startActivityForResult(uninstall, 69)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "App not installed", Toast.LENGTH_SHORT).show()
            activity?.recreate()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 69) {
            activity?.recreate()
        }
    }


}