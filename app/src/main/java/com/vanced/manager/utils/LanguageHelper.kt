package com.vanced.manager.utils

import android.content.Context
import com.vanced.manager.R
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
                loc.getDisplayName(loc)
            }
            else -> {
                val loc = Locale(language)
                loc.getDisplayName(loc)
            }
        }

    }

}