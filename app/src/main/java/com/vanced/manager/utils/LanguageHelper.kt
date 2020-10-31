package com.vanced.manager.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import android.util.Log
import androidx.annotation.RequiresApi
import com.vanced.manager.R
import com.vanced.manager.core.App
import java.util.*

object LanguageHelper {

    fun getLanguageFormat(context: Context, language: String): String {
        return when {
            language == "System Default" -> context.getString(R.string.system_default)
            language.length > 2 -> {
                val loc = Locale(
                        language.substring(0, language.length - 3),
                        language.substring(language.length - 2)
                )
                loc.getDisplayName(loc).capitalize(Locale.ENGLISH)
            }
            else -> {
                val loc = Locale(language)
                loc.getDisplayName(loc).capitalize(Locale.ENGLISH)
            }
        }

    }

    fun getDefaultVancedLanguages(activity: Activity): String {
        val serverLangs = (activity.applicationContext as App).vanced.get()!!.array<String>("langs")
        val sysLocales = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Resources.getSystem().configuration.locales.toLangTags() else arrayOf(Resources.getSystem().configuration.locale.language)
        val finalLangs = mutableListOf<String>()
        sysLocales.forEach { sysLocale ->
            if (sysLocale == "en")
                finalLangs.add(sysLocale)
            else if (serverLangs != null && serverLangs.contains(sysLocale))
                finalLangs.add(sysLocale)
        }

        return finalLangs.distinct().sorted().joinToString(", ")
    }

    fun getDefaultVancedLanguages(app: App): String {
        val serverLangs = app.vanced.get()!!.array<String>("langs")
        val sysLocales = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Resources.getSystem().configuration.locales.toLangTags() else arrayOf(Resources.getSystem().configuration.locale.language)
        val finalLangs = mutableListOf<String>()
        sysLocales.forEach { sysLocale ->
            if (sysLocale == "en")
                finalLangs.add(sysLocale)
            else if (serverLangs != null && serverLangs.contains(sysLocale))
                finalLangs.add(sysLocale)
        }

        return finalLangs.distinct().sorted().joinToString(", ")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun LocaleList.toLangTags(): Array<String> {
        val langTags: Array<String> = this.toLanguageTags().split(",").toTypedArray()
        for (i in 0 until this.size()) {
            langTags[i] = langTags[i].substring(0, 2)
        }
        return langTags
    }

}