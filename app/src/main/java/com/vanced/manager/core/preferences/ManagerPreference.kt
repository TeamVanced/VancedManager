package com.vanced.manager.core.preferences

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.edit
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.KProperty

fun <T> managerPreference(
    key: String,
    defaultValue: T,
    getter: SharedPreferences.(key: String, defaultValue: T) -> T?,
    setter: SharedPreferences.Editor.(key: String, newValue: T) -> Unit
) = ManagerPreference(key, defaultValue, getter, setter)

fun managerStringPreference(
    key: String,
    defaultValue: String = ""
) = managerPreference(
    key = key,
    defaultValue = defaultValue,
    getter = SharedPreferences::getString,
    setter = SharedPreferences.Editor::putString
)

fun managerStringSetPreference(
    key: String,
    defaultValue: Set<String> = setOf()
) = managerPreference(
    key = key,
    defaultValue = defaultValue,
    getter = SharedPreferences::getStringSet,
    setter = SharedPreferences.Editor::putStringSet
)

fun managerBooleanPreference(
    key: String,
    defaultValue: Boolean = false
) = managerPreference(
    key = key,
    defaultValue = defaultValue,
    getter = SharedPreferences::getBoolean,
    setter = SharedPreferences.Editor::putBoolean
)

fun managerIntPreference(
    key: String,
    defaultValue: Int = 0
) = managerPreference(
    key = key,
    defaultValue = defaultValue,
    getter = SharedPreferences::getInt,
    setter = SharedPreferences.Editor::putInt
)

fun managerLongPreference(
    key: String,
    defaultValue: Long = 0
) = managerPreference(
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

    private val _value = mutableStateOf(defaultValue)
    val value: State<T> = _value

    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value.value

    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        save(newValue)
    }

    fun save(newValue: T) {
        _value.value = newValue
        sharedPreferences.edit {
            setter(key, newValue)
        }
    }

    //It's Chewsday innit
    init {
        _value.value = sharedPreferences.getter(key, defaultValue) ?: defaultValue
    }

}