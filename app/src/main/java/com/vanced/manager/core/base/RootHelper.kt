package com.vanced.manager.core.base

import android.util.Log

class RootHelper {

    private val TAG: String = "VMRoot"

    fun rootRequest(): Boolean {
        return try {
            true
        } catch (e: Exception) {
            Log.d(TAG, "Unable to acquire root access")
            false
        }
    }

}