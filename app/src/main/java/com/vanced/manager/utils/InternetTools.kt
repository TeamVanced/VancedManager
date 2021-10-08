package com.vanced.manager.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.vanced.manager.R
import com.vanced.manager.utils.AppUtils.generateChecksum
import com.vanced.manager.utils.AppUtils.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.util.*

private const val TAG = "VMNetTools"

val vanced = MutableLiveData<JsonObject?>()
val music = MutableLiveData<JsonObject?>()
val microg = MutableLiveData<JsonObject?>()
val manager = MutableLiveData<JsonObject?>()

val vancedVersions = MutableLiveData<JsonArray<String>?>()
val musicVersions = MutableLiveData<JsonArray<String>?>()

val isFetching = MutableLiveData<Boolean>()

var isMicrogBroken: Boolean = false

var baseInstallUrl = ""

fun openUrl(url: String, color: Int, context: Context) {
    try {
        val customTabPrefs =
            getDefaultSharedPreferences(context).getBoolean("use_custom_tabs", true)
        if (customTabPrefs) {
            val builder = CustomTabsIntent.Builder()
            val params = CustomTabColorSchemeParams.Builder()
                .setToolbarColor(ContextCompat.getColor(context, color))
            builder.setDefaultColorSchemeParams(params.build())
            val customTabsIntent = builder.build()
            customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            customTabsIntent.launchUrl(context, url.toUri())
        } else
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    url.toUri()
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )

    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show()
    } catch (e: SecurityException) {
        Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show()
    }
}

fun getFileNameFromUrl(url: String) = url.substring(url.lastIndexOf('/') + 1, url.length)

//TODO: Use a better connection method that doesn't cause inappropriate blocks
@Suppress("BlockingMethodInNonBlockingContext")
suspend fun loadJson(context: Context) = withContext(Dispatchers.IO) {
    isFetching.postValue(true)
    val installUrl = context.defPrefs.installUrl
    if (baseInstallUrl == "" && installUrl != null) {
        baseInstallUrl = installUrl
    }

    try {
        val latestbaseUrl = "$baseInstallUrl/latest.json"
        val connection = URL(latestbaseUrl).openConnection() as HttpURLConnection
        connection.apply {
            connectTimeout = 5000
            readTimeout = 5000
            connect()
        }
        if (connection.responseCode != 200) {
            log(TAG, latestbaseUrl + ": " + connection.responseCode.toString())
            baseInstallUrl = "https://mirror.codebucket.de/vanced/api/v1"
        }
    } catch (e: IOException) {
        baseInstallUrl = "https://mirror.codebucket.de/vanced/api/v1"
    } catch (e: SocketTimeoutException) {
        log(TAG, "connection timed out")
        baseInstallUrl = "https://mirror.codebucket.de/vanced/api/v1"
    }

    log(TAG, "Fetching using URL: $baseInstallUrl")

    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    val second = calendar.get(Calendar.SECOND)
    val fetchTime = "fetchTime=$hour$minute$second"

    val latest = getJson("$baseInstallUrl/latest.json?$fetchTime")
    val versions = getJson("$baseInstallUrl/versions.json?$fetchTime")
    isMicrogBroken = latest?.boolean("is_microg_broken") ?: false
    vanced.postValue(latest?.obj("vanced"))
    vancedVersions.postValue(versions?.array("vanced"))
    music.postValue(latest?.obj("music"))
    musicVersions.postValue(versions?.array("music"))
    microg.postValue(latest?.obj("microg"))
    manager.postValue(latest?.obj("manager"))
    isFetching.postValue(false)
}

private suspend fun getJsonString(file: String, obj: String, context: Context): String {
    return try {
        getJson("$baseInstallUrl/$file")?.string(obj) ?: context.getString(R.string.unavailable)
    } catch (e: Exception) {
        Log.e(TAG, "Error: ", e)
        context.getString(R.string.unavailable)
    }
}

suspend fun getSha256(hashUrl: String, obj: String, context: Context): String {
    return getJsonString(hashUrl, obj, context)
}

fun checkSHA256(sha256: String, updateFile: File): Boolean {
    return try {
        val dataBuffer = updateFile.readBytes()
        // Generate the checksum
        val sum = generateChecksum(dataBuffer)

        sum.equals(sha256, ignoreCase = true)
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

const val baseUrl = "https://api.vancedapp.com/api/v1"