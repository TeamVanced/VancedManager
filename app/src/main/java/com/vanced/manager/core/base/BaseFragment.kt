package com.vanced.manager.core.base

import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageInstaller
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.dezlum.codelabs.getjson.GetJson
import com.vanced.manager.core.installer.SplitInstallerService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import zlc.season.rxdownload4.delete
import zlc.season.rxdownload4.download
import zlc.season.rxdownload4.file
import zlc.season.rxdownload4.task.Task
import zlc.season.rxdownload4.utils.getFileNameFromUrl
import java.io.*

open class BaseFragment : Fragment() {

    private var disposable: Disposable? = null
    private var packageInstaller: PackageInstaller? = activity?.packageManager?.packageInstaller

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
                    if (isInstalling) {
                        if (apk == "lang") {
                            if (apkVar != "en")
                                downloadEn(loadBar)
                        }
                        else installSplitApk()

                    } else {
                        view?.findNavController()?.navigate(navigate)
                    }

                },
                onError = { throwable ->
                    Toast.makeText(requireContext(), throwable.toString(), Toast.LENGTH_SHORT).show()
                }
            )
    }

    private fun downloadEn(loadBar: ProgressBar) {
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
                    loadBar.visibility = View.VISIBLE
                    loadBar.progress = progress.percent().toInt()
                },
                onComplete = {
                    loadBar.visibility = View.GONE
                    installSplitApk()
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
                    startActivityForResult(intent, 420)
                },
                onError = { throwable ->
                    Toast.makeText(requireContext(), throwable.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            )
    }

    fun uninstallApk(pkgUri: String, requestCode: Int) {
        try {
            val uri = Uri.parse(pkgUri)
            val uninstall = Intent(Intent.ACTION_DELETE, uri)
            startActivityForResult(uninstall, requestCode)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "App not installed", Toast.LENGTH_SHORT).show()
            activity?.recreate()
        }
    }

    private fun installSplitApk(): Int {
        val apkFolderPath = activity?.cacheDir?.path
        val nameSizeMap = HashMap<String, Long>()
        var totalSize: Long = 0
        var sessionId = 0
        val folder = File(apkFolderPath!!)
        val listOfFiles = folder.listFiles()
        try {
            for (listOfFile in listOfFiles!!) {
                if (listOfFile.isFile) {
                    Log.d("AppLog", "installApk: " + listOfFile.name)
                    nameSizeMap[listOfFile.name] = listOfFile.length()
                    totalSize += listOfFile.length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        }
        val installParams = PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
        installParams.setSize(totalSize)
        try {
            sessionId = packageInstaller?.createSession(installParams)!!
            Log.d("AppLog","Success: created install session [$sessionId]")
            for ((key, value) in nameSizeMap) {
                doWriteSession(sessionId, apkFolderPath + key, value, key)
            }
            doCommitSession(sessionId)
            Log.d("AppLog","Success")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return sessionId
    }

    private fun doWriteSession(sessionId: Int, inPath: String?, sizeBytes: Long, splitName: String): Int {
        var inPathToUse = inPath
        var sizeBytesToUse = sizeBytes
        if ("-" == inPathToUse) {
            inPathToUse = null
        } else if (inPathToUse != null) {
            val file = File(inPathToUse)
            if (file.isFile)
                sizeBytesToUse = file.length()
        }
        var session: PackageInstaller.Session? = null
        var inputStream: InputStream? = null
        var out: OutputStream? = null
        try {
            session = packageInstaller?.openSession(sessionId)
            if (inPathToUse != null) {
                inputStream = FileInputStream(inPathToUse)
            }
            out = session?.openWrite(splitName, 0, sizeBytesToUse)
            var total = 0
            val buffer = ByteArray(65536)
            var c: Int
            while (true) {
                c = inputStream!!.read(buffer)
                if (c == -1)
                    break
                total += c
                out?.write(buffer, 0, c)
            }
            if (out != null) {
                session?.fsync(out)
            }
            Log.d("AppLog", "Success: streamed $total bytes")
            return PackageInstaller.STATUS_SUCCESS
        } catch (e: IOException) {
            Log.e("AppLog", "Error: failed to write; " + e.message)
            return PackageInstaller.STATUS_FAILURE
        } finally {
            try {
                out?.close()
                inputStream?.close()
                session?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun doCommitSession(sessionId: Int) {
        var session: PackageInstaller.Session? = null
        try {
            try {
                session = packageInstaller?.openSession(sessionId)
                val callbackIntent = Intent(activity?.applicationContext, SplitInstallerService::class.java)
                val pendingIntent = PendingIntent.getService(activity?.applicationContext, 0, callbackIntent, 0)
                session?.commit(pendingIntent.intentSender)
                session?.close()
                Log.d("AppLog", "install request sent")
                Log.d("AppLog", "doCommitSession: " + packageInstaller?.mySessions)
                Log.d("AppLog", "doCommitSession: after session commit ")
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } finally {
            session!!.close()
        }
    }

}