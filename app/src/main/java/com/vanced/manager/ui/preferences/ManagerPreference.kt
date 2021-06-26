package com.vanced.manager.ui.preferences

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.KProperty

class ManagerPreference<T>(
    val key: Preferences.Key<T>,
    val defaultValue: T,
) : KoinComponent {

    private val dataStore: DataStore<Preferences> by inject()

    private val _value = mutableStateOf(defaultValue)
    val value: State<T> = _value

    @Composable
    fun rememberValue() = remember { value }

    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value.value

    suspend fun save(newValue: T) {
        _value.value = newValue
        dataStore.edit {
            it[key] = value.value
        }
    }

    //It's Chewsday innit - © Bri'ish ppl
    init {
        dataStore.data.map {
            _value.value = it[key] ?: defaultValue
        }
    }

}

fun managerStringPreference(
    key: Preferences.Key<String>,
    defaultValue: String = ""
) = ManagerPreference(key, defaultValue)

fun managerStringSetPreference(
    key: Preferences.Key<Set<String>>,
    defaultValue: Set<String> = setOf()
) = ManagerPreference(key, defaultValue)

fun managerBooleanPreference(
    key: Preferences.Key<Boolean>,
    defaultValue: Boolean = false
) = ManagerPreference(key, defaultValue)

fun managerIntPreference(
    key: Preferences.Key<Int>,
    defaultValue: Int = 0
) = ManagerPreference(key, defaultValue)

fun managerLongPreference(
    key: Preferences.Key<Long>,
    defaultValue: Long = 0
) = ManagerPreference(key, defaultValue)