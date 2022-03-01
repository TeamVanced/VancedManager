package com.vanced.manager.di

import androidx.browser.customtabs.CustomTabsIntent
import org.koin.dsl.module

val customTabsModule = module {

    fun provideChromeCustomTabs(): CustomTabsIntent {
        return CustomTabsIntent.Builder()
            .build()
    }

    single { provideChromeCustomTabs() }
}