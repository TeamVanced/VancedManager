package com.vanced.manager.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import androidx.preference.PreferenceManager
import com.crowdin.platform.Crowdin
import java.util.*

class LanguageContextWrapper(base: Context?) : ContextWrapper(base) {

    companion object {

        fun wrap(context: Context): ContextWrapper {
            val config: Configuration = context.resources.configuration
            val pref = PreferenceManager.getDefaultSharedPreferences(context).getString("manager_lang", "System Default")
            val locale =
                when {
                    pref == "System Default" -> Locale(config.locale.displayLanguage)
                    pref?.length!! > 2 -> Locale(pref.substring(0, pref.length - 3), pref.substring(pref.length - 2))
                    else -> Locale(pref)
                }
            Locale.setDefault(locale)
            config.setLocale(locale)
            context.createConfigurationContext(config)
            Crowdin.wrapContext(context)
            return LanguageContextWrapper(context)
        }

    }

}