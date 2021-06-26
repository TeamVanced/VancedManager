package com.vanced.manager.ui.preferences.holder

import com.vanced.manager.ui.preferences.managerBooleanPreference
import com.vanced.manager.ui.preferences.managerLongPreference
import com.vanced.manager.ui.preferences.managerStringPreference
import com.vanced.manager.ui.preferences.managerStringSetPreference
import com.vanced.manager.ui.theme.defAccentColor

val useCustomTabsPref = managerBooleanPreference(useCustomTabsKey)

val managerVariantPref = managerStringPreference(managerVariantKey, MANAGER_VARIANT_DEFAULT_VALUE)

val managerThemePref = managerStringPreference(managerThemeKey, "System Default")
val managerAccentColorPref = managerLongPreference(managerAccentColorKey, defAccentColor)

val vancedThemePref = managerStringPreference(vancedThemeKey, "Dark")
val vancedVersionPref = managerStringPreference(vancedVersionKey, "latest")
val vancedLanguagesPref = managerStringSetPreference(vancedLanguageKey, setOf("en"))

val musicVersionPref = managerStringPreference(musicVersionKey, "latest")

val vancedEnabled = managerBooleanPreference(vancedEnabledKey, VANCED_ENABLED_DEFAULT_VALUE)
val musicEnabled = managerBooleanPreference(musicEnabledKey, MUSIC_ENABLED_DEFAULT_VALUE)