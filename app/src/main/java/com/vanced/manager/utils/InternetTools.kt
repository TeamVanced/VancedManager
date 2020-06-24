package com.vanced.manager.utils

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.dezlum.codelabs.getjson.GetJson
import com.vanced.manager.R
import java.lang.IllegalStateException

object InternetTools {

    fun openUrl(Url: String, color: Int, context: Context) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(context, color))
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(context, Uri.parse(Url))
    }

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

}

