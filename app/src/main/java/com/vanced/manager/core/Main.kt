package com.vanced.manager.core

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.dezlum.codelabs.getjson.GetJson
import com.vanced.manager.R
import com.vanced.manager.core.base.BaseActivity
import zlc.season.rxdownload4.file
import java.io.File

// This activity will NOT be used in manifest
// since MainActivity will extend it
@SuppressLint("Registered")
open class Main: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) +
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    666
                )
            }

        }

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val firstStart = prefs.getBoolean("firstStart", true)
        val isUpgrading = prefs.getBoolean("isUpgrading", false)

        //Easter Egg
        val falseStatement = prefs.getBoolean("statement", true)

        when {
            firstStart -> showSecurityDialog()
            !falseStatement -> statementFalse()
            isUpgrading -> {
                val apkUrl = GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/manager.json")
                val dwnldUrl = apkUrl.get("url").asString

                if (dwnldUrl.file().exists())
                    dwnldUrl.file().delete()

                prefs.edit().putBoolean("isUpgrading", false).apply()
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            69 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT)
                        .show()
                }
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSecurityDialog() {
        AlertDialog.Builder(this)
            .setTitle(resources.getString(R.string.welcome))
            .setMessage(resources.getString(R.string.security_context))
            .setPositiveButton(resources.getString(R.string.close)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.edit().putBoolean("firstStart", false).apply()
    }

    //Easter Egg
    private fun statementFalse() {
        AlertDialog.Builder(this)
            .setTitle("Wait what?")
            .setMessage("So this statement is false huh? I'll go with True!")
            .setPositiveButton("wut?") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.edit().putBoolean("statement", true).apply()
    }

}