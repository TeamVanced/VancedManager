package com.vanced.manager.preferences.holder

import com.vanced.manager.preferences.managerStringPreference
import com.vanced.manager.preferences.managerStringSetPreference

var managerVariantPref by managerStringPreference(
    MANAGER_VARIANT_KEY,
    MANAGER_VARIANT_DEFAULT_VALUE
)

var vancedThemePref by managerStringPreference(APP_VANCED_THEME_KEY, VANCED_THEME_DEFAULT_VALUE)
var vancedVersionPref by managerStringPreference(APP_VANCED_VERSION_KEY, APP_VERSION_DEFAULT_VALUE)
var vancedLanguagesPref by managerStringSetPreference(
    APP_VANCED_LANGUAGE_KEY,
    VANCED_LANGUAGE_DEFAULT_VALUE
)

var musicVersionPref by managerStringPreference(APP_MUSIC_VERSION_KEY, APP_VERSION_DEFAULT_VALUE)