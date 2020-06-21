package com.vanced.manager.utils

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat

object InternetTools {

    fun openUrl(Url: String, color: Int, context: Context) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(context, color))
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(context, Uri.parse(Url))
    }

    fun getFileNameFromUrl(url: String) = url.substring(url.lastIndexOf('/')+1, url.length)

}

