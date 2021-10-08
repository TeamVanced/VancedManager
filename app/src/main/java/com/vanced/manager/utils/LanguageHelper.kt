package com.vanced.manager.utils

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import com.vanced.manager.R
import java.util.*

fun getLanguageFormat(context: Context, language: String): String {
    return when {
        language == "System Default" -> context.getString(R.string.system_default)
        language.contains("_") -> {
            val loc = Locale(
                language.substringBefore("_"),
                language.substringAfter("_")
            )
            loc.getDisplayName(loc).capitalize(Locale.ENGLISH)
        }
        else -> {
            val loc = Locale(language)
            loc.getDisplayName(loc).capitalize(Locale.ENGLISH)
        }
    }

}

@Suppress("DEPRECATION")
fun getDefaultVancedLanguages(): String {
    val serverLangs = vanced.value?.array("langs") ?: mutableListOf("")
    val sysLocales =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Resources.getSystem().configuration.locales.toLangTags() else arrayOf(
            Resources.getSystem().configuration.locale.language
        )
    val finalLangs = mutableListOf<String>()
    sysLocales.forEach { sysLocale ->
        when {
            sysLocale == "en" -> finalLangs.add(sysLocale)
            serverLangs.contains(sysLocale) -> finalLangs.add(sysLocale)
        }
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