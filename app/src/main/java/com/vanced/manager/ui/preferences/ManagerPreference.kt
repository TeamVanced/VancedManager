package com.vanced.manager.ui.preferences

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.edit
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.KProperty

class ManagerPreference<T>(
    private val key: String,
    private val defaultValue: T,
    private val getter: (SharedPreferences) -> T,
    private val setter: (SharedPreferences.Editor, key: String, newValue: T) -> Unit
) : KoinComponent {

    private val sharedPreferences: SharedPreferences by inject()

    private val _value = mutableStateOf(defaultValue)
    val value: State<T> = _value

    @Composable
    fun rememberValue() = remember { value }

    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value.value

    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        save(newValue)
    }

    fun save(newValue: T) {
        _value.value = newValue
        sharedPreferences.edit {
            setter(this, key, newValue)
        }
    }

    //It's Chewsday innit
    init {
        _value.value = getter(sharedPreferences)
    }

}

fun <T> managerPreference(
    key: String,
    defaultValue: T,
    getter: (SharedPreferences) -> T,
    setter: (SharedPreferences.Editor, key: String, newValue: T) -> Unit
) = ManagerPreference(key, defaultValue, getter, setter)

fun managerStringPreference(
    key: String,
    defaultValue: String = ""
) = managerPreference(
    key = key,
    defaultValue = defaultValue,
    getter = { sharedPreferences ->
        SharedPreferences::getString.invoke(sharedPreferences, key, defaultValue) ?: defaultValue
    },
    setter = SharedPreferences.Editor::putString
)

fun managerStringSetPreference(
    key: String,
    defaultValue: Set<String> = setOf()
) = managerPreference(
    key = key,
    defaultValue = defaultValue,
    getter = { sharedPreferences ->
        SharedPreferences::getStringSet.invoke(sharedPreferences, key, defaultValue) ?: defaultValue
    },
    setter = SharedPreferences.Editor::putStringSet
)

fun managerBooleanPreference(
    key: String,
    defaultValue: Boolean = false
) =managerPreference(
    key = key,
    defaultValue = defaultValue,
    getter = { sharedPreferences ->
        SharedPreferences::getBoolean.invoke(sharedPreferences, key, defaultValue)
    },
    setter = SharedPreferences.Editor::putBoolean
)

fun managerIntPreference(
    key: String,
    defaultValue: Int = 0
) = managerPreference(
    key = key,
    defaultValue = defaultValue,
    getter = { sharedPreferences ->
        SharedPreferences::getInt.invoke(sharedPreferences, key, defaultValue)
    },
    setter = SharedPreferences.Editor::putInt
)

fun managerLongPreference(
    key: String,
    defaultValue: Long = 0
) = managerPreference(
    key = key,
    defaultValue = defaultValue,
    getter = { sharedPreferences ->
        SharedPreferences::getLong.invoke(sharedPreferences, key, defaultValue)
    },
    setter = SharedPreferences.Editor::putLong
)