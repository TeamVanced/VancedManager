package com.vanced.manager.ui.core

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.vanced.manager.ui.MainActivity
import com.vanced.manager.ui.WelcomeActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (getDefaultSharedPreferences(this).getBoolean("firstLaunch", true)) {
            startActivity(Intent(this@SplashScreenActivity, WelcomeActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            finish()
        }

    }
}
