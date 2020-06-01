package com.vanced.manager.core.base

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.dezlum.codelabs.getjson.GetJson
import com.vanced.manager.core.installer.RootSplitInstallerService
import com.vanced.manager.ui.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import zlc.season.rxdownload4.delete
import zlc.season.rxdownload4.download
import zlc.season.rxdownload4.file
import zlc.season.rxdownload4.task.Task
import zlc.season.rxdownload4.utils.getFileNameFromUrl

@SuppressLint("SetTextI18n")
open class BaseFragment : Fragment() {

    private var disposable: Disposable? = null
    private val baseUrl = "https://x1nto.github.io/VancedFiles/Splits"

    fun openUrl(Url: String, color: Int) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(requireContext(), color))
        val customTabsIntent = builder.build()
        activity?.let { customTabsIntent.launchUrl(it, Uri.parse(Url)) }
    }

    fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun downloadArch(loadBar: ProgressBar, dlText: TextView, loadCircle: ProgressBar) {
        val prefs = activity?.getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
        prefs?.edit()?.putBoolean("isVancedDownloading", true)?.apply()
        val arch =
            when {
                Build.SUPPORTED_ABIS.contains("x86") -> "x86"
                Build.SUPPORTED_ABIS.contains("arm64-v8a") -> "arm64_v8a"
                else -> "armeabi_v7a"
            }
        val url = "$baseUrl/Config/config.$arch.apk"
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
                    val filename = getFileNameFromUrl(url)
                    loadBar.visibility = View.VISIBLE
                    dlText.visibility = View.VISIBLE
                    dlText.text = "Downloading $filename..."
                    loadBar.progress = progress.percent().toInt()
                },
                onComplete = {
                    downloadTheme(loadBar, dlText, loadCircle)
                },
                onError = { throwable ->
                    Toast.makeText(activity, throwable.toString(), Toast.LENGTH_SHORT).show()
                }
            )
    }
    private fun downloadTheme(loadBar: ProgressBar, dlText: TextView, loadCircle: ProgressBar) {
        val prefs = activity?.getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
        val theme = prefs?.getString("theme", "dark")
        val url = "$baseUrl/Theme/$theme.apk"

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
                    val filename = getFileNameFromUrl(url)
                    dlText.text = "Downloading $filename..."
                    loadBar.progress = progress.percent().toInt()
                },
                onComplete = {
                    downloadLang(loadBar, dlText, loadCircle)
                },
                onError = { throwable ->
                    Toast.makeText(activity, throwable.toString(), Toast.LENGTH_SHORT).show()
                }
            )
    }

    private fun downloadLang(loadBar: ProgressBar, dlText: TextView, loadCircle: ProgressBar) {
        val prefs = activity?.getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
        val lang = prefs?.getString("lang", "en")
        val url = "$baseUrl/Language/split_config.$lang.apk"

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
                    val filename = getFileNameFromUrl(url)
                    dlText.text = "Downloading $filename..."
                    loadBar.progress = progress.percent().toInt()
                },
                onComplete = {
                    loadBar.visibility = View.GONE
                    if (lang != "en")
                        downloadEn(loadBar, dlText, loadCircle)
                    else {
                        dlText.visibility = View.GONE
                        loadCircle.visibility = View.VISIBLE
                        prefs.edit()?.putBoolean("isVancedDownloading", false)?.apply()
                        launchInstaller()
                    }
                },
                onError = { throwable ->
                    Toast.makeText(activity, throwable.toString(), Toast.LENGTH_SHORT).show()
                }
            )
    }

    private fun downloadEn(loadBar: ProgressBar, dlText: TextView, loadCircle: ProgressBar) {
        val prefs = activity?.getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
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
                onNext = {progress ->
                    val filename = getFileNameFromUrl(url)
                    loadBar.visibility = View.VISIBLE
                    dlText.text = "Downloading $filename..."
                    loadBar.progress = progress.percent().toInt()
                },
                onComplete = {
                    loadBar.visibility = View.GONE
                    dlText.visibility = View.GONE
                    loadCircle.visibility = View.VISIBLE
                    prefs?.edit()?.putBoolean("isVancedDownloading", false)?.apply()
                    if (PreferenceManager.getDefaultSharedPreferences(activity).getString("vanced_variant", "nonroot") == "root") {
                        launchRootInstaller()
                    } else {
                        launchInstaller()
                    }
                },
                onError = { throwable ->
                    Toast.makeText(requireContext(), throwable.toString(), Toast.LENGTH_SHORT).show()
                }

            )

    }

    private fun launchInstaller() {
        val activity = (activity as MainActivity)
        activity.installSplitApk()
    }

    private fun launchRootInstaller() {
        activity?.startService(Intent(activity, RootSplitInstallerService::class.java))
    }

    fun installApk(url: String, loadBar: ProgressBar, dlText: TextView) {
        val prefs = activity?.getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
        prefs?.edit()?.putBoolean("isMicrogDownloading", true)?.apply()

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
            task.file().delete()

        disposable = task
            .download()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { progress ->
                    val filename = getFileNameFromUrl(dwnldUrl)
                    loadBar.visibility = View.VISIBLE
                    dlText.visibility = View.VISIBLE
                    dlText.text = "Downloading $filename..."
                    loadBar.progress = progress.percent().toInt()
                },
                onComplete = {
                    loadBar.visibility = View.GONE
                    dlText.visibility = View.GONE

                    prefs?.edit()?.putBoolean("isMicrogDownloading", false)?.apply()
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
                    startActivity(intent)
                },
                onError = { throwable ->
                    Toast.makeText(requireContext(), throwable.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            )
    }

    fun uninstallApk(pkgUri: String) {
        try {
            val uri = Uri.parse("package:$pkgUri")
            val uninstall = Intent(Intent.ACTION_DELETE, uri)
            startActivity(uninstall)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "Failed to uninstall", Toast.LENGTH_SHORT).show()
        }
    }

}