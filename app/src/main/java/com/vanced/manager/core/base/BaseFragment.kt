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
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import zlc.season.rxdownload4.delete
import zlc.season.rxdownload4.download
import zlc.season.rxdownload4.file
import zlc.season.rxdownload4.task.Task
import zlc.season.rxdownload4.utils.getFileNameFromUrl

@SuppressLint("SetTextI18n")
open class BaseFragment : Fragment() {

    private var disposable: Disposable? = null
    private val baseUrl = "https://vanced.app/api/v1/apks/v15.05.54"

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

    fun uninstallApp(pkgName: String) {
        try {
            val uri = Uri.parse("package:$pkgName")
            val uninstall = Intent(Intent.ACTION_DELETE, uri)
            uninstall.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            uninstall.putExtra(Intent.EXTRA_RETURN_RESULT, true)
            startActivityForResult(uninstall, APP_UNINSTALL)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "Failed to uninstall", Toast.LENGTH_SHORT).show()
        }
    }

    fun downloadSplits(type: String, loadBar: ProgressBar, dlText: TextView, loadCircle: ProgressBar) {
        val prefs = activity?.getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
        prefs?.edit()?.putBoolean("isVancedDownloading", true)?.apply()
        val variant = PreferenceManager.getDefaultSharedPreferences(activity).getString("vanced_variant", "nonroot")
        val lang = prefs?.getString("lang", "en")
        val theme = prefs?.getString("theme", "dark")
        val arch =
            when {
                Build.SUPPORTED_ABIS.contains("x86") -> "x86"
                Build.SUPPORTED_ABIS.contains("arm64-v8a") -> "arm64_v8a"
                else -> "armeabi_v7a"
            }
        val url =
            when (type) {
                "arch" -> "$baseUrl/$variant/Config/config.$arch.apk"
                "theme" -> "$baseUrl/$variant/Theme/$theme.apk"
                "lang" -> "$baseUrl/$variant/Language/split_config.$lang.apk"
                "enlang" -> "$baseUrl/$variant/Language/split_config.en.apk"
                else -> throw NotImplementedError("This type of APK is NOT valid. What the hell did you even do?")
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
            .observeOn(Schedulers.single())
            .subscribeBy(
                onNext = { progress ->
                    activity?.runOnUiThread {
                        val filename = getFileNameFromUrl(url)
                        loadBar.visibility = View.VISIBLE
                        dlText.visibility = View.VISIBLE
                        dlText.text = "Downloading $filename..."
                        loadBar.progress = progress.percent().toInt()
                    }
                },
                onComplete = {
                    when (type) {
                        "arch" -> downloadSplits("theme", loadBar, dlText, loadCircle)
                        "theme" -> downloadSplits("lang", loadBar, dlText, loadCircle)
                        "lang" -> {
                            if (lang == "en" || type == "enlang") {
                                activity?.runOnUiThread { loadCircle.visibility = View.VISIBLE }
                                if (variant == "root") {
                                    launchRootInstaller()
                                } else {
                                    launchInstaller()
                                }
                            } else {
                                downloadSplits("enlang", loadBar, dlText, loadCircle)
                            }
                        }
                    }
                },
                onError = { throwable ->
                    Toast.makeText(activity, throwable.toString(), Toast.LENGTH_SHORT).show()
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

    fun installMicrog(loadBar: ProgressBar?, dlText: TextView?) {
        val prefs = activity?.getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
        prefs?.edit()?.putBoolean("isMicrogDownloading", true)?.apply()

        val apkUrl = GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/microg.json")
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
            .observeOn(Schedulers.newThread())
            .subscribeBy(
                onNext = { progress ->
                    activity?.runOnUiThread {
                        val filename = getFileNameFromUrl(dwnldUrl)
                        loadBar?.visibility = View.VISIBLE
                        dlText?.visibility = View.VISIBLE
                        dlText?.text = "Downloading $filename..."
                        loadBar?.progress = progress.percent().toInt()
                    }
                },
                onComplete = {
                    activity?.runOnUiThread {
                        loadBar?.visibility = View.GONE
                        dlText?.visibility = View.GONE
                    }
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
                    intent.putExtra(Intent.EXTRA_RETURN_RESULT, true)
                    startActivityForResult(intent, MICROG_INSTALL)
                },
                onError = { throwable ->
                    Toast.makeText(requireContext(), throwable.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            )
    }

    companion object {
        const val APP_UNINSTALL = 69
        const val MICROG_INSTALL = 420
    }

}