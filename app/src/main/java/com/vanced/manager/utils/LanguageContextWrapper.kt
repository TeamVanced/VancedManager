package com.vanced.manager.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import com.vanced.manager.utils.AppUtils.currentLocale
import java.util.*

class LanguageContextWrapper(base: Context?) : ContextWrapper(base) {

    companion object {

        fun wrap(context: Context): ContextWrapper {
            val config: Configuration = context.resources.configuration
            context.createConfigurationContext(setLocale(config, context))
            return LanguageContextWrapper(context)
        }

        @Suppress("DEPRECATION")
        private fun setLocale(config: Configuration, context: Context): Configuration {
            val pref = context.defPrefs.managerLang
            val sysLocale = Resources.getSystem().configuration.locale
            val locale = when {
                pref == "System Default" -> Locale(sysLocale.language, sysLocale.country)
                pref?.length!! > 2 -> Locale(
                    pref.substring(0, pref.length - 3),
                    pref.substring(pref.length - 2)
                )
                else -> Locale(pref)
            }
            currentLocale = locale
            Locale.setDefault(locale)
            config.setLocale(locale)
            return config
        }

    }

}