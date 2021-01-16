package com.vanced.manager.utils

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

private const val MIUI_PROP_NAME = "ro.miui.ui.version.name"

fun isMiui(): Boolean = !getSystemProps(MIUI_PROP_NAME).isNullOrEmpty()

private fun getSystemProps(propname: String): String? {
    var input: BufferedReader? = null
    return try {
        val process = Runtime.getRuntime().exec("getprop $propname")
        input = BufferedReader(InputStreamReader(process.inputStream), 1024)
        input.readLine()
    } catch (e: IOException) {
        null
    } finally {
        input?.close()
    }
}