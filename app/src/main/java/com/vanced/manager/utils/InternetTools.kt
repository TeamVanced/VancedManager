package com.vanced.manager.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.vanced.manager.BuildConfig
import com.dezlum.codelabs.getjson.GetJson
import okhttp3.*
import com.vanced.manager.R
import org.json.JSONObject
import java.io.IOException

object InternetTools {

    fun openUrl(Url: String, color: Int, context: Context) {
        val customTabPrefs = getDefaultSharedPreferences(context).getBoolean("use_customtabs", true)
        if (customTabPrefs) {
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(ContextCompat.getColor(context, color))
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(context, Uri.parse(Url))
        } else
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Url)))
    }

    fun getFileNameFromUrl(url: String) = url.substring(url.lastIndexOf('/')+1, url.length)

    fun getObjectFromJson(url: String, obj: String, context: Context): String {
        return if (GetJson().isConnected(context))
            GetJson().AsJSONObject(url).get(obj).asString
        else
            ""
    }

    fun getJsonInt(file: String, obj: String, context: Context): Int {
        val client = OkHttpClient()
        val url = "${getDefaultSharedPreferences(context).getString("install_url", baseUrl)}/$file"
        var toReturn = 0

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                toReturn = 0
            }

            override fun onResponse(call: Call, response: Response) {
                toReturn = JSONObject(response.body?.string()!!).getInt(obj)
                Log.d("VMResponse", toReturn.toString())
            }

        })

        return toReturn
    }

    fun getJsonString(file: String, obj: String, context: Context): String {
        val client = OkHttpClient()
        val url = "${getDefaultSharedPreferences(context).getString("install_url", baseUrl)}/$file"
        var toReturn = ""

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                toReturn = context.getString(R.string.unavailable)
            }

            override fun onResponse(call: Call, response: Response) {
                toReturn = JSONObject(response.body?.string()!!).getString(obj)
                Log.d("VMResponse", toReturn)
            }

        })

        return toReturn
    }

    fun isUpdateAvailable(): Boolean {
        val checkUrl = GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/manager.json")
        val remoteVersion = checkUrl.get("versionCode").asInt

        return remoteVersion > BuildConfig.VERSION_CODE
    }

    const val baseUrl = "https://vanced.app/api/v1"

}