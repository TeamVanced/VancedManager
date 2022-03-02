package com.vanced.manager.preferences

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.KProperty

fun managerStringPreference(
    key: String,
    defaultValue: String = ""
) = ManagerPreference(
    key = key,
    defaultValue = defaultValue,
    getter = SharedPreferences::getString,
    setter = SharedPreferences.Editor::putString
)

fun managerStringSetPreference(
    key: String,
    defaultValue: Set<String> = setOf()
) = ManagerPreference(
    key = key,
    defaultValue = defaultValue,
    getter = SharedPreferences::getStringSet,
    setter = SharedPreferences.Editor::putStringSet
)

fun managerBooleanPreference(
    key: String,
    defaultValue: Boolean = false
) = ManagerPreference(
    key = key,
    defaultValue = defaultValue,
    getter = SharedPreferences::getBoolean,
    setter = SharedPreferences.Editor::putBoolean
)

fun managerIntPreference(
    key: String,
    defaultValue: Int = 0
) = ManagerPreference(
    key = key,
    defaultValue = defaultValue,
    getter = SharedPreferences::getInt,
    setter = SharedPreferences.Editor::putInt
)

fun managerLongPreference(
    key: String,
    defaultValue: Long = 0
) = ManagerPreference(
    key = key,
    defaultValue = defaultValue,
    getter = SharedPreferences::getLong,
    setter = SharedPreferences.Editor::putLong
)

class ManagerPreference<T>(
    private val key: String,
    private val defaultValue: T,
    private val getter: SharedPreferences.(key: String, defaultValue: T) -> T?,
    private val setter: SharedPreferences.Editor.(key: String, newValue: T) -> Unit
) : KoinComponent {

    private val sharedPreferences: SharedPreferences by inject()

    var value by mutableStateOf(sharedPreferences.getter(key, defaultValue) ?: defaultValue)
        private set

    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>
    ) = value

    operator fun setValue(
        thisRef: Any?,
        property: KProperty<*>,
        newValue: T
    ) {
        value = newValue
        sharedPreferences.edit {
            setter(key, newValue)
        }
    }

}