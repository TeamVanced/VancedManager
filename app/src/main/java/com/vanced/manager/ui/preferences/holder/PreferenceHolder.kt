package com.vanced.manager.ui.preferences.holder

import com.vanced.manager.ui.preferences.managerBooleanPreference
import com.vanced.manager.ui.preferences.managerLongPreference
import com.vanced.manager.ui.preferences.managerStringPreference
import com.vanced.manager.ui.preferences.managerStringSetPreference
import com.vanced.manager.ui.theme.defAccentColor

val useCustomTabsPref = managerBooleanPreference(USE_CUSTOM_TABS_KEY)

val managerVariantPref = managerStringPreference(MANAGER_VARIANT_KEY, MANAGER_VARIANT_DEFAULT_VALUE)

val managerThemePref = managerStringPreference(MANAGER_THEME_KEY, "System Default")
val managerAccentColorPref = managerLongPreference(MANAGER_ACCENT_COLOR_KEY, defAccentColor)

val vancedThemePref = managerStringPreference(APP_VANCED_THEME_KEY, "Dark")
val vancedVersionPref = managerStringPreference(APP_VANCED_VERSION_KEY, "latest")
val vancedLanguagesPref = managerStringSetPreference(APP_VANCED_LANGUAGE_KEY, setOf("en"))

val musicVersionPref = managerStringPreference(APP_MUSIC_VERSION_KEY, "latest")

val vancedEnabled = managerBooleanPreference(VANCED_ENABLED_KEY, VANCED_ENABLED_DEFAULT_VALUE)
val musicEnabled = managerBooleanPreference(MUSIC_ENABLED_KEY, MUSIC_ENABLED_DEFAULT_VALUE)