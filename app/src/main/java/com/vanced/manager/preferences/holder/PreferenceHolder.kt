package com.vanced.manager.preferences.holder

import com.vanced.manager.preferences.managerBooleanPreference
import com.vanced.manager.preferences.managerLongPreference
import com.vanced.manager.preferences.managerStringPreference
import com.vanced.manager.preferences.managerStringSetPreference
import com.vanced.manager.ui.theme.defAccentColor

val useCustomTabsPref = managerBooleanPreference(USE_CUSTOM_TABS_KEY)
val managerVariantPref = managerStringPreference(MANAGER_VARIANT_KEY, MANAGER_VARIANT_DEFAULT_VALUE)

val managerThemePref = managerStringPreference(MANAGER_THEME_KEY, MANAGER_THEME_DEFAULT_VALUE)
val managerAccentColorPref = managerLongPreference(MANAGER_ACCENT_COLOR_KEY, defAccentColor)

val vancedThemePref = managerStringPreference(APP_VANCED_THEME_KEY, VANCED_THEME_DEFAULT_VALUE)
val vancedVersionPref = managerStringPreference(APP_VANCED_VERSION_KEY, APP_VERSION_DEFAULT_VALUE)
val vancedLanguagesPref = managerStringSetPreference(APP_VANCED_LANGUAGE_KEY, VANCED_LANGUAGE_DEFAULT_VALUE)

val musicVersionPref = managerStringPreference(APP_MUSIC_VERSION_KEY, APP_VERSION_DEFAULT_VALUE)

val vancedEnabled = managerBooleanPreference(VANCED_ENABLED_KEY, APP_ENABLED_DEFAULT_VALUE)
val musicEnabled = managerBooleanPreference(MUSIC_ENABLED_KEY, APP_ENABLED_DEFAULT_VALUE)