package com.vanced.manager.utils

import android.content.Context
import com.dezlum.codelabs.getjson.GetJson
import com.vanced.manager.BuildConfig
import com.vanced.manager.R
import java.lang.IllegalStateException

object InternetTools {
    fun getFileNameFromUrl(url: String) = url.substring(url.lastIndexOf('/')+1, url.length)

    fun displayJsonString(json: String, obj: String, context: Context): String {
        return if (GetJson().isConnected(context)) {
            try {
                GetJson().AsJSONObject("https://vanced.app/api/v1/$json").get(obj).asString
            } catch (e: IllegalStateException) {
                GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/$json").get(obj).asString
            }
        } else {
            context.getString(R.string.unavailable)
        }
    }

    fun displayJsonInt(json: String, obj: String, context: Context): Int {
        return if (GetJson().isConnected(context)) {
            try {
                GetJson().AsJSONObject("https://vanced.app/api/v1/$json").get(obj).asInt
            } catch (e: IllegalStateException) {
                GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/$json").get(obj).asInt
            }
        } else 0
    }

    fun isUpdateAvailable(): Boolean {
        val checkUrl = GetJson().AsJSONObject("https://vanced.app/api/v1/manager.json")
        val remoteVersion = checkUrl.get("versionCode").asInt

        return remoteVersion > BuildConfig.VERSION_CODE
    }

    fun getLatestVancedUrl(context: Context): String {
        return if (GetJson().isConnected(context)) {
            val latestVer = GetJson().AsJSONObject("https://x1nto.github.io/vanced.json").getAsJsonObject("version").asString
            "https://vanced.app/api/v1/apks/$latestVer"
        } else
            "https://vanced.app/api/v1/apks/v15.05.54"
    }

}

