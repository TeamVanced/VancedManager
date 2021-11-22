package com.vanced.manager.core.preferences.holder

import com.vanced.manager.core.preferences.managerBooleanPreference
import com.vanced.manager.core.preferences.managerLongPreference
import com.vanced.manager.core.preferences.managerStringPreference
import com.vanced.manager.core.preferences.managerStringSetPreference
import com.vanced.manager.ui.theme.defAccentColor

var useCustomTabsPref by managerBooleanPreference(USE_CUSTOM_TABS_KEY)
var managerVariantPref by managerStringPreference(
    MANAGER_VARIANT_KEY,
    MANAGER_VARIANT_DEFAULT_VALUE
)

var managerThemePref by managerStringPreference(MANAGER_THEME_KEY, MANAGER_THEME_DEFAULT_VALUE)
var managerAccentColorPref by managerLongPreference(MANAGER_ACCENT_COLOR_KEY, defAccentColor)

var vancedThemePref by managerStringPreference(APP_VANCED_THEME_KEY, VANCED_THEME_DEFAULT_VALUE)
var vancedVersionPref by managerStringPreference(APP_VANCED_VERSION_KEY, APP_VERSION_DEFAULT_VALUE)
var vancedLanguagesPref by managerStringSetPreference(
    APP_VANCED_LANGUAGE_KEY,
    VANCED_LANGUAGE_DEFAULT_VALUE
)

var musicVersionPref by managerStringPreference(APP_MUSIC_VERSION_KEY, APP_VERSION_DEFAULT_VALUE)

var vancedEnabled by managerBooleanPreference(VANCED_ENABLED_KEY, APP_ENABLED_DEFAULT_VALUE)
var musicEnabled by managerBooleanPreference(MUSIC_ENABLED_KEY, APP_ENABLED_DEFAULT_VALUE)