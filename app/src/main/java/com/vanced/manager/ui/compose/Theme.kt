package com.vanced.manager.ui.compose

//import android.content.Context
//import androidx.compose.foundation.isSystemInDarkTheme
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.darkColors
//import androidx.compose.material.lightColors
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.preference.PreferenceManager.getDefaultSharedPreferences
//import com.vanced.manager.utils.mutableAccentColor
//
//const val Dark = "Dark"
//const val SystemDefault = "System Default"
//
//const val defAccentColor: Int = -13732865
//
//val Context.accentColor get() = mutableAccentColor.value ?: getDefaultSharedPreferences(this).getInt("manager_accent_color", defAccentColor)
//
//enum class Theme {
//    DARK, LIGHT
//}
//
//val lightColors = lightColors(
//    primary = Color(defAccentColor)
//)
//
//val darkColors = darkColors(
//    primary = Color(defAccentColor)
//)
//
//fun Context.retrieveTheme(): Theme = when (getDefaultSharedPreferences(this).getString("manager_theme", SystemDefault)) {
//    SystemDefault -> if (isSystemInDarkTheme()) Theme.DARK else Theme.LIGHT
//    Dark -> Theme.DARK
//    else -> Theme.LIGHT
//}
//
//val Context.isDarkTheme: Boolean
//    get() = retrieveTheme() == Theme.DARK
//
//fun Context.ManagerTheme(
//    content: @Composable () -> Unit
//) {
//    MaterialTheme(
//        colors = if (isDarkTheme) darkColors else lightColors,
//        content = content
//    )
//}