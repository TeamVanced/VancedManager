package com.vanced.manager.utils

import android.text.TextUtils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object MiuiHelper {

    fun isMiui(): Boolean {
        return !TextUtils.isEmpty(getSystemProps("ro.miui.ui.version.name"))
    }

    fun isMiuiOptimisationsDisabled(): Boolean {
        return if (isMiui())
            getSystemProps("persist.sys.miui_optimization") == "0"
        else
            false
    }

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