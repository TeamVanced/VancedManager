package com.vanced.manager.ui.viewmodels

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.ObservableField
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.crowdin.platform.Crowdin
import com.vanced.manager.R
import com.vanced.manager.model.DataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class HomeViewModel(application: Application): AndroidViewModel(application) {

    //val variant = getDefaultSharedPreferences(application).getString("vanced_variant", "nonroot")
    var variant = "nonroot"
    
    val fetching = ObservableBoolean()
    
    val vanced = ObservableField<DataModel>()
    val microg = ObservableField<DataModel>()
    val music = ObservableField<DataModel>()
    val manager = ObservableField<DataModel>()
    
    fun fetchData() {
        CoroutineScope(Dispatchers.IO).launch {
            fetching.set(true)
            Crowdin.forceUpdate(getApplication())
            vanced.set(DataModel("vanced", variant, getApplication()))
            microg.set(DataModel("microg", context = getApplication()))
            music.set(DataModel("music", context = getApplication()))
            manager.set(DataModel("manager", context = getApplication()))
            fetching.set(false)
        }
    }

    fun openMicrogSettings() {
        try {
            val intent = Intent()
            intent.component = ComponentName(
                "com.mgoogle.android.gms",
                "org.microg.gms.ui.SettingsActivity"
            )
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(getApplication(), intent, null)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(getApplication(), "Error", Toast.LENGTH_SHORT).show()
        }
    }

    fun openUrl(Url: String) {
        val customTabPrefs = getDefaultSharedPreferences(getApplication()).getBoolean("use_customtabs", true)
        val color: Int =
            when (Url) {
                "https://discord.gg/TUVd7rd" -> R.color.Discord
                "https://t.me/joinchat/AAAAAEHf-pi4jH1SDlAL4w" -> R.color.Telegram
                "https://twitter.com/YTVanced" -> R.color.Twitter
                "https://reddit.com/r/vanced" -> R.color.Reddit
                "https://vanced.app" -> R.color.Vanced
                "https://brave.com/van874" -> R.color.Brave
                else -> R.color.Vanced
            }

        if (customTabPrefs) {
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(ContextCompat.getColor(getApplication(), color))
            val customTabsIntent = builder.build()
            customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            customTabsIntent.launchUrl(getApplication(), Uri.parse(Url))
        } else {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Url))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(getApplication(), intent , null)
        }
    }

    init {
        fetching.set(false)
        fetchData()
    }

}
