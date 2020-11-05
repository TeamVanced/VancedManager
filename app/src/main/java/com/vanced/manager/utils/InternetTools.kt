package com.vanced.manager.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.vanced.manager.BuildConfig
import com.vanced.manager.R
import com.vanced.manager.utils.Extensions.getDefaultPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    fun openUrl(Url: String, color: Int, context: Context) {
        val customTabPrefs = getDefaultSharedPreferences(context).getBoolean("use_customtabs", true)
        if (customTabPrefs) {
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(ContextCompat.getColor(context, color))
            val customTabsIntent = builder.build()
            customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            customTabsIntent.launchUrl(context, Uri.parse(Url))
        } else
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Url)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    fun getFileNameFromUrl(url: String) = url.substring(url.lastIndexOf('/')+1, url.length)

    suspend fun getObjectFromJson(url: String, obj: String): String {
        return try {
            JsonHelper.getJson(url)?.string(obj) ?: ""
        } catch  (e: Exception) {
            Log.e(TAG, "Error: ", e)
            ""
        }
    }

    suspend fun getArrayFromJson(url: String, array: String): MutableList<String> {
        return try {
            JsonHelper.getJson(url)?.array<String>(array)?.value ?: mutableListOf("null")
        } catch (e: Exception) {
            Log.e(TAG, "Error: ", e)
            mutableListOf("null")
        }
    }

    suspend fun getJsonInt(file: String, obj: String, context: Context): Int {
        val installUrl = getDefaultSharedPreferences(context).getString("install_url", baseUrl)
        return try {
            JsonHelper.getJson("$installUrl/$file")?.int(obj) ?: 0
        } catch (e: Exception) {
            Log.e(TAG, "Error: ", e)
            0
        }
    }

    fun loadJson(context: Context) = CoroutineScope(Dispatchers.IO).launch {
        val installUrl = context.getDefaultPrefs().getString("install_url", baseUrl)
        val latest = JsonHelper.getJson("$installUrl/latest.json?fetchTime=${SimpleDateFormat("HHmmss", Locale.getDefault())}")
        val versions = JsonHelper.getJson("$installUrl/versions.json?fetchTime=${SimpleDateFormat("HHmmss", Locale.getDefault())}")
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

    suspend fun getJsonString(file: String, obj: String, context: Context): String {
        val installUrl = context.getDefaultPrefs().getString("install_url", baseUrl)
        return try {
            JsonHelper.getJson("$installUrl/$file")?.string(obj) ?: context.getString(R.string.unavailable)
        } catch (e: Exception) {
            Log.e(TAG, "Error: ", e)
            context.getString(R.string.unavailable)
        }
    }

    fun isUpdateAvailable(context: Context): Boolean {
        val result = manager.get()?.int("versionCode") ?: 0

        return result > BuildConfig.VERSION_CODE
    }

    const val baseUrl = "https://vancedapp.com/api/v1"

}