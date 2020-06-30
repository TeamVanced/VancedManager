package com.vanced.manager.ui.viewmodels

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.vanced.manager.R

open class AboutViewModel(application: Application): AndroidViewModel(application) {

    fun openUrl(Url: String) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(getApplication(), R.color.GitHub))
        val customTabsIntent = builder.build()
        customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        customTabsIntent.launchUrl(getApplication(), Uri.parse(Url))
    }

}