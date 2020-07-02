package com.vanced.manager.utils

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.dezlum.codelabs.getjson.GetJson
import com.vanced.manager.BuildConfig
import com.vanced.manager.R

object InternetTools {

    fun openUrl(Url: String, color: Int, context: Context) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(context, color))
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(context, Uri.parse(Url))
    }

    fun getFileNameFromUrl(url: String) = url.substring(url.lastIndexOf('/')+1, url.length)

    fun displayJsonString(json: String, obj: String, context: Context): String {
        val installUrl = PreferenceManager.getDefaultSharedPreferences(context).getString("install_url", baseUrl)
        return if (GetJson().isConnected(context))
            GetJson().AsJSONObject("$installUrl/$json").get(obj).asString
        else
            context.getString(R.string.unavailable)
    }

    fun displayJsonInt(json: String, obj: String, context: Context): Int {
        val installUrl = PreferenceManager.getDefaultSharedPreferences(context).getString("install_url", baseUrl)
        return if (GetJson().isConnected(context))
            GetJson().AsJSONObject("$installUrl/$json").get(obj).asInt
        else
            0

    }

    fun getObjectFromJson(url: String, obj: String, context: Context): String {
        return if (GetJson().isConnected(context))
            GetJson().AsJSONObject(url).get(obj).asString
        else
            ""
    }

    fun isUpdateAvailable(): Boolean {
        val checkUrl = GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/manager.json")
        val remoteVersion = checkUrl.get("versionCode").asInt

        return remoteVersion > BuildConfig.VERSION_CODE
    }

    const val baseUrl = "https://vanced.app/api/v1"

}