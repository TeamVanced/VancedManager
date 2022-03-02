package com.vanced.manager.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.topjohnwu.superuser.BusyBoxInstaller
import com.topjohnwu.superuser.Shell
import com.vanced.manager.BuildConfig

class SplashScreenActivity : ComponentActivity() {

    init {
        Shell.enableVerboseLogging = BuildConfig.DEBUG
        Shell.setDefaultBuilder(
            Shell.Builder
                .create()
                .setInitializers(BusyBoxInstaller::class.java)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Shell.getShell {
            startActivity(
                Intent(this, MainActivity::class.java)
            )
            finish()
        }
    }

}