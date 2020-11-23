package com.vanced.manager.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.ObservableField
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.vanced.manager.BuildConfig
import com.vanced.manager.R
import com.vanced.manager.utils.AppUtils.generateChecksum
import com.vanced.manager.utils.Extensions.getDefaultPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object InternetTools {

    private const val TAG = "VMNetTools"

    var vanced = ObservableField<JsonObject?>()
    var music = ObservableField<JsonObject?>()
    var microg = ObservableField<JsonObject?>()
    var manager = ObservableField<JsonObject?>()

    var vancedVersions = ObservableField<JsonArray<String>>()
    var musicVersions = ObservableField<JsonArray<String>>()

    //var braveTiers = ObservableField<JsonObject?>()

    fun openUrl(url: String, color: Int, context: Context) {
        val customTabPrefs = getDefaultSharedPreferences(context).getBoolean("use_custom_tabs", true)
        if (customTabPrefs) {
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(ContextCompat.getColor(context, color))
            val customTabsIntent = builder.build()
            customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            customTabsIntent.launchUrl(context, url.toUri())
        } else
            context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    fun getFileNameFromUrl(url: String) = url.substring(url.lastIndexOf('/') + 1, url.length)

    suspend fun loadJson(context: Context) = withContext(Dispatchers.IO) {
        val installUrl = context.getDefaultPrefs().getString("install_url", baseUrl)
        val latest = JsonHelper.getJson("$installUrl/latest.json?fetchTime=${SimpleDateFormat("HHmmss", Locale.ROOT)}")
        val versions = JsonHelper.getJson("$installUrl/versions.json?fetchTime=${SimpleDateFormat("HHmmss", Locale.ROOT)}")
//      braveTiers.apply {
//          set(getJson("$installUrl/sponsor.json"))
//          notifyChange()
//      }

        vanced.apply {
            set(latest?.obj("vanced"))
            notifyChange()
        }
        vancedVersions.set(versions?.array("vanced"))
        music.apply {
            set(latest?.obj("music"))
            notifyChange()
        }
        musicVersions.set(versions?.array("music"))
        microg.apply {
            set(latest?.obj("microg"))
            notifyChange()
        }
        manager.apply {
            set(latest?.obj("manager"))
            notifyChange()
        }

    }

    private suspend fun getJsonString(file: String, obj: String, context: Context): String {
        val installUrl = context.getDefaultPrefs().getString("install_url", baseUrl)
        return try {
            JsonHelper.getJson("$installUrl/$file")?.string(obj) ?: context.getString(R.string.unavailable)
        } catch (e: Exception) {
            Log.e(TAG, "Error: ", e)
            context.getString(R.string.unavailable)
        }
    }

    fun isUpdateAvailable(): Boolean {
        while (true) {
            if (manager.get() != null) {
                return manager.get()?.int("versionCode") ?: 0 > BuildConfig.VERSION_CODE
            }
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

            sum.toLowerCase(Locale.ENGLISH) == sha256.toLowerCase(Locale.ENGLISH)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    const val baseUrl = "https://vancedapp.com/api/v1"

}