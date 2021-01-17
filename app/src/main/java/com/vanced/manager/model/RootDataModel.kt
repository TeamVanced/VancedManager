package com.vanced.manager.model

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.LiveData
import com.beust.klaxon.JsonObject
import com.vanced.manager.R
import com.vanced.manager.utils.PackageHelper

class RootDataModel(
    jsonObject: LiveData<JsonObject?>,
    private val context: Context,
    override val appPkg: String,
    override val appName: String,
    override val appIcon: Drawable?,
    private val scriptName: String
): DataModel(
    jsonObject, context, appPkg, appName, appIcon
) {

    init {
        Log.d("test", appPkg)
    }

    override fun getPkgVersionName(pkg: String): String {
        return if (PackageHelper.scriptExists(scriptName)) {
            super.getPkgVersionName(pkg)
        } else {
            context.getString(R.string.unavailable)
        }
    }

    override fun compareInt(int1: Int?, int2: Int?): String {
        return if (PackageHelper.scriptExists(scriptName)) {
            super.compareInt(int1, int2)
        } else {
            context.getString(R.string.install)
        }
    }

}