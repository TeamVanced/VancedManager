package com.vanced.manager.ui.preferences.holder

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

val useCustomTabsKey = booleanPreferencesKey(USE_CUSTOM_TABS_KEY)
val managerVariantKey = stringPreferencesKey(MANAGER_VARIANT_KEY)
val managerThemeKey = stringPreferencesKey("manager_theme")
val managerAccentColorKey = longPreferencesKey("manager_accent_color")

val vancedThemeKey = stringPreferencesKey(APP_VANCED_THEME_KEY)
val vancedVersionKey = stringPreferencesKey(APP_VANCED_VERSION_KEY)
val vancedLanguageKey = stringSetPreferencesKey(APP_VANCED_LANGUAGE_KEY)
val musicVersionKey = stringPreferencesKey(APP_MUSIC_VERSION_KEY)

val vancedEnabledKey = booleanPreferencesKey(VANCED_ENABLED_KEY)
val musicEnabledKey = booleanPreferencesKey(MUSIC_ENABLED_KEY)