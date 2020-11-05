package com.vanced.manager.utils

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object MiuiHelper {

    fun isMiui(): Boolean = getSystemProps("ro.miui.ui.version.name")!!.isNotEmpty()

    private fun getSystemProps(propname: String): String? {
        val line: String
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $propname")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (e: IOException) {
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return line
    }

}